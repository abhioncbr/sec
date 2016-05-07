package com.capiq.sec.filling.analysis.launch.multithread;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocument;
import com.capiq.sec.filling.analysis.parse.stat.IParseStat;

public class SecFileParser implements IProcessExecutor {
	private String[][] form10KListData = null;
	private CountDownLatch batchCountDownLatch = null;

	public void set(CountDownLatch batchCountDownLatch, String[][] form10KListData){
		this.batchCountDownLatch = batchCountDownLatch;
		this.form10KListData = form10KListData;
	}

	@Override
	public LinkedList<IParseStat> call() throws Exception {
		LinkedList<IParseStat> list = new LinkedList<IParseStat>();
		
		ParseDocumentContext context = null;
		ParseDocument documentParser = null;
		
		try{
			for(String[] str: form10KListData){
				String fileName = str[1];
				
				System.out.println("Starting Parsing Process : " + fileName);
				
				context = new ParseDocumentContext();
				documentParser = new ParseDocument();
				
				try { IParseStat stat = documentParser.parse(fileName, context); list.add(stat); }
				catch(SymantecAnalysisGeneralException ex) { System.out.println("Exception while extraction for : " + fileName); System.out.println(ex);}
				
				System.out.println("End Parsing Process for: " + fileName);
			}
		} finally {
			batchCountDownLatch.countDown();
		}
		return list;
	}
}
