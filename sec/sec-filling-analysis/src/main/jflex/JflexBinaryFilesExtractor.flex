package com.sec.filing.analysis.parse.jflex;

@SuppressWarnings({"unused"})
%%

%public
%class JflexBinaryFilesExtractor
%type JflexToken
%line
%column

%{
	private boolean skip = false;
	private String lexeme = null;
	private StringBuilder content = new StringBuilder();
	
	public StringBuilder getContent(){
		return content;
	}
	
	private JflexToken token(JflexTokenType type, String value) {
    	return new JflexToken(type, yyline+1, yycolumn+1, value);
    }
%}


String			= [^ \n\f]
Blankspace 		= [ \t\f]
LineTerminator  = [\r\n\r\n]

%caseless

BinaryFile_End_String	 = ("</DOCUMENT>")
BinaryFile_Start_String  = ("<DOCUMENT>"){LineTerminator}*("<TYPE>")("GRAPHIC"|"ZIP"|"EXCEL"|"PDF")
Rest_String 	   		 = ({String}*{Blankspace}*)*
%%

{BinaryFile_End_String}	 	{ lexeme = yytext(); skip = false; }
{BinaryFile_Start_String}	{ lexeme = yytext(); skip = true; return token(JflexTokenType.BINARY_FILE, lexeme);}
{Rest_String} 				{ lexeme = yytext(); if(!skip) content.append(lexeme);}
{LineTerminator}			{ lexeme = yytext(); if(!skip) content.append(lexeme);}
<<EOF>>     				{ return token(JflexTokenType.EOF,""); }