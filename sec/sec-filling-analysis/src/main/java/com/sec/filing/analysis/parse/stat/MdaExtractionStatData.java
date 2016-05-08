package com.sec.filing.analysis.parse.stat;

import java.io.IOException;
import java.util.Map.Entry;

import com.sec.filing.analysis.file.FileAccessorUtil;
import com.sec.filing.analysis.file.IFileAccessor;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.document.ParseDocumentContextEnum;
import com.sec.filing.analysis.parse.document.dataStructure.TocContent;
import com.sec.filing.analysis.parse.document.dataStructure.TocContentDetails;

public class MdaExtractionStatData implements IParseStat{
	private static final long serialVersionUID = -7425965740909505105L;

	private String fileName;
	
	private String rawFileSize;
	private String cleanFileSize;
	private String binaryIgnoredFileSize;

	private Boolean item7tocDeduced = false;
	private Boolean item8tocDeduced = false;

	private Boolean mdaExtracted = false;
	private String extractionRule;
	private Integer linesInExtractedData;
	
	public MdaExtractionStatData(ParseDocumentContext context, Boolean mdaExtracted, Integer linesInExtractedData, String extractionRule) throws IOException {
		super();
		this.fileName = context.getContextValue(ParseDocumentContextEnum.FILE_NAME, String.class);
		
		String filePath = context.getContextValue(ParseDocumentContextEnum.RAW_DOCUMENT_PATH, String.class);
		boolean isHdfs = context.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
		
		IFileAccessor rawFileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfs);
		this.rawFileSize =  rawFileAccessor.getFileSize();
		rawFileAccessor.closeIOStream();
		
		filePath = context.getContextValue(ParseDocumentContextEnum.NON_BINARY_DOCUMENT_PATH, String.class);
		IFileAccessor nonBinaryFileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfs);
		this.binaryIgnoredFileSize = nonBinaryFileAccessor.getFileSize();
		nonBinaryFileAccessor.closeIOStream();
		
		filePath = context.getContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, String.class);
		IFileAccessor cleanFileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfs);
		this.cleanFileSize = cleanFileAccessor.getFileSize();
		cleanFileAccessor.closeIOStream();
		
		TocContent content = context.getContextValue(ParseDocumentContextEnum.PARSED_TABLE_OF_CONTENT_DATA, TocContent.class);
		if(content != null)
			for(Entry<String, TocContentDetails> entries : content.getContentDetailsMap().entrySet()){
				String key = entries.getKey();
				
				
				if(key.contains("7") &&  !key.contains("A"))
					this.item7tocDeduced = true;
				else if(key.contains("8"))
					this.item8tocDeduced = true;
			}
		
		this.mdaExtracted = mdaExtracted;
		this.extractionRule = extractionRule;
		this.linesInExtractedData = linesInExtractedData;
	}

	@Override
	public String toString() {
		return  fileName + "," + rawFileSize + "," + binaryIgnoredFileSize
				+ "," + cleanFileSize + "," +  item7tocDeduced + "," + item8tocDeduced
				+ "," + mdaExtracted + "," + extractionRule + "," + linesInExtractedData;
	}

	public String getFileName() {
		return fileName;
	}

	public String getRawFileSize() {
		return rawFileSize;
	}

	public String getCleanFileSize() {
		return cleanFileSize;
	}

	public String getBinaryIgnoredFileSize() {
		return binaryIgnoredFileSize;
	}

	public Boolean getMdaExtracted() {
		return mdaExtracted;
	}

	public Integer getLinesInExtractedData() {
		return linesInExtractedData;
	}

	public Boolean getItem7tocDeduced() {
		return item7tocDeduced;
	}

	public Boolean getItem8tocDeduced() {
		return item8tocDeduced;
	}

	public String getExtractionRule() {
		return extractionRule;
	}
}
