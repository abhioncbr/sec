package com.sec.filling.analysis.spark.job.domain

sealed case class ExtractionStat(fileName : String, 
    rawFileSize : String, 
    cleanFileSize : String, 
    binaryIgnoredFileSize : String, 
    item7tocDeduced: Boolean, 
    item8tocDeduced : Boolean, 
    mdaExtracted: Boolean, 
    extractionRule : String, 
    linesInExtractedData : Integer)