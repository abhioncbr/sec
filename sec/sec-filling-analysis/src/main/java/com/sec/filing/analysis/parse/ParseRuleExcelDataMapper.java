package com.sec.filing.analysis.parse;

import java.lang.reflect.Method;
import java.util.LinkedList;

import com.sec.filing.analysis.input.ExcelDataMapper;

public class ParseRuleExcelDataMapper implements ExcelDataMapper<ParseRule> {
	public static final String METHOD_NAME = "setRule";
	
	public void setObjectData(LinkedList<Object> values, ParseRule targetObject){
		try {
			Method method = ParseRule.class.getMethod(METHOD_NAME, Double.class, String.class, Integer.class, Boolean.class, Boolean.class);
			
			Object tempObject = values.get(4);
			Boolean caseSensitive = tempObject != null ? (Boolean) tempObject : null;
			
			tempObject = values.get(3);
			Boolean trim = tempObject != null ? (Boolean) tempObject : null;
					
			tempObject = values.get(2);
			String rule = tempObject != null ? (String) tempObject : null;
			
			tempObject = values.get(1);
			Double weight = tempObject != null ? (Double) tempObject : null;
			
			tempObject = values.get(0);
			Integer group = tempObject != null ? ((Double) tempObject).intValue() : null;
			
			if(caseSensitive == null || trim == null || weight == null || rule == null || group == null)
				return;
			else
				method.invoke(targetObject, weight, rule, group, trim, caseSensitive);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
