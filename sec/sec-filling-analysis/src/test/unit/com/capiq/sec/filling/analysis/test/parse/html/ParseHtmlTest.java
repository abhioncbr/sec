package com.capiq.sec.filling.analysis.test.parse.html;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.parse.util.ParseHtmlElementsUtil;
import com.capiq.sec.filling.common.util.PropertiesUtil;
import com.capiq.sec.filling.common.util.SpringAppContext;

public class ParseHtmlTest {
	
	@Before
	public void setUp(){
		SpringAppContext.loadApplicationContext();
	}
	
	@Test
	public void processHtmlResponseTest() throws SymantecAnalysisGeneralException {
		boolean saveResponse = true;
		String url = "http://www.sec.gov/Archives/edgar/data/30554/0000930413-03-000634-index.html";
		String fileName = PropertiesUtil.getProperty("file.sec.form-10k.folder") + "/test.html";
		
		ParseHtmlElementsUtil.processHtmlResponse(url, saveResponse, fileName);
		
		File file = new File(fileName);
		assertTrue(file.exists());
	}
	
	@Test
	public void processHtmlResponseToTextTest() throws SymantecAnalysisGeneralException {
		boolean saveResponse = true;
		
		String baseUrl = PropertiesUtil.getProperty("sec.base.uri");
		String url = PropertiesUtil.getProperty("file.sec.form-10k.folder") + "0000030554-15-000004.txt";
		String fileName = PropertiesUtil.getProperty("file.sec.form-10k.folder") + "/text-parse.txt";
		
		ParseHtmlElementsUtil.processHtmlResponseToText(url, baseUrl, saveResponse, fileName);
		
		File file = new File(fileName);
		assertTrue(file.exists());
	}
	
	@Test
	public void loadHtmlResponseTest() throws SymantecAnalysisGeneralException {
		String baseUrl = PropertiesUtil.getProperty("sec.base.uri");
		String htmlfilePath = PropertiesUtil.getProperty("file.sec.form-10k.folder") + "/test.html";
		
		Document doc = ParseHtmlElementsUtil.loadHtmlResponse(htmlfilePath, baseUrl);
		
		assertNotNull(doc);
	}
	
	@Test
	public void extractHrefHtmlTagTest() throws SymantecAnalysisGeneralException{
		String baseUrl = PropertiesUtil.getProperty("sec.base.uri");
		String htmlfilePath = PropertiesUtil.getProperty("file.sec.form-10k.folder") + "/test.html";
		
		Map<String, String> hrefLinkMap = ParseHtmlElementsUtil.extractHrefHtmlTag(htmlfilePath, baseUrl);
		
		assertNotNull(hrefLinkMap);
		assertTrue(hrefLinkMap.size()>0);
	}
	
	@Test
	public void processHtmlResponseTest1() throws SymantecAnalysisGeneralException {
		boolean saveResponse = true;
		String url = "http://www.sec.gov/Archives/edgar/data/30554/000093041303000634/";
		String fileName = PropertiesUtil.getProperty("file.sec.form-10k.folder") + "/file-list.html";
		
		ParseHtmlElementsUtil.processHtmlResponse(url, saveResponse, fileName);
		
		File file = new File(fileName);
		assertTrue(file.exists());
	}
	
	@Test
	public void extractHrefTableTagTest() throws SymantecAnalysisGeneralException{
		String baseUrl = "http://www.sec.gov/Archives/edgar/data/30554/000093041303000634/";
		String htmlfilePath = PropertiesUtil.getProperty("file.sec.form-10k.folder") + "/file-list.html";
			
		Map<String, String> hrefLinkMap = ParseHtmlElementsUtil.extractHrefTableTag(htmlfilePath, baseUrl);
		
		assertNotNull(hrefLinkMap);
		assertTrue(hrefLinkMap.size()>0);
	}
}
