package com.capiq.sec.filling.analysis.parse.document.dataStructure;

import java.util.LinkedList;

public class SecDocument {
	
	private SecPage filingHeaderPage;
	private SecPage filingCoverPage;
	private SecPage filingToCPage;
	private LinkedList<SecPage> filingDocumentPages;
	
	//Default constructor
	public SecDocument(){
	}
	
	//constructor with classification of only pages
	public SecDocument(LinkedList<SecPage> filingDocumentPages){
		this.filingDocumentPages = filingDocumentPages; 
	}
	
	//constructor with classification of only header, index & pages
	public SecDocument(SecPage filingHeaderPage, SecPage filingCoverPage, SecPage filingToCPage, LinkedList<SecPage> filingDocumentPages) {
		this.filingHeaderPage = filingHeaderPage;
		this.filingCoverPage = filingCoverPage;
		this.filingToCPage = filingToCPage;
		this.filingDocumentPages = filingDocumentPages;
	}

	public LinkedList<SecPage> getPageContent(){
		return this.filingDocumentPages;
	}

	public SecPage getFilingHeaderPage() {
		return filingHeaderPage;
	}

	public SecPage getFilingCoverPage() {
		return filingCoverPage;
	}

	public SecPage getFilingToCPage() {
		return filingToCPage;
	}

	public LinkedList<SecPage> getFilingDocumentPages() {
		return filingDocumentPages;
	}

	public void setFilingDocumentPages(LinkedList<SecPage> filingDocumentPages) {
		this.filingDocumentPages = filingDocumentPages;
	}

	public void setFilingHeaderPage(SecPage filingHeaderPage) {
		this.filingHeaderPage = filingHeaderPage;
	}

	public void setFilingCoverPage(SecPage filingCoverPage) {
		this.filingCoverPage = filingCoverPage;
	}

	public void setFilingToCPage(SecPage filingToCPage) {
		this.filingToCPage = filingToCPage;
	}
	
	public SecPage getSecPageBasedOnParsePageNumber(Integer parsePageNumber){
		for(SecPage page: filingDocumentPages){
			if(page.getParsePageNumber() == parsePageNumber)
				return page;
		}
		return null;
	}

	public SecPage getSecPageBasedOnDocumentPageNumber(Integer documentPageNumber){
		for(SecPage page: filingDocumentPages){
			if(page.getDocumentPageNumber() == documentPageNumber)
				return page;
		}
		return null;
	}

	
	public SecPage getSecPageContainsLineNumber(Integer lineNumber){
		for(SecPage page: filingDocumentPages){
			if(page.getStartLineNumber() <= lineNumber && page.getEndLineNumber() >= lineNumber)
				return page;
		}
		return null;
	}
}
