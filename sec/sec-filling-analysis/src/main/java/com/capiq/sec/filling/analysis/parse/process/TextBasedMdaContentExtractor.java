package com.capiq.sec.filling.analysis.parse.process;

import java.io.PrintWriter;
import java.util.LinkedList;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.file.FileAccessorUtil;
import com.capiq.sec.filling.analysis.file.IFileAccessor;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContextEnum;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.RawSecDocument;
import com.capiq.sec.filling.analysis.parse.jflex.JflexToken;
import com.capiq.sec.filling.analysis.parse.jflex.JflexTokenDataStructure;
import com.capiq.sec.filling.analysis.parse.jflex.JflexTokenType;
import com.capiq.sec.filling.analysis.parse.stat.IParseStat;
import com.capiq.sec.filling.analysis.parse.stat.MdaExtractionStatData;

public class TextBasedMdaContentExtractor extends AbstractProcess {
	
	@Override
	public boolean condition(ParseDocumentContext context) {
		RawSecDocument rawSecDocument = context.getContextValue(ParseDocumentContextEnum.CLEAN_RAW_SEC_DOCUMENT_OBJECT, RawSecDocument.class);
		if(rawSecDocument == null)
			return false;

		
		Boolean output = context.getContextValue(ParseDocumentContextEnum.MDA_EXTRACTED, Boolean.class);
		
		JflexTokenDataStructure flexTokenMap = context.getContextValue(ParseDocumentContextEnum.JFLEX_MARKERS, JflexTokenDataStructure.class);
		LinkedList<Integer> item7JflexMarkerList = flexTokenMap.getTokenTypeIndexList(JflexTokenType.MDA_STRING);
		LinkedList<Integer> item8JflexMarkerList = flexTokenMap.getTokenTypeIndexList(JflexTokenType.FSSD_STRING);
		
		output = item7JflexMarkerList != null ? true : false;
		if(output) output = item8JflexMarkerList != null ? true : false;
		
		return output;
	}

	@Override
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException {
		JflexTokenDataStructure flexTokenMap = context.getContextValue(ParseDocumentContextEnum.JFLEX_MARKERS, JflexTokenDataStructure.class);
		LinkedList<Integer> item7JflexMarkerList = flexTokenMap.getTokenTypeIndexList(JflexTokenType.MDA_STRING);
		LinkedList<Integer> item8JflexMarkerList = flexTokenMap.getTokenTypeIndexList(JflexTokenType.FSSD_STRING);

		if(item7JflexMarkerList.size() > 0 && item8JflexMarkerList.size() > 0)
			removeJflexTocMarker(item7JflexMarkerList, item8JflexMarkerList, flexTokenMap);

		if(item7JflexMarkerList.size() > 0 && item8JflexMarkerList.size() > 0){
			int item7JflexMarkerLineNumber = flexTokenMap.getToken(item7JflexMarkerList.getFirst()).getLineNumber();
			int item8JflexMarkerLineNumber = flexTokenMap.getToken(item8JflexMarkerList.getFirst()).getLineNumber();
			
			writeExtractedFile(context, item7JflexMarkerLineNumber, item8JflexMarkerLineNumber);
		}
			
	}
	
	private void removeJflexTocMarker(LinkedList<Integer> item7JflexMarkerList, LinkedList<Integer> item8JflexMarkerList, JflexTokenDataStructure flexTokenMap){
		JflexToken item7JflexMarker = flexTokenMap.getToken(item7JflexMarkerList.getFirst());
		JflexToken item8JflexMarker = flexTokenMap.getToken(item8JflexMarkerList.getFirst());
		
		if(item7JflexMarker.getLineNumber() + 5 > item8JflexMarker.getLineNumber()){
			item7JflexMarkerList.remove();
			item8JflexMarkerList.remove();
		}
	}

	
	private void writeExtractedFile(ParseDocumentContext context, int item7LineNumber, int item8LineNumber) throws SymantecAnalysisGeneralException{
		String filePath = context.getContextValue(ParseDocumentContextEnum.MDA_DOCUMENT_PATH, String.class);
		Boolean isHdfsStorage = context.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
		
		RawSecDocument rawSecDocument = context.getContextValue(ParseDocumentContextEnum.CLEAN_RAW_SEC_DOCUMENT_OBJECT, RawSecDocument.class);
		
		try {
			IFileAccessor fileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfsStorage);
			PrintWriter writer = fileAccessor.getPrintWriter();
			rawSecDocument.open();
			int numberOfLines = 0;
			
			for(int i = item7LineNumber; i <= item8LineNumber; i++){
				String line = rawSecDocument.getLineContent(i);
				writer.println(line);
				numberOfLines++;
			}
			
			fileAccessor.closeIOStream();
			
			IParseStat parseStat = new MdaExtractionStatData(context, true, numberOfLines, "Text Based");
			context.setContextValue(ParseDocumentContextEnum.IPARSE_STAT, parseStat);
			context.setContextValue(ParseDocumentContextEnum.MDA_EXTRACTED,true);
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		} 
	}
	
}
