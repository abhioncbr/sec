package com.sec.filing.analysis.parse.preprocess.rule;

import java.util.Iterator;
import java.util.LinkedList;

import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.document.ParseDocumentContextEnum;
import com.sec.filing.analysis.parse.document.dataStructure.LineContent;
import com.sec.filing.analysis.parse.document.dataStructure.SecContent;
import com.sec.filing.analysis.parse.document.dataStructure.SecDocument;
import com.sec.filing.analysis.parse.document.dataStructure.SecPage;
import com.sec.filing.analysis.parse.document.dataStructure.TocContent;
import com.sec.filing.analysis.parse.jflex.JflexToken;
import com.sec.filing.analysis.parse.jflex.JflexTokenDataStructure;
import com.sec.filing.analysis.parse.jflex.JflexTokenType;
import com.sec.filing.analysis.parse.preprocess.AbstractPreProcess;

public class ContentTableJflexPreProcessor extends AbstractPreProcess {
	
	@Override
	public boolean condition(ParseDocumentContext context) {
		TocContent content = context.getContextValue(ParseDocumentContextEnum.PARSED_TABLE_OF_CONTENT_DATA, TocContent.class);
		if(content != null) return false;

		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		if(document == null) return false;

		boolean output = false;
		JflexTokenDataStructure flexTokenMap = context.getContextValue(ParseDocumentContextEnum.JFLEX_MARKERS, JflexTokenDataStructure.class);
		if(flexTokenMap != null && flexTokenMap.getAllToken().size() > 0) {
			output = true;
		}
		
		return output;
	}

	@Override
	public void process(ParseDocumentContext context) {
		JflexTokenDataStructure flexTokenMap = context.getContextValue(ParseDocumentContextEnum.JFLEX_MARKERS, JflexTokenDataStructure.class);

		//Processing based on TOC token deduced through Jflex lexing.  
		LinkedList<Integer> tocIndexList = flexTokenMap.getTokenTypeIndexList(JflexTokenType.TOC);
		JflexToken toctoken = tocIndexList != null && tocIndexList.size() > 0 ? flexTokenMap.getToken(tocIndexList.getFirst()) : null;

		
		//deduction based TOC_FSSD_STRING & TOC_MDA_STRING
		LinkedList<Integer> tocMdaList = flexTokenMap.getTokenTypeIndexList(JflexTokenType.TOC_MDA_STRING);
		JflexToken tocMdatoken = tocMdaList != null && tocMdaList.size() ==1 ? flexTokenMap.getToken(tocMdaList.getFirst()) : null;

		LinkedList<Integer> tocFssdList = flexTokenMap.getTokenTypeIndexList(JflexTokenType.TOC_FSSD_STRING);
		JflexToken tocFssdtoken = tocFssdList != null && tocFssdList.size() ==1 ? flexTokenMap.getToken(tocFssdList.getFirst()) : null;
		
		if(tocMdatoken != null && tocFssdtoken!= null){
			if(deduceTocPage(tocMdatoken, tocFssdtoken,context))
				return;
		}
		
		//deduce consecutive tokens present in Table Of Content Page
		LinkedList<JflexToken> itemLinkedList = flexTokenMap.getAllToken();
		LinkedList<JflexToken> TocItemList = deduceTocItems(itemLinkedList, flexTokenMap);
		if(TocItemList != null && TocItemList.size()>0){
			context.setContextValue(ParseDocumentContextEnum.TOC_TOKEN_LIST, TocItemList);
			deduceTocPage(toctoken, TocItemList, context);
		}
	}
	
	private Boolean deduceTocPage(JflexToken tocMdatoken, JflexToken tocFssdtoken, ParseDocumentContext context){
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		SecPage tocMdaPage = null;
		SecPage tocFssdPage = null;
		
		tocMdaPage = document.getSecPageContainsLineNumber(tocMdatoken.getLineNumber());
		tocFssdPage = document.getSecPageContainsLineNumber(tocFssdtoken.getLineNumber());
		
		if(tocMdaPage.equals(tocFssdPage)){
			document.setFilingToCPage(tocMdaPage);
			context.setContextValue(ParseDocumentContextEnum.TOC_DEDUCED, true);
			return true;
		} else return false;
	}
	
	private void deduceTocPage(JflexToken tocToken, LinkedList<JflexToken> TocItemList, ParseDocumentContext context){
		context.setContextValue(ParseDocumentContextEnum.TOC_DEDUCED, false);
		
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		SecPage documentPage = document.getSecPageContainsLineNumber(TocItemList.getFirst().getLineNumber());
		if(matchTocTokenList(documentPage, tocToken, TocItemList)){
			document.setFilingToCPage(documentPage);
			context.setContextValue(ParseDocumentContextEnum.TOC_DEDUCED, true);
			return;
		}
		
		for(SecPage page : document.getPageContent()){
			if(matchTocTokenList(page, tocToken, TocItemList)){
				document.setFilingToCPage(page);
				context.setContextValue(ParseDocumentContextEnum.TOC_DEDUCED, true);
				break;
			}
		}
	}
	
	private boolean matchTocTokenList(SecPage documentPage, JflexToken tocToken, LinkedList<JflexToken> TocItemList){
		boolean tocLineMatched = false;
		boolean tocItemMatched = false;
		
		int tocItemIndexCounter = 0;
		int tocItemEndIndex = TocItemList.size();

		//TODO: imply logic for fetching document as per data structure.
		LinkedList<SecContent> secContentList = documentPage.getPageContent();
		for(SecContent secContent : secContentList){
			Iterator<LineContent> contentIterator =  secContent.getPageContent().iterator();
			while(contentIterator.hasNext()){
				LineContent lineContent= contentIterator.next();
				if(tocToken!= null && tocToken.getLineNumber().equals(lineContent.getDocumentLineNumber())){
					tocLineMatched = true;
				}
				
				if(tocItemIndexCounter < tocItemEndIndex && TocItemList.get(tocItemIndexCounter).getLineNumber().equals(lineContent.getDocumentLineNumber())){
					do {
						tocItemIndexCounter++;
					} while(tocItemIndexCounter < tocItemEndIndex && TocItemList.get(tocItemIndexCounter).getLineNumber().equals(lineContent.getDocumentLineNumber()));
				} else if(tocItemIndexCounter == tocItemEndIndex && tocItemIndexCounter != 0){
					tocItemMatched = true;
					break;
				}
			}
		}
		
		if(tocLineMatched || tocItemMatched)
			return true;	
		else return false;
	}
	
	private LinkedList<JflexToken> deduceTocItems(LinkedList<JflexToken> itemLinkedList, JflexTokenDataStructure flexTokenMap){
		LinkedList<JflexToken> TocItemList = new LinkedList<JflexToken>();
		JflexToken previousToken = itemLinkedList.getFirst();
		for(JflexToken token : itemLinkedList.subList(1, itemLinkedList.size())){
			if(token.getLineNumber() - previousToken.getLineNumber() < 2){
				TocItemList.add(previousToken);
			} else if(TocItemList.size() > 6) {
				break;
			}
			previousToken = token;
		}
		return TocItemList;
	}
}
