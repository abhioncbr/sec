package com.sec.filing.analysis.parse;

import java.util.LinkedList;
import java.util.List;

public class FinancialDictionary {
	
	private final List<FinancailKeyword> finacialDictionaryList = new LinkedList<FinancailKeyword>();
	
	public List<FinancailKeyword> getFinancialDictionaryList() {
		return finacialDictionaryList;
	}

	public void setfinancialDictionary(String keyword, String property){
		FinancailKeyword temp = new FinancailKeyword(keyword, property);
		this.finacialDictionaryList.add(temp);
	}
	
	public class FinancailKeyword {
		private String word;
		private String property;
		
		public FinancailKeyword(String word, String property){
			this.word = word;
			this.property = property;
		}

		public String getWord() {
			return word;
		}

		public String getProperty() {
			return property;
		}
	}
}
