package com.sec.filing.analysis.parse.preprocess.rule;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;

import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.document.ParseDocumentContextEnum;
import com.sec.filing.analysis.parse.document.dataStructure.LineContent;
import com.sec.filing.analysis.parse.document.dataStructure.SecContent;
import com.sec.filing.analysis.parse.document.dataStructure.SecDocument;
import com.sec.filing.analysis.parse.document.dataStructure.SecPage;
import com.sec.filing.analysis.parse.document.dataStructure.TocContent;
import com.sec.filing.analysis.parse.preprocess.AbstractPreProcess;
import com.sec.filing.analysis.regex.SecFilingRegeRule;

public class ParseContentTablePreProcessor extends AbstractPreProcess {
	
	private static final String END_RULE = "([ ]*)([a-zA-z,' ]+)([ ]*)([0-9]+)([ ]*)";
	private static final String START_RULE = "([  \\t]*)([item]*[ ]*)([0-9][a-zA-Z]?[.]?)([ ]*)([a-zA-z,' ]+)([ ]*)";
	private static final String COMPLETE_RULE = "([  \\t]*)([item]*[ ]*)([0-9][a-zA-Z]?[.]?)([ ]*)([a-zA-z,' ]+)([ ]*)([0-9]+)([ ]*)";
	
	@Override
	public boolean condition(ParseDocumentContext context) {
		TocContent content = context.getContextValue(ParseDocumentContextEnum.PARSED_TABLE_OF_CONTENT_DATA, TocContent.class);
		if(content != null){
			return false;
		}

		boolean output = false;
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		SecPage tocPage = document.getFilingToCPage();
	
		if(tocPage !=null)
			output = true;
		else{
			System.out.println(context.getContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, String.class) + " - no toc page");
			output = false;
		}
			
		return output;
	}

	@Override
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException {
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		SecPage tocPage = document.getFilingToCPage();
		LinkedList<SecContent> contentList = tocPage.getPageContent();
		
		TocContent  indexContent =  new TocContent();
		for(SecContent secContent : contentList){
			Iterator<LineContent> contentIterator =  secContent.getPageContent().iterator();
			
			LineContent lineContent = null;
			StringBuilder previousLine = new StringBuilder(); 
			
			while(contentIterator.hasNext()){
				lineContent= contentIterator.next();
				String line = lineContent.getStringContent();
				boolean startLineAppended = false;
				
				Matcher endMatcher = SecFilingRegeRule.getRegexMatcher(line, END_RULE, false, true);
				Matcher startMatcher = SecFilingRegeRule.getRegexMatcher(line, START_RULE, false, true);
				Matcher completeMatcher = SecFilingRegeRule.getRegexMatcher(line, COMPLETE_RULE, false, true);
				if(completeMatcher != null && completeMatcher.matches()){
					processCompleteMatcher(completeMatcher, indexContent);
				} else if(startMatcher != null && startMatcher.matches()) {
					previousLine = new StringBuilder();
					previousLine.append(line);
					startLineAppended = true;
				} else if(endMatcher != null && endMatcher.matches()) {
					if(startLineAppended) {
						previousLine.append(line);
						completeMatcher = SecFilingRegeRule.getRegexMatcher(previousLine.toString(), COMPLETE_RULE, false, true);
						if(completeMatcher != null && completeMatcher.matches()) processCompleteMatcher(completeMatcher, indexContent);
					} else startLineAppended = false;
				} 
				
				if(indexContent.getContentDetailsMap().size()>12)
					break;
			}
			// Adding parsed table of content data in parsing context
			context.setContextValue(ParseDocumentContextEnum.PARSED_TABLE_OF_CONTENT_DATA, indexContent);
		}	

	}
	
	public void processCompleteMatcher(Matcher completeMatcher, TocContent indexContent) throws SymantecAnalysisGeneralException{
			try{
				int pageNumber = Integer.parseInt(completeMatcher.group(7).trim());
				String indexNumber = completeMatcher.group(3).trim() ;
				String indexHeading = completeMatcher.group(5).trim();
				indexContent.addContent(pageNumber, indexNumber, indexHeading);
			} catch(Exception e) {
				throw new SymantecAnalysisGeneralException("Parse Failure : " + completeMatcher.group(7).trim(), e);
			}
	}

}
