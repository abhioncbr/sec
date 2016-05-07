package com.capiq.sec.filling.analysis.parse.process;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.file.FileAccessorUtil;
import com.capiq.sec.filling.analysis.file.IFileAccessor;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContextEnum;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.LineContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecDocument;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.SecPage;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.TocContent;
import com.capiq.sec.filling.analysis.parse.document.dataStructure.TocContentDetails;
import com.capiq.sec.filling.analysis.parse.stat.IParseStat;
import com.capiq.sec.filling.analysis.parse.stat.MdaExtractionStatData;

public class TocBasedMdaContentExtractor extends AbstractProcess {
	private volatile TocContentDetails mdaContentDetail;
	private volatile TocContentDetails financialStstementContentDetail;

	@Override
	public boolean condition(ParseDocumentContext context) {
		Boolean output = context.getContextValue(ParseDocumentContextEnum.MDA_EXTRACTED, Boolean.class);
		if(output == null || output) return false;
		
		
		TocContent content = context.getContextValue(ParseDocumentContextEnum.PARSED_TABLE_OF_CONTENT_DATA, TocContent.class);
		if(content == null){
			System.out.println("No content index entries.");
			return output;
		}	
		Map<String, TocContentDetails> map = content.getContentDetailsMap();
		
		for(Entry<String, TocContentDetails> entries : map.entrySet()){
			String key = entries.getKey();
			
			System.out.println(key + " : " + entries.getValue());
			
			if(key.contains("7") &&  !key.contains("A"))
				mdaContentDetail = entries.getValue();
			else if(key.contains("8"))
				financialStstementContentDetail = entries.getValue();
		}
		
		if(mdaContentDetail !=null && financialStstementContentDetail!=null)
			output= true;
			
			
		return output;
	}

	@Override
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException {
		SecDocument document = context.getContextValue(ParseDocumentContextEnum.CLEAN_SEC_DOCUMENT_OBJECT, SecDocument.class);
		String filePath = context.getContextValue(ParseDocumentContextEnum.MDA_DOCUMENT_PATH, String.class);
		Boolean isHdfsStorage = context.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
		
		int numberOfLines = 0;
		try {
			IFileAccessor fileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfsStorage);
			PrintWriter writer = fileAccessor.getPrintWriter();

			for(SecPage documentPage : document.getPageContent()){

				int pageNumber = documentPage.getParsePageNumber();
				if(pageNumber ==-1)
					pageNumber = documentPage.getDocumentPageNumber();
					
				if(pageNumber >= mdaContentDetail.getPageNumber() && 
						pageNumber <= financialStstementContentDetail.getPageNumber()){
					LinkedList<SecContent> secContentList = documentPage.getPageContent();
					for(SecContent secContent : secContentList){
						for(LineContent content: secContent.getPageContent()){
							numberOfLines++;
							writer.println(content.getStringContent());
						}	
					}
				}
			}
			fileAccessor.closeIOStream();
			
			IParseStat parseStat = new MdaExtractionStatData(context, true, numberOfLines, "Toc Based");
			context.setContextValue(ParseDocumentContextEnum.IPARSE_STAT, parseStat);
			context.setContextValue(ParseDocumentContextEnum.MDA_EXTRACTED, new Boolean("True"));
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		} 
	}
	
}
