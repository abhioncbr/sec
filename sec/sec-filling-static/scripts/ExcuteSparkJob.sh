#!/bin/bash


SEC_HOME=/home/abhishek/Desktop/sec/try
SEC_LIB=$SEC_HOME/lib
SEC_LOG=$SEC_HOME/log

#Set log file for the process execution.
LOG_FILE=$SEC_LOG"/sec-spark.log"
if [ ! -f $LOG_FILE ] 
then 
	cat $LOG_FILE
else 
	rm $LOG_FILE 
fi



echo Starting to run spark job.

sh /home/abhishek/BigData/spark-1.6.1-bin-hadoop2.6/bin/spark-submit \
	--master yarn-client \
	--class com.sec.filling.analysis.spark.job.SecExtractorSparkJob  \
	--driver-memory "500m" \
    --driver-java-options "-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps" \
    --executor-cores 1 \
    --queue "default" \
    --conf spark.io.compression.codec=lz4 \
    --conf spark.shuffle.compress=true \
    --conf spark.hadoop.validateOutputSpecs=false \
    --conf spark.driver.maxResultSize=0 \
    --conf "spark.executor.extraJavaOptions=-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+OptimizeStringConcat" \
    --jars /home/abhishek/BigData/spark-1.6.1-bin-hadoop2.6/lib/spark-assembly-1.6.1-hadoop2.6.0.jar,/home/abhishek/BigData/spark-1.6.1-bin-hadoop2.6/lib/spark-1.6.1-yarn-shuffle.jar,/home/abhishek/Desktop/sec/try/sec-filling-analysis-0.0.1-SNAPSHOT.jar,/home/abhishek/Desktop/sec/try/sec-filling-static-0.0.1-SNAPSHOT.jar\
	sec-filing-spark-0.0.1-SNAPSHOT.jar>$LOG_FILE
