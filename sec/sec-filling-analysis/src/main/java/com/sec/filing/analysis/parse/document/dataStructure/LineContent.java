package com.sec.filing.analysis.parse.document.dataStructure;

import java.util.HashMap;
import java.util.Map;

public class LineContent{
	private final StringBuilder content;
	private final Integer pageLineNumber;
	private final Integer documentLineNumber;
	
	private Map<Object, Object> contentProperty = new HashMap<Object, Object>(); 
	
	LineContent(String content, Integer pageLineNumber, Integer documentLineNumber){
		this.pageLineNumber = pageLineNumber;
		this.documentLineNumber = documentLineNumber;
		this.content = new StringBuilder(content);
	}
	
	public StringBuilder getContent(){
		return this.content;
	}
	
	public Integer getPageLineNumber() {
		return pageLineNumber;
	}

	public Integer getDocumentLineNumber() {
		return documentLineNumber;
	}

	public String getStringContent(){
		return getContent().toString();
	}
	
	public void setContentProperty(Object key, Object value){
		this.contentProperty.put(key, value);
	}
	
	public Object getContentProperty(Object key){
		return this.contentProperty.get(key);
	}
	
	public Map<Object, Object> getContentPropertyMap(){
		return this.contentProperty;
	}
}

