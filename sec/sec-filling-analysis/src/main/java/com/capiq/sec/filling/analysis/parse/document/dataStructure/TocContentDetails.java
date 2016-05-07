package com.capiq.sec.filling.analysis.parse.document.dataStructure;

public class TocContentDetails 	{
	private final String indexNumber;
	private final int pageNumber;
	private final String  indexHeading;
	
	TocContentDetails(int pageNumber, String indexNumber, String indexHeading){
		this.pageNumber = pageNumber;
		this.indexNumber = indexNumber;
		this.indexHeading = indexHeading;
	}
	
	public String getIndexNumber() {
		return indexNumber;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public String getIndexHeading() {
		return indexHeading;
	}

	@Override
	public String toString() {
		return "TocContentDetails [indexNumber=" + indexNumber
				+ ", pageNumber=" + pageNumber + ", indexHeading="
				+ indexHeading + "]";
	}
	
}
