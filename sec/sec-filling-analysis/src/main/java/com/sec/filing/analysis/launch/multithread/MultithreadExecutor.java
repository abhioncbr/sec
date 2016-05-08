package com.sec.filing.analysis.launch.multithread;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.batch.item.file.FlatFileItemWriter;

import com.sec.filing.analysis.common.util.SpringAppContext;
import com.sec.filing.analysis.input.FileInput;
import com.sec.filing.analysis.output.FileOutput;
import com.sec.filing.analysis.parse.stat.IParseStat;

public class MultithreadExecutor {
	private static final String EXTRACT = "EXTRACT";
	private static final String DOWNLOAD = "DOWNLOAD";
	
	private static final Integer BATCH_SIZE = 50;
	
	public static void main(String args[]) throws Exception{
		SpringAppContext.loadApplicationContext();
		
		String processName = null;
		String filePath = null;
		
		IProcessExecutor process = null;
		if(args.length != 2) {
			System.err.println("Require two arguments - ");
			System.err.println("		 1) Process Name ( 'Download', 'Extract')");
			System.err.println(" 		 2) Valid file path contain sec file information.");
			System.exit(0);
		} else if(args.length == 2) {
			processName = args[0].toUpperCase();
			if(EXTRACT.equals(processName)) process = new SecFileParser();
			else if(DOWNLOAD.equals(processName)) process = new SecFileDownloader();
			else { System.err.println("Not a valid process name. Process Name should be either 'Download' or 'Extract'."); System.exit(0);}
			
			filePath = args[1];
			File file = new File(filePath);
			if(!file.exists()) { System.err.println("File not found at provided path. Please check carefully."); System.exit(0); }
		}
		
		List<String> form10KList = FileInput.readFile(filePath);
		String[][] universeData = new String[form10KList.size()][];
		int count = 0;
		for(String line: form10KList){
			universeData[count++] = line.split(",");
		}
		
		LinkedList<IParseStat> universeStatList = execute(universeData, process.getClass());
		
		if(process.getClass().equals(SecFileParser.class)) writeExtractionCsvDataFiles(universeStatList);
        else writeDownloadCsvDataFiles(universeStatList);
	}
	
	public static LinkedList<IParseStat> execute(String[][] universeData, Class<? extends IProcessExecutor> class1) throws Exception{
		Integer batchCount = (universeData.length / BATCH_SIZE) + 1;

        CountDownLatch batchCountDownLatch = new CountDownLatch(batchCount);
        ExecutorService executor = Executors.newFixedThreadPool(batchCount);
        List<Future<LinkedList<IParseStat>>> futureList = new LinkedList<Future<LinkedList<IParseStat>>>();

        LinkedList<IParseStat>  universeStatList = new LinkedList<IParseStat> ();
        
        for (int i = 0; i < batchCount; i++) {
            Integer startIndex = i * BATCH_SIZE;
            Integer endIndex = universeData.length > (startIndex + BATCH_SIZE) ? startIndex + BATCH_SIZE : universeData.length;
            
            String[][] batchUniverseData = Arrays.copyOfRange(universeData, startIndex, endIndex);
            
            IProcessExecutor processExecutor = class1.newInstance();
            
            processExecutor.set(batchCountDownLatch, batchUniverseData);
            futureList.add(executor.submit(processExecutor));
        }

        executor.shutdown();
        batchCountDownLatch.await();
        
        for(Future<LinkedList<IParseStat>> future: futureList){
            List<IParseStat> batchProcessedData = future.get();
            universeStatList.addAll(batchProcessedData);
        }
        
       return universeStatList; 
	}
	
	private static void writeExtractionCsvDataFiles(LinkedList<IParseStat>  universeStatList) {
			FlatFileItemWriter<IParseStat> parseStatFileWriter = SpringAppContext.getBean("parseStatFileWriter");
			
			// FileOut
			FileOutput fileOutput = new FileOutput();
			fileOutput.writeParseStatFile(universeStatList, parseStatFileWriter);
	}
	
	private static void writeDownloadCsvDataFiles(LinkedList<IParseStat>  universeStatList) {
		FlatFileItemWriter<IParseStat> parseStatFileWriter = SpringAppContext.getBean("downloadStatFileWriter");
		
		// FileOut
		FileOutput fileOutput = new FileOutput();
		fileOutput.writeParseStatFile(universeStatList, parseStatFileWriter);
}
}
