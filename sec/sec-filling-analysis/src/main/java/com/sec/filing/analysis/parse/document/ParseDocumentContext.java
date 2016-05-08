package com.sec.filing.analysis.parse.document;

import java.util.LinkedHashMap;

public class ParseDocumentContext {
	private LinkedHashMap<ParseDocumentContextEnum, Object> contextValues = new LinkedHashMap<ParseDocumentContextEnum, Object>();
	
	@SuppressWarnings("unchecked")
	public <E> E getContextValue(ParseDocumentContextEnum valueEnum, Class<E> type){
		if(contextValues.containsKey(valueEnum)){
				return (E) contextValues.get(valueEnum);
		}
		return null;
	}
		
	public void setContextValue(ParseDocumentContextEnum valueEnum, Object value){
		contextValues.put(valueEnum, value);
	}
	
}


