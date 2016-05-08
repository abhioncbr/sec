package com.sec.filing.analysis.parse.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;

public class ParseHtmlElementsUtil {
	
	private static final String UTF_8_CHARSET = "UTF-8"; 
	
	public static void processHtmlResponse(String url, Boolean saveResponse, String fileName) 
			throws SymantecAnalysisGeneralException{
		try {
			Response response = Jsoup.connect(url).execute();
			final Document doc = response.parse();
			
			if(saveResponse){
	        	final File f = new File(fileName);
	        	FileUtils.writeStringToFile(f, doc.outerHtml(), UTF_8_CHARSET);
	        }
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}
	
	public static void processHtmlResponseToText(String htmlfilePath, String baseUrl, Boolean saveResponse, String fileName) 
			throws SymantecAnalysisGeneralException{
		try {
			final Document doc = loadHtmlResponse(htmlfilePath, baseUrl);
			
			if(saveResponse){
	        	final File f = new File(fileName);
	        	FileUtils.writeStringToFile(f, doc.text(), UTF_8_CHARSET);
	        }
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}
	
	
	public static Document loadHtmlResponse(String htmlfilePath, String baseUrl) throws SymantecAnalysisGeneralException{
		try {
			File input = new File(htmlfilePath);
			Document doc = Jsoup.parse(input, UTF_8_CHARSET, baseUrl);
			return doc;
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}
	
	public static Map<String, String> extractHrefTableTag(String htmlfilePath, String baseUrl) throws SymantecAnalysisGeneralException{
		Map<String, String> linkTagMap = new LinkedHashMap<String, String>();
		
		Elements links = extractHtmlTag("td a", htmlfilePath, baseUrl);
		for (Element link : links) {
			linkTagMap.put(link.text(), link.attr("abs:href"));
        }
		
		return linkTagMap;
	}
	
	public static Map<String, String> extractHrefHtmlTag(String htmlfilePath, String baseUrl) 
			throws SymantecAnalysisGeneralException{
		Map<String, String> linkTagMap = new LinkedHashMap<String, String>();
		
		Elements links = extractHtmlTag("a[href]", htmlfilePath, baseUrl);
		for (Element link : links) {
			linkTagMap.put(link.text(), link.attr("abs:href"));
        }
		
		return linkTagMap;
	}
	
	public static Elements extractHtmlTag(String tag, String htmlfilePath, String baseUrl) 
			throws SymantecAnalysisGeneralException{
		Document doc = loadHtmlResponse(htmlfilePath, baseUrl);
		Elements tags = doc.select(tag);
		return tags;
	}
	
}
