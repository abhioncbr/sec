package com.capiq.sec.filling.analysis.test.regex;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.capiq.sec.filling.analysis.regex.SecFilingRegeRule;
import com.capiq.sec.filling.common.util.SpringAppContext;

public class SecFilingRegexRuleTest {
	
	@Before
	public void setUp(){
		SpringAppContext.loadApplicationContext();
	}
	
	@Test
	public void getSecForm10FilingUrlTest() {
		String testRawUrl = "http://www.sec.gov/Archives/edgar/data/30554/0000930413-03-000634.txt";
		String actualUrl = SecFilingRegeRule.getSecForm10FilingUrl(testRawUrl);
		String expectedUrl = "http://www.sec.gov/Archives/edgar/data/30554/0000930413-03-000634-index.html";
		assertResult(actualUrl, expectedUrl);
		
		
		testRawUrl = "http://www.sec.gov/Archives/edgar/data/30554/0000930413-04-000799.txt";
		actualUrl = SecFilingRegeRule.getSecForm10FilingUrl(testRawUrl);
		expectedUrl = "http://www.sec.gov/Archives/edgar/data/30554/0000930413-04-000799-index.html";
		assertResult(actualUrl, expectedUrl);
		
		
		testRawUrl = "http://www.sec.gov/Archives/edgar/data/30554/0001047469-05-005183.txt";
		actualUrl = SecFilingRegeRule.getSecForm10FilingUrl(testRawUrl);
		expectedUrl = "http://www.sec.gov/Archives/edgar/data/30554/0001047469-05-005183-index.html";
		assertResult(actualUrl, expectedUrl);
	}	
	
	@Test
	public void getSecForm10KFileNameTest() {
		String testRawUrl = "http://www.sec.gov/Archives/edgar/data/30554/0000930413-03-000634.txt";
		String actualFileName = SecFilingRegeRule.getSecForm10KFileName(testRawUrl);
		String expectedFileName = "0000930413-03-000634.txt";
		assertResult(actualFileName, expectedFileName);
		
		
		testRawUrl = "http://www.sec.gov/Archives/edgar/data/30554/0000930413-04-000799.txt";
		actualFileName = SecFilingRegeRule.getSecForm10KFileName(testRawUrl);
		expectedFileName = "0000930413-04-000799.txt";
		assertResult(actualFileName, expectedFileName);
		
		
		testRawUrl = "http://www.sec.gov/Archives/edgar/data/30554/0001047469-05-005183.txt";
		actualFileName = SecFilingRegeRule.getSecForm10KFileName(testRawUrl);
		expectedFileName = "0001047469-05-005183.txt";
		assertResult(actualFileName, expectedFileName);
	}
	
	@Test
	public void matchRegexTest() {
		String regexRule = "([<][0-9a-zA-Z=\"' ]*)([page][a-r-]*[before][:{1}][always])([0-9a-zA-Z&'\";/ <>#=]*[>]?)";
		
		String string = "<H5 align=\"left\" style=\"page-break-before:always\">&nbsp;</H5><P>";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, false);
		assertTrue(result);
		
		string = "<H5 align=\"left\" style=\"page-break-before:always\"><A HREF=\"#toc\">Table of Contents</A></H5><P>";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, false);
		assertTrue(result);
		
		string = "<p style='page-break-before:always'>";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, false);
		assertTrue(result);
		
		string = "<DIV align=\"center\" style=\"font-size: 10pt; margin-top: 12pt\"><B>UNITED STATES<BR>";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, false);
		assertFalse(result);
		
		string = "<sequence>1";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, false);
		assertFalse(result);
		
		string = "<description>sharevalue program";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, false);
		assertFalse(result);
	}
	
	@Test
	public void matchRegexTest1() {
		String regexRule = "([<][-! ]*)(pagebreak{1})([ -]*[>]?)";
		
		String string = "<!-- pagebreak -->";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, false);
		assertTrue(result);
	}
	
	@Test
	public void tableofContentRegexTest() {
		String regexRule = "([ ]*)(table{1})([ ]+)(of{1})([ ]+)(contents{1})([ ]*)";
		
		String string = "   TABLE OF CONTENTS    ";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
	}
	
	@Test
	public void itemPageRegexTest() {
		String regexRule = "([ ]*)(item{1})([ ]+)(page{1})([ ]*)";
		
		String string = "      item                                                              page    ";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
		
		result = SecFilingRegeRule.matchRegex(string, regexRule, true, true);
		assertTrue(result);
	}
	
	@Test
	public void partRommanNumberRegexTest() {
		String regexRule = "([ ]*)(part{1})([ ]+)(IX|IV|V?I{0,3})([.]?)([ ]*)";
		
		String string = " PART III.     ";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
		
		string = " PART II.     ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, true, true);
		assertTrue(result);
		
		string = " PART I.     ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, true, true);
		assertTrue(result);
		
		string = " PART IV     ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, true, true);
		assertTrue(result);
	}
	
	@Test
	public void matchDigitOnlyRegexTest() {
		String regexRule = "[0-9]+|^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
		
		String string = "                                                                            i";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, true, true);
		assertTrue(result);
		
		string = "";
		result = SecFilingRegeRule.matchRegex(string, regexRule, true, true);
		assertFalse(result);
		
		string = "                                                                          127";
		result = SecFilingRegeRule.matchRegex(string, regexRule, true, true);
		assertTrue(result);
	}
	
	@Test
	public void tableofContentDataRegexTest() {
		String regexRule = "([  \\t]*)([item]*[ ]*)([0-9]+[a-zA-Z]?[.]{1})([ ]*)([a-zA-z,' ]+)([ ]*)([0-9]+)([ ]*)";
		
		String string = " 1.      BUSINESS                                                      8 ";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
		
		string = "   1A.     RISK FACTORS                                                 20 ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
		
		string = "6.      SELECTED FINANCIAL DATA                                      32";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
		
		/*string = "           GENERAL                                                       8";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "		   5.      MARKET FOR REGISTRANT'S COMMON EQUITY, RELATED          ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "           Stockholder Matters and Issuer Purchases of Equity Securities ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	*/
		
		/*string = "   Item 10. Directors, Executive Officers and Corporate Governance        107  ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "   Item 5.  Market for the Registrant's Common Equity, Related            29   ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "   Item 13.    Certain Relationships and Related Transactions,                ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "   Item 7.     Management's Discussion and Analysis of                        ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "               Accounting and Financial Disclosure                     154   ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "   Signatures                                                          167    ";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);	
		
		string = "PART II ITEM 7. MANAGEMENT'S \\n DISCUSSION AND ANALYSIS OF FINANCIAL CONDITION AND RESULTS OF OPERATIONS";*/

	}
	
	@Test
	public void pageNumberRegexTest() {
		String regexRule = "[ ]*[-]?[ ]*[(]?[0-9]+[)]?[ ]*[-]?[ ]*[ SWN]*|^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
		
		String string = "   - 1 -";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
	}
	
	@Test
	public void headingRegexTest(){
		String regexRule = "(ITEM[ ]*[\\n]?[\\t]?)([8]{1}[.]?[ ]*)(FINANCIAL[ ]*)(STATEMENT[S]?[ ]*)((AND | &)[ ]*)(SUPPLEMENTAL[ ]*)(DATA[ ]*)([(]?CONTINUED[)]?[ ]*)?";
				//+ "[ ]*[FINANCIAL][ ]*[STATEMENTS][ ]*[AND][ ]*[SUPPLEMENTAL][ ]*[DATA][ ]*";
		
		String string = "aaaaaaaaaa ITEM 8. FINANCIAL STATEMENTS AND SUPPLEMENTAL DATA anknsdkhnvjkshjk";
		boolean result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);
		
		string = "ITEM 8. FINANCIAL STATEMENTS AND SUPPLEMENTAL DATA (continued)";
		result = SecFilingRegeRule.matchRegex(string, regexRule, false, true);
		assertTrue(result);

	}
	
	private void assertResult(String actualUrl, String expectedUrl){
		assertNotNull(actualUrl);
		assertEquals(expectedUrl, actualUrl);
	}
	

}
