package com.capiq.sec.filling.analysis.parse.document.dataStructure;

import java.util.LinkedList;

public class SecContent {
	private LinkedList<LineContent> pageContent = new LinkedList<LineContent>();
	
	public SecContent(){
		this.pageContent = new LinkedList<LineContent>();
	}
	
	public void setContent(String content, Integer pageLineNumber, Integer documentLineNumber){
		LineContent lineContent = new LineContent(content, pageLineNumber, documentLineNumber);
		this.pageContent.add(lineContent); 
	}
	
	public LinkedList<LineContent> getPageContent(){
		return this.pageContent;
	}
}
