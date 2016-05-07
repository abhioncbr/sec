package com.capiq.sec.filling.analysis.parse.jflex;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({"unused"})
%%

%public
%class JflexSecLexer
%type JflexToken
%line
%column

%{
	private String lexeme;
	
	private JflexToken previousItem6Token;
	private JflexToken previousItem7AToken;
	
	private Map<Integer, Integer> pageBreakLengthMap = new HashMap<Integer,Integer>();
	private Map<Integer, Integer> hyphenPageBreakLengthMap = new HashMap<Integer,Integer>();
	
  	private JflexToken token(JflexTokenType type, String value) {
    	return new JflexToken(type, yyline+1, yycolumn+1, value);
    }
    
    public Integer getPageBreakLength(){
    	return deduceMaxValueKey(pageBreakLengthMap);
    }

    public Integer getHyphenPageBreakLength(){
    	return deduceMaxValueKey(hyphenPageBreakLengthMap);
    }
    
    private Integer deduceMaxValueKey(Map<Integer, Integer> map){
    	int value = -1;
		int output = -1;
		for(Entry<Integer, Integer> entry: map.entrySet()){
			if(entry.getValue() > value)
				output = entry.getKey();
		}
		
		return output;
    }	
    
    private void addLengthToMap(Map<Integer, Integer> map, Integer length){
		if(map.containsKey(length)) {
			map.put(length, map.get(length) + 1);
		} else{
			map.put(length, 1);
		}
    }
    
    private void deducePageBreakLength(String content){
	  String[] splitLexeme = content.split("[\n]");
	  for(String str: splitLexeme){
			if(str.trim().matches("^[-][-]+$")){
				  Integer length = str.trim().length();
				  addLengthToMap(pageBreakLengthMap, length);
			}
	  }
    }
%}

Digit  				= [0-9]
Hyphen 				= ("--")
Lparen				= [(]
Rparen				= [)]

SingleQuote			= [\']
JunkCharacter		= ["\"\`\\\/\+\!\^"] | {SingleQuote}

Roman_Digit 		= (IX|IV|V?I{0,3})

Blankspace 		= [ \t\f]
LineTerminator  = [\r\n\r\n]
Whitespace 		= {LineTerminator} | {Blankspace}

Page_Number 	= {Lparen}?{Blankspace}*{Digit}+{Blankspace}*{Rparen}?

String			= ([^\\\' \n\f])
 

%caseless

Page_String		= ("PAGE")	 
Part_String 	= ("PART")
Index_String 	= ("INDEX")
And_String		= ("AND" | "&")
Item_String		= ("ITEM")?{Blankspace}*{Digit}{Digit}?("A"|"B")?(".")?(":")?
Toc_String 	 	= ("TABLE")?{Whitespace}*("OF")?{Whitespace}*("CONTENT")("S")? 

MDA_String_First_Half	= ("MANAG")("E")?("MENT"){JunkCharacter}("S"){Whitespace}*("DISCUSSION"){Whitespace}*{And_String}{Whitespace}*
				  		  ("ANALYSIS"){Whitespace}*("OF"){Whitespace}*("RESULTS")?{Whitespace}*("OF")?{Whitespace}*("FINANCIAL"){Whitespace}*	
MDA_String_Second_Half	= ("CONDITION"){Whitespace}*{And_String}{Whitespace}*("RESULT")("S")?{Whitespace}*("OF"){Whitespace}*
						  ("OPERATION")("S")?{Whitespace}*("(MD&A)")?([.]|{Whitespace})* 

CSO_String		= ("CONSOLIDATED"){Whitespace}*("STATEMENT")("S")?{Whitespace}*("OF"){Whitespace}*("OPERATION")("S")?([.]|{Whitespace})*
				             
FSSD_String		= ("FINANCIAL"){Whitespace}*("STATEMENT")("S")?{Whitespace}*{And_String}{Whitespace}*
				  ("SUPPLEMENT")("ARY" | "AL"){Whitespace}*("DATA")([.]|{Whitespace})*
				  
Form10K_String	= ("FORM"){Whitespace}*{Digit}{Digit}{Hyphen}?("K")

PageDivider_String  = 	{Hyphen}{Hyphen}+
PageBreak_String1	=	{Page_Number}{Whitespace}*{PageDivider_String}{Whitespace}*{Toc_String}
PageBreak_String2	=	{Page_Number}{Whitespace}*{PageDivider_String}
PageBreak_String3	=	{PageDivider_String}{Whitespace}*{Toc_String}

Item1_String 		=	{Item_String}{Blankspace}*("BUSINESS")[.]?
Item1A_String 		=	{Item_String}{Blankspace}*("RISK"){Blankspace}*("FACTOR")("S")?[.]?
Item1B_String 		=	{Item_String}{Blankspace}*("UNRESOLVED"){Blankspace}*("STAFF"){Blankspace}*("COMMENT")("S")?[.]?
Item2_String 		=	{Item_String}{Blankspace}*("PROPERTIES")[.]?
Item3_String 		=	{Item_String}{Blankspace}*("LEGAL"){Blankspace}*("PROCEEDING")("S")?[.]?
Item6_String 		=	{Item_String}{Blankspace}*("SELECTED"){Blankspace}*("FINANCIAL"){Blankspace}*("DATA")[.]?
Item7A_String		=   {Item_String}{Blankspace}*("QUANTITATIVE")?("QUALITATIVE")?{Blankspace}*("AND"){Blankspace}*("QUANTITATIVE")?("QUALITATIVE")?
						{Whitespace}*("DISCLOSURE")("S")?{Blankspace}*("ABOUT"){Blankspace}*("MARKET"){Blankspace}*("RISK")[.]?
Item9A_String 		=	{Item_String}{Blankspace}*("CONTROL")("S")?{Blankspace}*("AND"){Blankspace}*("PROCEDURE")("S")?[.]?
Item9B_String 		=	{Item_String}{Blankspace}*("OTHER"){Blankspace}*("INFORMATION")[.]?
Item11_String 		=	{Item_String}{Blankspace}*("EXECUTIVE"){Blankspace}*("COMPENSATION")[.]?       
%%
<YYINITIAL>{
	({Toc_String} | {Form10K_String}?{Whitespace}*{Index_String}){Whitespace}*{Page_String}?{Whitespace}*{Part_String}{Whitespace}*{Roman_Digit}
			{ lexeme = yytext(); return token(JflexTokenType.TOC, lexeme); }
}

<YYINITIAL>{
 	({Item_String}{Blankspace}*{FSSD_String} | {Item_String}{Blankspace}*{CSO_String}){Blankspace}*{Digit}+{Hyphen}?{Digit}*
 			{ lexeme = yytext(); JflexToken temp = token(JflexTokenType.TOC_FSSD_STRING_WITH_NUMBER, lexeme); return temp;}
}

<YYINITIAL>{
 	({Item_String}{Blankspace}*{MDA_String_First_Half}{MDA_String_Second_Half}){Blankspace}*{Digit}+{Hyphen}?{Digit}* 
 			{ lexeme = yytext(); JflexToken temp = token(JflexTokenType.TOC_MDA_STRING_WITH_NUMBER, lexeme); return temp;}
}

<YYINITIAL>{
 	{Item_String}{Whitespace}*{FSSD_String} | {Item_String}{Whitespace}*{CSO_String}
 			{ lexeme = yytext(); JflexToken temp = null;
 			  if(previousItem7AToken != null && yyline - previousItem7AToken.getLineNumber() < 2) {
 			  	 temp = token(JflexTokenType.TOC_FSSD_STRING, lexeme);
 			  } else temp = token(JflexTokenType.FSSD_STRING, lexeme); 
 			  return temp;	 }
}

<YYINITIAL>{
 	{Item_String}{Whitespace}*{MDA_String_First_Half}{MDA_String_Second_Half}
 			{ lexeme = yytext(); JflexToken temp = null;
 			  if(previousItem6Token != null && yyline - previousItem6Token.getLineNumber() < 2) {
 			  	 temp = token(JflexTokenType.TOC_MDA_STRING, lexeme);
 			  } else temp = token(JflexTokenType.MDA_STRING, lexeme); 
 			  return temp;	 }
}

/* {Toc_String} 				{ lexeme = yytext(); return token(JflexTokenType.TOC_STRING, lexeme); } */

{Item1_String} | {Item1A_String}| {Item1B_String} | {Item2_String} | {Item3_String} | {Item9A_String}| {Item9B_String}| {Item11_String} 
 								{ lexeme = yytext(); return token(JflexTokenType.TOC_ITEM_STRING, lexeme); }
{Item6_String} 					{ lexeme = yytext(); previousItem6Token = token(JflexTokenType.TOC_ITEM_STRING, lexeme); return previousItem6Token;}	
{Item7A_String} 				{ lexeme = yytext(); previousItem7AToken = token(JflexTokenType.TOC_ITEM_STRING, lexeme); return previousItem7AToken;}
^{PageBreak_String1}$ 			{ lexeme = yytext(); deducePageBreakLength(lexeme); return token(JflexTokenType.PAGE_BREAK, lexeme);}
^{PageBreak_String2}$ 			{ lexeme = yytext(); deducePageBreakLength(lexeme); return token(JflexTokenType.PAGE_BREAK, lexeme);}
^{PageBreak_String3}$			{ lexeme = yytext(); deducePageBreakLength(lexeme); return token(JflexTokenType.PAGE_BREAK, lexeme);}
^{PageDivider_String}$			{ lexeme = yytext(); 

							   	  Integer length = lexeme.trim().length();
							   	  addLengthToMap(hyphenPageBreakLengthMap, length);

								  return token(JflexTokenType.HYPHEN_PAGE_BREAK, lexeme);}
								  
{MDA_String_First_Half}			{ lexeme = yytext();	
								  if(previousItem6Token != null && yyline - previousItem6Token.getLineNumber() < 2) {
 			  	 						return token(JflexTokenType.MDA_STRING_FIRST_HALF, lexeme);
 			  					  }}			  


{String}*                       { /* Ignore */ }
{JunkCharacter}					{ /* Ignore */ }
{Whitespace} 					{ /* Ignore */ }

<<EOF>>     					{ return token(JflexTokenType.EOF,""); }