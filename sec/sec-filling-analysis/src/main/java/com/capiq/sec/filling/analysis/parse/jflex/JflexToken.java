package com.capiq.sec.filling.analysis.parse.jflex;

public class JflexToken implements Comparable<JflexToken>{
	private Integer lineNumber;
	private Integer columnNumber;
	private JflexTokenType tokenType;
	private String tokenText;
	
	public JflexToken(JflexTokenType tokenType, int lineNumber, int columnNumber,  String tokenText){
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.tokenType = tokenType;
		this.tokenText = tokenText;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public JflexTokenType getTokenType() {
		return tokenType;
	}

	public String getTokenText() {
		return tokenText;
	}

	@Override
	public int compareTo(JflexToken obj) {
		return this.getColumnNumber().compareTo(obj.getColumnNumber());
	}
}
