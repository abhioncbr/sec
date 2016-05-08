package com.sec.filing.analysis.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class SecFilingRegeRule {
	private static final String INDEX_SUNSTRING = "-index.html";
	private static final String FORM_10K_REGEX = "((http|https)([:]/{2}w{3}[a-zA-Z0-9./\\-]*)(.txt))";
	private static final String FORM_10K_FILE_NAME_REGEX = "((http|https)([:]/{2}w{3}[a-zA-Z0-9./]*/)([0-9\\-]*)(.txt))";
	
	public static String getSecForm10FilingUrl(String rawUrl){
		StringBuilder urlBuilder = null;
		
		Pattern pattern = Pattern.compile(FORM_10K_REGEX);
		Matcher matcher = pattern.matcher(rawUrl);
		if(matcher.matches()){
			urlBuilder = new StringBuilder();
			
			urlBuilder.append(matcher.group(2));
			urlBuilder.append(matcher.group(3));
			urlBuilder.append(INDEX_SUNSTRING);
		}
		
		return urlBuilder.toString();
	}
	
	public static String getSecForm10KFileName(String rawUrl){
		StringBuilder fileName = null;
		
		Pattern pattern = Pattern.compile(FORM_10K_FILE_NAME_REGEX);
		Matcher matcher = pattern.matcher(rawUrl);
		if(matcher.matches()){
			fileName = new StringBuilder();
			
			fileName.append(matcher.group(4));
			fileName.append(matcher.group(5));
		}
		
		return fileName.toString();
	}
	
	public static Matcher getRegexMatcher(String string, String regexRule, boolean trim, boolean caseInsensitive){
		Pattern pattern;
		if(caseInsensitive)
			pattern = Pattern.compile(regexRule, Pattern.CASE_INSENSITIVE);
		else
			pattern = Pattern.compile(regexRule);
		
		String temp = trim ? string.trim() : string;
		if(StringUtils.isEmpty(temp)) return null;
		
		Matcher matcher = pattern.matcher(temp);
		
		return matcher;
		
	}
	
	public static boolean matchRegex(String string, String regexRule, boolean trim, boolean caseInsensitive){
		Matcher matcher = getRegexMatcher(string, regexRule, trim, caseInsensitive);
		if(matcher == null ) 
			return false;
		
		return matcher.matches();
	}
}
