package com.capiq.sec.filling.analysis.parse.preprocess.rule;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.file.FileAccessorUtil;
import com.capiq.sec.filling.analysis.file.IFileAccessor;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContextEnum;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.RawSecDocument;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.TocContent;
import com.capiq.sec.filling.analysis.parse.jflex.JflexSecLexer;
import com.capiq.sec.filling.analysis.parse.jflex.JflexToken;
import com.capiq.sec.filling.analysis.parse.jflex.JflexTokenDataStructure;
import com.capiq.sec.filling.analysis.parse.jflex.JflexTokenType;
import com.capiq.sec.filling.analysis.parse.preprocess.AbstractPreProcess;
import com.capiq.sec.filling.analysis.regex.SecFilingRegeRule;

public class JflexLexerMarkerPreProcessor extends AbstractPreProcess {
	private static final String COMPLETE_RULE = "([item]*[ ]*)([0-9][a-zA-Z]?[.]?[:]?)([ ]*)([a-zA-z,.' ]+)([ ]*)([0-9]+)([-][0-9]+)?";
	
	@Override
	public boolean condition(ParseDocumentContext context) {
		String cleanFilePath = context.getContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, String.class);
		if(StringUtils.isBlank(cleanFilePath))
			return false;
		
		return true;
	}

	@Override
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException {
		String cleanFilePath = context.getContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, String.class);
		
		try {
			JflexTokenDataStructure flexTokenDataStructure = performLexing(cleanFilePath, context);
			//print(flexTokenDataStructure);
			context.setContextValue(ParseDocumentContextEnum.JFLEX_MARKERS, flexTokenDataStructure);
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}
	
	private JflexTokenDataStructure performLexing(String filePath, ParseDocumentContext context) throws IOException {
		Boolean isHdfsStorage = context.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
		IFileAccessor fileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfsStorage);
		
		JflexSecLexer lexer = new JflexSecLexer(fileAccessor.getInputStreamReader());
		JflexTokenDataStructure flexTokenDataStructure = new JflexTokenDataStructure();
		while (true) {
			JflexToken token = lexer.yylex();
			
			JflexTokenType type = token.getTokenType();
			if (type == JflexTokenType.EOF) break;
			
			flexTokenDataStructure.addToken(token);
		}
		fileAccessor.closeIOStream();
		
		context.setContextValue(ParseDocumentContextEnum.PAGE_DIVIDER_LENGTH, lexer.getPageBreakLength());
		context.setContextValue(ParseDocumentContextEnum.HYPHEN_PAGE_DIVIDER_LENGTH, lexer.getHyphenPageBreakLength());
		
		LinkedList<Integer> item7TokenList = flexTokenDataStructure.getTokenTypeIndexList(JflexTokenType.TOC_MDA_STRING_WITH_NUMBER);
		LinkedList<Integer> item8TokenList = flexTokenDataStructure.getTokenTypeIndexList(JflexTokenType.TOC_FSSD_STRING_WITH_NUMBER);
		LinkedList<Integer> item7HalfMatchTokenList = flexTokenDataStructure.getTokenTypeIndexList(JflexTokenType.MDA_STRING_FIRST_HALF);
		
		boolean item7Condition = item7TokenList != null && item7TokenList.size()==1 ? true : false;
		boolean item8Condition = item8TokenList != null && item8TokenList.size()==1 ? true : false;
		boolean item7HalfMatchCondition = item7HalfMatchTokenList != null && item7HalfMatchTokenList.size()==1 ? true : false;
		if(item7Condition &&  item8Condition){
			TocContent indexContent = new TocContent();
			process(flexTokenDataStructure.getToken(item7TokenList.getFirst()).getTokenText(), indexContent);
			process(flexTokenDataStructure.getToken(item8TokenList.getFirst()).getTokenText(), indexContent);
			
			context.setContextValue(ParseDocumentContextEnum.PARSED_TABLE_OF_CONTENT_DATA, indexContent);
		} else if (item7HalfMatchCondition && item8Condition){
			
			RawSecDocument rawDoc = context.getContextValue(ParseDocumentContextEnum.CLEAN_RAW_SEC_DOCUMENT_OBJECT, RawSecDocument.class);
			rawDoc.open();
			int firstHalfLineNumber = flexTokenDataStructure.getToken(item7HalfMatchTokenList.getFirst()).getLineNumber();
			String firstHalf = rawDoc.getLineContent(firstHalfLineNumber).trim();
			Matcher completeMatcher = SecFilingRegeRule.getRegexMatcher(firstHalf, COMPLETE_RULE, false, true);		
			
			if(completeMatcher.matches()){
				TocContent indexContent = new TocContent();
				process(firstHalf, indexContent);
				process(flexTokenDataStructure.getToken(item8TokenList.getFirst()).getTokenText(), indexContent);
				
				context.setContextValue(ParseDocumentContextEnum.PARSED_TABLE_OF_CONTENT_DATA, indexContent);

			}
			rawDoc.Close();
		}
		
		return flexTokenDataStructure;
	}
	
	private void process(String line, TocContent indexContent ){
		line = line.replaceAll("[\n\r]", "");
		
		Matcher completeMatcher = SecFilingRegeRule.getRegexMatcher(line, COMPLETE_RULE, false, true);
		try{
			if(completeMatcher.matches()){
				int pageNumber = Integer.parseInt(completeMatcher.group(6).trim());
				String indexNumber = completeMatcher.group(2).trim() ;
				String indexHeading = completeMatcher.group(4).trim();
				indexContent.addContent(pageNumber, indexNumber, indexHeading);
			}
		} catch(Exception e) {
			System.out.println("Parse Failure : " + completeMatcher.group(6).trim());
		}
	}
	
	private void print(JflexTokenDataStructure flexTokenDataStructure){
		for(Entry<JflexTokenType, LinkedList<Integer>> entry: flexTokenDataStructure.getAllTokenType().entrySet()){
			LinkedList<Integer> list = entry.getValue();
			for(Integer index: list){
				JflexToken token = flexTokenDataStructure.getToken(index);
				System.out.printf("%s(\"%s\"),%d , %d  %n", 
						token.getTokenType(), token.getTokenText(), token.getLineNumber(), token.getColumnNumber());
			}
		}
	}

	public static void main(String[] argsv) throws IOException{
		JflexLexerMarkerPreProcessor proc = new JflexLexerMarkerPreProcessor();
		
		ParseDocumentContext context = new ParseDocumentContext();
		context.setContextValue(ParseDocumentContextEnum.CLEAN_RAW_SEC_DOCUMENT_OBJECT, argsv[0]);
		
		proc.print(proc.performLexing(argsv[0], context));
	}
}
