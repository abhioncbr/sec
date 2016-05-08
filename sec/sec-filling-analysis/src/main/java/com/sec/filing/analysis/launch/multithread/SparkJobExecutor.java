package com.sec.filing.analysis.launch.multithread;

import com.sec.filing.analysis.parse.document.ParseDocument;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.stat.MdaExtractionStatData;

public class SparkJobExecutor {
	
	public MdaExtractionStatData runJob(String fileName) throws Exception{
		ParseDocument parseDocument = new ParseDocument();
		return (MdaExtractionStatData) parseDocument.parse(fileName, new ParseDocumentContext());
	}
}
