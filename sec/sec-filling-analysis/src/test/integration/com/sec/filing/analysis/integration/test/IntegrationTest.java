package com.sec.filing.analysis.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sec.filing.analysis.common.util.PropertiesUtil;
import com.sec.filing.analysis.common.util.SpringAppContext;
import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.http.SecDocHttpClient;
import com.sec.filing.analysis.input.FileInput;
import com.sec.filing.analysis.launch.multithread.IProcessExecutor;
import com.sec.filing.analysis.launch.multithread.MultithreadExecutor;
import com.sec.filing.analysis.launch.multithread.SecFileDownloader;
import com.sec.filing.analysis.launch.multithread.SecFileParser;
import com.sec.filing.analysis.parse.document.ParseDocument;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.document.ParseDocumentContextEnum;
import com.sec.filing.analysis.parse.document.dataStructure.SecDocument;
import com.sec.filing.analysis.regex.SecFilingRegeRule;

public class IntegrationTest {
	private static final String FORM_10K_INPUT_TEST_FILE = "/Form-10K-Data-Min.txt";
	private List<String> form10KList = null;
	private String[][] form10KListData = null;
	
	@Before
	public void setUp() throws SymantecAnalysisGeneralException{
		SpringAppContext.loadApplicationContext();
		
		String inputFile = PropertiesUtil.getProperty("file.excel.input.folder") + FORM_10K_INPUT_TEST_FILE;
		form10KList = FileInput.readFile(inputFile);
		
		form10KListData = new String[form10KList.size()][];
		int count = 0;
		for(String line: form10KList){
			form10KListData[count++] = line.split(",");
		}	
	}
	
	//@Test
	public void processForm10KInputs() throws SymantecAnalysisGeneralException {
		for(String[] str: form10KListData){
			String actualFileName = SecFilingRegeRule.getSecForm10KFileName(str[0]);
			String expectedFileName = str[1];
			assertNotNull(actualFileName);
			assertEquals(expectedFileName, actualFileName);
		}
	}
	
	//@Test
	public void processForm10KInputFiles() throws SymantecAnalysisGeneralException {
		boolean isHdfsStorage = Boolean.parseBoolean(PropertiesUtil.getProperty("file.sec.hdfs.storage"));
		
		String baseFilePath = null;
		if(isHdfsStorage) baseFilePath = PropertiesUtil.getProperty("file.sec.hdfs.base.storage.url");
		else baseFilePath = PropertiesUtil.getProperty("file.sec.local.base.storage.url");
		
		String downloadPath =  PropertiesUtil.getProperty("file.sec.form-10k.download.folder");

		for(String[] str: form10KListData){
			String url = str[0];
			String fileName = str[1];
			String filePath = baseFilePath + downloadPath + "/" + fileName;
			
			System.out.println("Starting : " + fileName + " with file path : " + filePath);
			
			boolean saveResponse = true;
			SecDocHttpClient secDocHttpClient =  new SecDocHttpClient();
			secDocHttpClient.saveHttpResponse(fileName, filePath, url, saveResponse);
			
			File file = new File(fileName);
			assertTrue(file.exists());
		}
	}
	
	@Test
	public void processForm10KInputFilesMultiThreaded() throws Exception {
			IProcessExecutor process = new SecFileDownloader();
			MultithreadExecutor.execute(form10KListData, process.getClass());
	}
	
	//@Test
	public void parseForm10KDocumentMultiThreaded() throws Exception {
			IProcessExecutor process = new SecFileParser();
			MultithreadExecutor.execute(form10KListData, process.getClass());
	}
	
	//@Test
	public void parseForm10KDocument() throws SymantecAnalysisGeneralException{
		ParseDocument documentParser = new ParseDocument();
		for(String[] str: form10KListData){
			String fileName = str[1];
			ParseDocumentContext context = new ParseDocumentContext();
			
			System.out.println("STARTING : " + str[1]);
			
			documentParser.parse(fileName, context);
			SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
			
			//assertNotNull(document);
			//assertTrue(document.getPageContent().size() > 0);
			int count = document != null ? document.getPageContent().size() : 0;
			System.out.println(str[1] + "," + count);
			System.out.println();
			System.out.println();
		}
	}
	
	//@Test
	public void fileRename(){
		for(String[] str: form10KListData){
			
			File oldName = new File("/home/abhishek/Desktop/sec/S&P/columbia_sec_clean_md&a_2013/" + str[0]);
			File newName = new File("/home/abhishek/Desktop/sec/S&P/columbia_sec_clean_md&a_2013/" + str[0]+".txt");
		    
			if(oldName.renameTo(newName)) {
		         System.out.println("renamed");
		      } else {
		         System.out.println("Error");
		      }
		}
	}
}
