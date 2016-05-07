package com.capiq.sec.filling.analysis.parse;

import com.capiq.sec.filling.analysis.input.ExcelReader;
import com.capiq.sec.filling.analysis.input.FileInput;
import com.capiq.sec.filling.common.util.SpringAppContext;

public class ParseRuleAccessor {
	private static final ExcelReader<ParseRule> PARSE_DICTIONARY = SpringAppContext.getBean("parseRuleReader");
	private static ParseRule CONTENT_TABLE_RULE;
	private static ParseRule PAGE_BREAK_RULE;
	
	static {
		try{
			CONTENT_TABLE_RULE = FileInput.readExcelFile(1, PARSE_DICTIONARY);
			PAGE_BREAK_RULE = FileInput.readExcelFile(0, PARSE_DICTIONARY);
		}catch(Exception ex){
			System.out.println("Exception while loading parse rules from excel file.");
		}
	}
	
	public static ParseRule getPageBreakRule(){
		return PAGE_BREAK_RULE;
	}
	
	public static ParseRule getContentTableRule(){
		return CONTENT_TABLE_RULE;
	}
	
}
