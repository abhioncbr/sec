package com.sec.filling.analysis.spark.job

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import com.sec.filing.analysis.launch.multithread.SparkJobExecutor
import com.sec.filing.analysis.common.util.SpringAppContext
import scala.reflect.ClassTag
import com.sec.filing.analysis.parse.stat.MdaExtractionStatData
import com.sec.filling.analysis.spark.job.domain.ExtractionStat
import scala.collection.mutable.MutableList
import org.apache.spark.sql.SaveMode
import com.sec.filling.analysis.spark.job.domain.DomainDef
import com.sec.filing.analysis.input.FileInput

object SecExtractorSparkJob {
  
  def main(args: Array[String]) {
    SpringAppContext.loadApplicationContext();
   
    val jobContext = JobContext.create("ExtractStat")
    
    val inputFile = SpringAppContext.getResource("classpath:Form-10K-Data-Min.txt").getURI().getPath()
    val inputData = FileInput.readFile(inputFile)
    val inputDataSeq = scala.collection.JavaConversions.asScalaBuffer(inputData)
    val inputRDD = jobContext.asRdd(inputDataSeq, 4)
    
    val stats: MutableList[MdaExtractionStatData]  =  evaluate(inputRDD)
    save(stats , jobContext)
    
    JobContext.stop(jobContext)
  }
  
  def evaluate[MdaExtractionStatData:ClassTag](rdd: RDD[String]): MutableList[com.sec.filing.analysis.parse.stat.MdaExtractionStatData] = {
    val list = MutableList[com.sec.filing.analysis.parse.stat.MdaExtractionStatData]();
    rdd.collect().foreach(a => { val b = execute(a); list += b })
    return list
  } 
  
  def execute(a: String): MdaExtractionStatData = {
    val executor = new SparkJobExecutor();
    val temp = a.split(",")
    executor.runJob(temp(1))
  }
  
  def save(ar: MutableList[MdaExtractionStatData], jobContext: JobContext) {
    val sqlContext= jobContext.sqlContext
    val inputRdd = jobContext.asRDD(ar, DomainDef.ExtractStat)
    
    import sqlContext.implicits._
    val dataDF = inputRdd.map(p => ExtractionStat(p.getFileName(), p.getRawFileSize(), p.getCleanFileSize(), p.getBinaryIgnoredFileSize()
                                      , p.getItem7tocDeduced(), p.getItem8tocDeduced(), p.getMdaExtracted(), p.getExtractionRule(), p.getLinesInExtractedData())).toDF();
    
    //dataDF.write.mode(SaveMode.Append).parquet("parseStat.parquet") // write to parquet
    jobContext.persist(dataDF, DomainDef.ExtractStat)
    
    val newDataDF = sqlContext.read.parquet(jobContext.buildParquetPath("ExtractStat"))  // read back parquet to DF
    newDataDF.show()   
  }
 }