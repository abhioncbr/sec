package com.capiq.sec.filling.analysis.parse.preprocess.rule;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.parse.ParseRule;
import com.capiq.sec.filling.analysis.parse.ParseRule.Rule;
import com.capiq.sec.filling.analysis.parse.ParseRuleAccessor;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContextEnum;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.RawSecDocument;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecDocument;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecPage;
import com.capiq.sec.filling.analysis.parse.preprocess.AbstractPreProcess;
import com.capiq.sec.filling.analysis.parse.util.ParseCommonUtil;
import com.capiq.sec.filling.analysis.regex.SecFilingRegeRule;

public class PageBreakPreProcessor extends AbstractPreProcess {
	private final ParseRule PAGE_BREAK_RULE;
	private ConditionRule pageBreakRule;

	public PageBreakPreProcessor() throws SymantecAnalysisGeneralException {
		PAGE_BREAK_RULE = ParseRuleAccessor.getPageBreakRule();
	}

	@Override
	public boolean condition(ParseDocumentContext context) {
		boolean output = false;
		
		RawSecDocument rawDoc = context.getContextValue(ParseDocumentContextEnum.CLEAN_RAW_SEC_DOCUMENT_OBJECT, RawSecDocument.class);
		if(rawDoc ==  null) return output;
		
		Map<Integer,ConditionRule> rules = new LinkedHashMap<Integer,ConditionRule>();
		for (Rule rule : PAGE_BREAK_RULE.getRuleList()) {
			if(rules.containsKey(rule.getGroup())){
				ConditionRule temp = rules.get(rule.getGroup());
				temp.addRule(rule);
			} else { 
				rules.put(rule.getGroup(), new ConditionRule(rule));
			}	
		}
		
		pageBreakRule =rules.get(0);
		output=true;
		return output;
	}

	@Override
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException {
		try {
			RawSecDocument rawDoc = context.getContextValue(ParseDocumentContextEnum.CLEAN_RAW_SEC_DOCUMENT_OBJECT, RawSecDocument.class);
			rawDoc.open();
			
			LinkedList<SecPage> pageList =  new LinkedList<SecPage>();
			LinkedList<SecContent> secContentList =  new LinkedList<SecContent>();
			
			SecContent secContent = new SecContent();
			
			int pageLineNumber = 0;
			int documentLineNumber = 0;
			int pageNumber = 0;
			
			//TODO: imply proper logic here
			Rule pageNumberRule = pageBreakRule.ruleList.getFirst();
			Rule dividerRule = pageBreakRule.ruleList.getLast();
			
			Integer jflexDividerLength = context.getContextValue(ParseDocumentContextEnum.PAGE_DIVIDER_LENGTH, Integer.class);
			Integer jflexHyphenDividerLength = context.getContextValue(ParseDocumentContextEnum.HYPHEN_PAGE_DIVIDER_LENGTH, Integer.class);
			int dividerLength	= jflexDividerLength !=null ? jflexDividerLength : -1;
			dividerLength =  dividerLength == -1 ? jflexHyphenDividerLength : dividerLength;
			
			String previousLine = null;
			for (String line : rawDoc) {
				//incrementing document line number.
				++documentLineNumber;
				
					if(SecFilingRegeRule.matchRegex(line, dividerRule.getRule(), dividerRule.getTrim(), dividerRule.getCaseInsensitive()) 
									&& line.trim().length()==dividerLength){
						secContentList.add(secContent);
						
						int pageStartLineNumber = documentLineNumber - pageLineNumber;
						int parsePageNumber = deducePageNumber(previousLine, pageNumberRule);
						SecPage pageContent =  new SecPage(secContentList, pageStartLineNumber, documentLineNumber, parsePageNumber, ++pageNumber);
						pageList.add(pageContent);
						
						secContent = new SecContent();
						secContentList =  new LinkedList<SecContent>();
						pageLineNumber = 0;
					}
					secContent.setContent(line, ++pageLineNumber, documentLineNumber);
					previousLine =  StringUtils.isBlank(line) ? previousLine : line;	
			}	
			
			secContentList.add(secContent);
			int pageStartLineNumber = documentLineNumber - pageLineNumber;
			int parsePageNumber = deducePageNumber(previousLine, pageNumberRule);
			SecPage pageContent =  new SecPage(secContentList, pageStartLineNumber, documentLineNumber, parsePageNumber, ++pageNumber);
			pageList.add(pageContent);
			
			SecDocument document = new SecDocument(pageList);
			context.setContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, document);
			
			rawDoc.Close();
		} catch (IOException ex) {
			throw new SymantecAnalysisGeneralException(ex);
		}
	}
	
	private int deducePageNumber(String previousLine, Rule pageNumberRule) throws SymantecAnalysisGeneralException{
		if(previousLine == null)
			return -1;
		
		Integer parsePageNumber = new Integer(-1);
		if(SecFilingRegeRule.matchRegex(previousLine, pageNumberRule.getRule(), pageNumberRule.getTrim(), pageNumberRule.getCaseInsensitive())){
			String temp = previousLine.trim();
			parsePageNumber = parsePageNumber(temp);
		}
		return parsePageNumber;
	}
	
	private int parsePageNumber(String temp) throws SymantecAnalysisGeneralException{
		int output = -1;
		if(ParseCommonUtil.isRommanNumeral(temp))
			output = ParseCommonUtil.toIntegerRegex(temp.toUpperCase());
		else if(ParseCommonUtil.isNumeral(temp))
			output = Integer.parseInt(temp);
		else if(ParseCommonUtil.isNumeralwithFixes(temp)){
			output = ParseCommonUtil.parseNumeralwithFixes(temp);
		} else{
			throw new SymantecAnalysisGeneralException("Not able to parse page number : " + temp);
		}
		return output;
	}
}
