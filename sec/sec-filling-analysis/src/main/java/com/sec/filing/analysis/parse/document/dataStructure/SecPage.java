package com.sec.filing.analysis.parse.document.dataStructure;

import java.util.LinkedList;

public class SecPage {
	private Integer endLineNumber;
	private Integer startLineNumber;
	private Integer parsePageNumber;
	private Integer documentPageNumber;
	private LinkedList<SecContent> pageContent;
	
	public SecPage(LinkedList<SecContent> pageContent, Integer startLineNumber, Integer endLineNumber, Integer parsePageNumber, Integer documentPageNumber){
		this.pageContent = pageContent; 
		this.endLineNumber = endLineNumber;
		this.startLineNumber = startLineNumber;
		this.parsePageNumber = parsePageNumber;
		this.documentPageNumber = documentPageNumber;
	}
	
	public LinkedList<SecContent> getPageContent(){
		return this.pageContent;
	}
	
	public Integer getParsePageNumber() {
		return parsePageNumber;
	}

	public Integer getDocumentPageNumber(){
		return this.documentPageNumber;
	}

	public Integer getEndLineNumber() {
		return endLineNumber;
	}

	public Integer getStartLineNumber() {
		return startLineNumber;
	}
}
