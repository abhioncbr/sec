package com.sec.filling.analysis.spark.job

import org.apache.spark.SparkConf
import org.apache.spark.sql.DataFrame
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import scala.reflect.ClassTag
import org.apache.spark.sql.SQLContext
import java.io.IOException
import java.lang.Boolean
import scala.reflect.runtime.universe.TypeTag
import scalax.file.Path
import scala.sys.SystemProperties
import java.util.concurrent.ConcurrentHashMap
import com.sec.filling.analysis.spark.job.domain.DomainDef
import com.sec.filling.analysis.spark.job.util.AppConf
import com.capiq.sec.filling.common.util.PropertiesUtil
import org.apache.spark.sql.SaveMode

object JobContext  {
  private var jc: JobContext = null;
  
  private val isHdfs = Boolean.parseBoolean(PropertiesUtil.getProperty("file.sec.hdfs.storage"));
  private val basePath = if(isHdfs) PropertiesUtil.getProperty("file.sec.hdfs.base.storage.url") else PropertiesUtil.getProperty("file.sec.local.base.storage.url")
  private val inputPath = PropertiesUtil.getProperty("file.parquet.stat.data")

  def create(appName: String): JobContext = {
    val conf = new SparkConf().setAppName(appName)
    if (jc == null) {} else { jc.sparkContext.stop(); }
    jc = new JobContext(basePath, conf, inputPath)
    return jc
  }

  def createHdfs(appName: String): JobContext = {
    val conf = new SparkConf().setAppName(appName)
    if (jc == null) {} else { jc.sparkContext.stop(); }
    jc = new JobContext(basePath, conf, inputPath)
    return jc
  }
  
  
  def createForTest(className: String): JobContext = {
    val conf = new SparkConf().setAppName("Test-" + className)

    conf.setMaster("local[7]")
    conf.set("spark.io.compression.codec", "lz4")
    conf.set("spark.rdd.compress", "false")
    conf.set("spark.serializer.objectStreamReset", "2000")
    conf.set("spark.storage.memoryMapThreshold", "65536")
    conf.set("spark.default.parallelism", "4")
    conf.set("spark.reducer.maxMbInFlight", "12")
    conf.set("spark.shuffle.file.buffer.kb", "512");
    conf.set("spark.broadcast.factory", "org.apache.spark.broadcast.HttpBroadcastFactory");

    new JobContext(basePath, conf, inputPath)
  }

  def stop(jobContext: JobContext) {
    jobContext.sparkContext.stop()
  }
  
  def stopAndClear(jobContext: JobContext) {
    jobContext.sparkContext.stop()
    this.clear(jobContext)
  }

  def clear(jobContext: JobContext) {
    val path = Path.fromString(jobContext.basePath + "/" + jobContext.inputPath)
    try { path.deleteRecursively(continueOnFailure = true) } 
    catch { case e: IOException => e.printStackTrace() }
  }

  def coalleceOrRepartition[T](numPartitions: Int, df: RDD[T]): RDD[T] = {
    val res = if (numPartitions <= df.partitions.size) {
      df.coalesce(numPartitions, false)
    } else { df }
    res
  }

}

class JobContext(val basePath: String, val conf: SparkConf, val inputPath: String) extends Serializable {

  private val availTables = new ConcurrentHashMap[DomainDef.Value, String]();
  private val rddNElemsInOnePartition = initPartitionSizeMap
  private val cacheLoadedTables = AppConf.getConfBoolOrElse("spark-settings.sql.persist.table.all", false)

  def initPartitionSizeMap(): Map[DomainDef.Value, Int] = {
    val default = AppConf.getConfIntOrElse("partition-def.Default", 990)
    DomainDef.values.map(x => (x, AppConf.getConfIntOrElse("partition-def." + x, default))).toMap
  }

  private val ONE_MB = 1024 * 1024

  private def getSpkCtx(c: SparkConf): SparkContext = {
    val tmp = new SparkContext(c);
    /**
     * You can use the following suffix (case insensitive): k(kilo), m(mega), g(giga), t(tera), p(peta), e(exa) to specify the size (such as 128k, 512m, 1g, etc.), Or provide complete size in bytes (such as 134217728 for 128 MB).
     */

    //tmp.hadoopConfiguration.set("dfs.block.size", "33554432") // 32m
    tmp.hadoopConfiguration.set("dfs.block.size", (64 * ONE_MB).toString())
    tmp.hadoopConfiguration.set("parquet.block.size", (64 * ONE_MB).toString())
    tmp.hadoopConfiguration.set("mapreduce.input.fileinputformat.split.minsize", (64 * ONE_MB).toString())

    tmp
  }

  private def getSqlCtx(c: SparkContext): SQLContext = {
    val tmp = new SQLContext(c);

    tmp.setConf("dfs.block.size", (64 * ONE_MB).toString()) // 32m
    tmp.setConf("parquet.block.size", (64 * ONE_MB).toString()) // 32m
    tmp.setConf("mapreduce.input.fileinputformat.split.minsize", (64 * ONE_MB).toString())

    def setConf(confParm: String) {
      val paramVal = AppConf.getObj("spark-settings.sql." + confParm, null)
      if (paramVal != null) { tmp.setConf(confParm, paramVal.toString()) }
    }
    setConf("spark.sql.parquet.compression.codec")
    setConf("spark.sql.parquet.filterPushdown")
    setConf("spark.sql.inMemoryColumnarStorage.compressed")
    setConf("spark.sql.inMemoryColumnarStorage.batchSize")
    setConf("spark.sql.codegen")
    setConf("spark.sql.shuffle.partitions")

    tmp
  }

  val sparkContext: SparkContext = getSpkCtx(conf);
  val sqlContext = getSqlCtx(sparkContext)
  def getNElemsInOnePartition(dd: DomainDef.Value) = rddNElemsInOnePartition.getOrElse(dd, 400)

  def buildFilePath(fileName: String): String = basePath + "/" + inputPath + "/" + fileName 
  def buildParquetPath(fileName: String): String = buildFilePath(fileName) + ".parquet"
  def buildParquetPathWithKey(fileName: String, keyName: String, keyValue: String): String = buildFilePath(fileName) + ".parquet/" + keyName + "=" + keyValue
  def persist[T: ClassTag](recs: Seq[T], name: DomainDef.Value, nPart: Int) = sparkContext.parallelize(recs, nPart).saveAsObjectFile(buildFilePath(DomainDef.getTName(name)))

  def persistAsObjAndLoadAsRdd[A <: Product: TypeTag](rdd: RDD[A], name: DomainDef.Value): RDD[A] = {
    val path = buildFilePath(DomainDef.getTName(name))
    rdd.saveAsObjectFile(path)
    val nSlices = 4
    val lRdd = sparkContext.objectFile(path, nSlices).asInstanceOf[RDD[A]]
    lRdd
  }

  def persistAsObjWithTable[A <: Product: TypeTag](rdd: RDD[A], name: DomainDef.Value): DataFrame = {
    val lRdd = persistAsObjAndLoadAsRdd(rdd, name)
    val sRdd = this.sqlContext.createDataFrame(lRdd)
    sRdd.registerTempTable(DomainDef.getTName(name))
    sRdd
  }

  def persist(recs: DataFrame, name: DomainDef.Value, key: String = null): String = {
    val path = if (key != null) {
      buildParquetPath(DomainDef.getTName(name)) + "/" + key
    } else { buildParquetPath(DomainDef.getTName(name)) }
    recs.write.mode(SaveMode.Append).parquet(path)
    path
  }
  
  def persistWithTable[A <: Product: TypeTag](rdd: RDD[A], name: DomainDef.Value): DataFrame = {
    val scRdd = this.sqlContext.createDataFrame(rdd)
    val path = persist(scRdd, name)
    val parquetFile = sqlContext.parquetFile(path)
    parquetFile.registerTempTable(DomainDef.getTName(name))
    if (this.cacheLoadedTables) { parquetFile.persist() }
    availTables.put(name, path);
    parquetFile
  }

  def loadParquetAndRegisterTable(domain: Array[DomainDef.Value], isInputPath: Boolean = false) {
    domain.foreach(x => {
      val tName = DomainDef.getTName(x)
      val path = buildParquetPath(tName)
      val parquetFile = loadParquet(x)
      if (this.cacheLoadedTables) { parquetFile.persist() }
      parquetFile.registerTempTable(tName)
      availTables.put(x, path)
    })
  }

  def loadParquet(domain: DomainDef.Value): DataFrame = {
    val tName = DomainDef.getTName(domain)
    val path = buildParquetPath(tName)
    sqlContext.parquetFile(path)
  }

  def loadParquetWithKey(domain: DomainDef.Value, keyName: String, keyValue: String): DataFrame = {
    val tName = DomainDef.getTName(domain)
    val path = buildParquetPathWithKey(tName, keyName, keyValue)
    sqlContext.parquetFile(path)
  }

  def schemaRDD[A <: Product: TypeTag](rdd: RDD[A], name: DomainDef.Value): DataFrame = this.sqlContext.createDataFrame(rdd)

  def asRDD[A: ClassTag](req: Seq[A], dd: DomainDef.Value) = {
    val nSlices = 4
    val rdd = sparkContext.parallelize(req, nSlices)
    rdd;
  }

  def asRdd[A: ClassTag](req: Seq[A], nSlices: Int) = {
    sparkContext.parallelize(req, nSlices)
  }

  def isTable(tn: DomainDef.Value): Boolean = availTables.contains(tn)
}