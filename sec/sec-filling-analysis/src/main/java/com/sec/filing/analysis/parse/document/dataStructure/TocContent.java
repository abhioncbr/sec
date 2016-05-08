package com.sec.filing.analysis.parse.document.dataStructure;

import java.util.HashMap;
import java.util.Map;

public class TocContent {
	
	Map<String, TocContentDetails> indexContentMap = new HashMap<String, TocContentDetails>();
	
	public void addContent(int pageNumber, String indexNumber, String indexHeading){
		TocContentDetails temp = new TocContentDetails(pageNumber, indexNumber, indexHeading);
		indexContentMap.put(indexNumber, temp);
	}
	
	public Map<String, TocContentDetails> getContentDetailsMap(){
		return indexContentMap;
	}
	
	public TocContentDetails getIndexContent(int indexNumber) {
		if(indexContentMap.containsKey(indexNumber)) {
			return indexContentMap.get(indexNumber);
		} 
		return null;
	}
}
