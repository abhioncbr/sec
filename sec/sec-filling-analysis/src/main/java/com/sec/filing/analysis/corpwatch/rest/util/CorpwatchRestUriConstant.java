package com.sec.filing.analysis.corpwatch.rest.util;


public class CorpwatchRestUriConstant {
	
	private static final String CORPWATCH_SERVER_URL = "http://api.corpwatch.org/";
	private static final String CORPWATCH_KEY = "key=11c415c32ccdd0cd89bd2b8c67a76658";
	
	//cik uri
	public static final String CIK_URI = CORPWATCH_SERVER_URL + "companies?cik={cik}&" + CORPWATCH_KEY;
    public static final String FILING_URI = CORPWATCH_SERVER_URL + "{year}/companies/{cwId}/filings?" + CORPWATCH_KEY;
    
 }
