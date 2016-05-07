package com.capiq.sec.filling.analysis.parse.util;

import java.util.regex.Matcher;

import com.capiq.sec.filling.analysis.regex.SecFilingRegeRule;

public class ParseCommonUtil {
	
	private static final int[] VALUES = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
	private static final String[] DIGRAMS = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"}; 
	
	public static boolean isNumeral(String s){
		 if (s.isEmpty())
			 return false;
		 else if (SecFilingRegeRule.matchRegex(s, "[0-9]+", false, true))
		        return true;
		 else 
			 return false;
	}
	
	public static boolean isNumeralwithFixes(String s){
		 if (s.isEmpty())
			 return false;
		 else if (SecFilingRegeRule.matchRegex(s, "([(]?[-]?[ ]?)([0-9]+)([)]?[ ]?[-]?)", false, true))
		        return true;
		 else 
			 return false;
	}
	
	public static boolean isPageNumberLine(String lineString){
		if(isNumeral(lineString))
			return true;
		else if(isRommanNumeral(lineString))
			return true;
		else if(isNumeralwithFixes(lineString))
			return true;
		else return false;
	}
	
	public static boolean isRommanNumeral(String s){
		 if (s.isEmpty())
			 return false;
		 else if (SecFilingRegeRule.matchRegex(s, "^(C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$", false, true))
		        return true;
		 else 
			 return false;
	}
	
	public static int parseNumeralwithFixes(String s){
		 Matcher matcher = SecFilingRegeRule.getRegexMatcher(s, "[ ]*([-]?[ ]*[(]?)([0-9]+)([)]?[ ]*[-]?[ ]*)", false, true);
		 matcher.matches();
		 int result = Integer.parseInt(matcher.group(2));
		 return result;
	}

	public static int toIntegerRegex(String s) {
	    //creates a regex with each roman numeral pattern -> M|CM|D|......
	    //You'll need it to check if you have several occurrences of a pattern
	    //-> CC or III...
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < DIGRAMS.length; i++) {
	        builder.append(DIGRAMS[i]);

	        if (i < DIGRAMS.length - 1)
	            builder.append("|");
	    }     

	    Matcher matcher = SecFilingRegeRule.getRegexMatcher(s, builder.toString(), false, true);
	    //Pattern.compile(builder.toString()).matcher(s);    
	    int result = 0;

	    while (matcher.find()) {
	        for (int j = 0; j < DIGRAMS.length; j++) {
	            if (DIGRAMS[j].equals(matcher.group()))
	                result += VALUES[j];
	        }
	    }
	    return result;
	}

}
