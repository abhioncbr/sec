package com.sec.filing.analysis.launch.multithread;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import com.sec.filing.analysis.common.util.PropertiesUtil;
import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.http.SecDocHttpClient;
import com.sec.filing.analysis.parse.stat.IParseStat;

public class SecFileDownloader implements IProcessExecutor {
	private CountDownLatch batchCountDownLatch = null;
	private String[][] form10KListData = null;
	
	public SecFileDownloader(){
		
	}
	
	public void set(CountDownLatch batchCountDownLatch, String[][] form10KListData){
		this.batchCountDownLatch = batchCountDownLatch;
		this.form10KListData = form10KListData;
	}

	@Override
	public LinkedList<IParseStat> call() throws Exception {
		LinkedList<IParseStat> list = new LinkedList<IParseStat>();
		
		SecDocHttpClient secDocHttpClient = null;
		
		boolean isHdfsStorage = Boolean.parseBoolean(PropertiesUtil.getProperty("file.sec.hdfs.storage"));
		
		String baseFilePath = null;
		if(isHdfsStorage) baseFilePath = PropertiesUtil.getProperty("file.sec.hdfs.base.storage.url");
		else baseFilePath = PropertiesUtil.getProperty("file.sec.local.base.storage.url");
		
		String downloadPath =  PropertiesUtil.getProperty("file.sec.form-10k.download.folder");
		try{
			for(String[] str: form10KListData){
				String url = str[0];
				String fileName = str[1];
				String filePath = baseFilePath + downloadPath + "/" + fileName;
				
				boolean saveResponse = true;
				secDocHttpClient = new SecDocHttpClient();
				System.out.println("Starting : " + fileName + " with file path : " + filePath);
				
				try { IParseStat stat = secDocHttpClient.saveHttpResponse(fileName, filePath, url, saveResponse); list.add(stat); }
				catch(SymantecAnalysisGeneralException ex) { System.out.println("Exception while downloading for : " + fileName); System.out.println(ex);}
			} 
		} finally {
			batchCountDownLatch.countDown();
		}
		return list;
	}
}
