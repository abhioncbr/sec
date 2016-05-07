package com.capiq.sec.filling.analysis.launch.multithread;

import com.capiq.sec.filling.analysis.parse.document.ParseDocument;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.stat.MdaExtractionStatData;

public class SparkJobExecutor {
	
	public MdaExtractionStatData runJob(String fileName) throws Exception{
		ParseDocument parseDocument = new ParseDocument();
		return (MdaExtractionStatData) parseDocument.parse(fileName, new ParseDocumentContext());
	}
}
