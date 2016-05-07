#!/bin/bash

 
SEC_HOME=/home/abhishek/Desktop/sec/app
SEC_LIB=$SEC_HOME/lib
SEC_LOG=$SEC_HOME/log

HADOOP_HOME=/home/abhishek/BigData/hadoop-2.6.4
HADOOP_LIB=${HADOOP_HOME}/lib

#Set log file for the process execution.
LOG_FILE=$SEC_LOG"/sec.log"
if [ ! -f $LOG_FILE ] 
then 
	cat $LOG_FILE
else 
	rm $LOG_FILE 
fi

Classpath="'"$HADOOP_LIB"/*;"$SEC_LIB"/*'"

echo Starting to run sec 10k files download.
java -Xms300m -Xmx500m -classpath  $Classpath -jar sec-filling-analysis-0.0.1-SNAPSHOT.jar Download input/Form-10K-Data-Min.txt>$LOG_FILE
