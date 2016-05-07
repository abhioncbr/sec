package com.capiq.sec.filling.analysis.parse;

import java.lang.reflect.Method;
import java.util.LinkedList;

import com.capiq.sec.filling.analysis.input.ExcelDataMapper;

public class FinancialDictionaryExcelDataMapper implements ExcelDataMapper<FinancialDictionary> {
	public static final String METHOD_NAME = "setfinancialDictionary";
	
	public void setObjectData(LinkedList<Object> values, FinancialDictionary targetObject){
		try {
			Method method = FinancialDictionary.class.getMethod(METHOD_NAME, String.class, String.class);
			String keyword = (String)values.get(0);
			String property = (String)values.get(1);
			method.invoke(targetObject, keyword, property);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
