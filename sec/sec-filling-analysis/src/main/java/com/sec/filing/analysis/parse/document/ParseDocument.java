package com.sec.filing.analysis.parse.document;

import java.io.IOException;

import com.sec.filing.analysis.common.util.PropertiesUtil;
import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.file.FileAccessorUtil;
import com.sec.filing.analysis.file.IFileAccessor;
import com.sec.filing.analysis.parse.document.dataStructure.RawSecDocument;
import com.sec.filing.analysis.parse.preprocess.rule.CleanTextPreProcessor;
import com.sec.filing.analysis.parse.preprocess.rule.ContentTableJflexPreProcessor;
import com.sec.filing.analysis.parse.preprocess.rule.JflexBinaryFileExtractorPreProcessor;
import com.sec.filing.analysis.parse.preprocess.rule.JflexLexerMarkerPreProcessor;
import com.sec.filing.analysis.parse.preprocess.rule.PageBreakPreProcessor;
import com.sec.filing.analysis.parse.process.TextBasedMdaContentExtractor;
import com.sec.filing.analysis.parse.process.TocBasedMdaContentExtractor;
import com.sec.filing.analysis.parse.stat.IParseStat;
import com.sec.filing.analysis.parse.stat.MdaExtractionStatData;

public class ParseDocument {
	
	public IParseStat parse(String fileName, ParseDocumentContext parseContext) throws SymantecAnalysisGeneralException{
		
		initialiseFilingDoc(fileName, parseContext);
		preprocessFilingDoc(parseContext);
		processFilingDoc(parseContext);
		postprocessFilingDoc(parseContext);
		
		IParseStat parseStat =  parseContext.getContextValue(ParseDocumentContextEnum.IPARSE_STAT , IParseStat.class);
		if(parseStat == null)
			try { parseStat = new MdaExtractionStatData(parseContext, false, 0, "None");
			} catch (IOException e) { new SymantecAnalysisGeneralException(e); }
		return parseStat;
	}
	
	private void initialiseFilingDoc(String fileName, ParseDocumentContext parseContext) throws SymantecAnalysisGeneralException{
		String baseFilePath = null;
		boolean isHdfsStorage = Boolean.parseBoolean(PropertiesUtil.getProperty("file.sec.hdfs.storage"));
		
		if(isHdfsStorage){
			baseFilePath = PropertiesUtil.getProperty("file.sec.hdfs.base.storage.url");
		} else {
			baseFilePath = PropertiesUtil.getProperty("file.sec.local.base.storage.url");
		}
		
		String rawFilepath = baseFilePath + PropertiesUtil.getProperty("file.sec.form-10k.raw.folder") + "/" + fileName;
		String nonBinaryfilepath = baseFilePath + PropertiesUtil.getProperty("file.sec.form-10k.non-binary.folder") + "/" + fileName;
		String cleanFilePath = baseFilePath + PropertiesUtil.getProperty("file.sec.form-10k.clean.folder") + "/" + fileName;
		String mdaFilePath = baseFilePath + PropertiesUtil.getProperty("file.sec.form-10k.mda.folder") + "/" + fileName;
		
		String tempDestFilePath = PropertiesUtil.getProperty("file.sec.form-10k.temp.dest") + "/" + fileName;
		String tempSourceFilePath = PropertiesUtil.getProperty("file.sec.form-10k.temp.source") + "/" + fileName;
		
		try {
			parseContext.setContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, isHdfsStorage);
			parseContext.setContextValue(ParseDocumentContextEnum.FILE_NAME, fileName);
			parseContext.setContextValue(ParseDocumentContextEnum.RAW_DOCUMENT_PATH, rawFilepath);
			parseContext.setContextValue(ParseDocumentContextEnum.NON_BINARY_DOCUMENT_PATH, nonBinaryfilepath);
			parseContext.setContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, cleanFilePath);
			parseContext.setContextValue(ParseDocumentContextEnum.MDA_DOCUMENT_PATH, mdaFilePath);
			parseContext.setContextValue(ParseDocumentContextEnum.TEMP_DEST_FILE_PATH, tempDestFilePath);
			parseContext.setContextValue(ParseDocumentContextEnum.TEMP_SOURCE_FILE_PATH, tempSourceFilePath);
			
			parseContext.setContextValue(ParseDocumentContextEnum.TOC_DEDUCED, false);
			
			Boolean overWriteFiles = Boolean.parseBoolean(PropertiesUtil.getProperty("file.sec.form-10k.overwrite.files"));
			//remove binary files from the 10K filings
			IFileAccessor nonBinaryFileAccessor = FileAccessorUtil.getFileAccesor(nonBinaryfilepath, isHdfsStorage);
			if(overWriteFiles || !nonBinaryFileAccessor.isFileExist())
				new JflexBinaryFileExtractorPreProcessor().execute(parseContext);
			nonBinaryFileAccessor.closeIOStream();
			
			//shell execution step for cleaning documents from HTML tags
			IFileAccessor cleanFileAccessor = FileAccessorUtil.getFileAccesor(cleanFilePath, isHdfsStorage);
			if(overWriteFiles || !cleanFileAccessor.isFileExist())
				new CleanTextPreProcessor().execute(parseContext);
			cleanFileAccessor.closeIOStream();
			
			parseContext.setContextValue(ParseDocumentContextEnum.CLEAN_RAW_SEC_DOCUMENT_OBJECT, new RawSecDocument(parseContext, cleanFilePath));
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}
	
	private void preprocessFilingDoc(ParseDocumentContext parseContext) throws SymantecAnalysisGeneralException{
		new JflexLexerMarkerPreProcessor().execute(parseContext);
		new PageBreakPreProcessor().execute(parseContext);
		new ContentTableJflexPreProcessor().execute(parseContext);
		//new ParseContentTablePreProcessor().execute(parseContext);
	}
	
	private void processFilingDoc(ParseDocumentContext parseContext) throws SymantecAnalysisGeneralException{
		new TextBasedMdaContentExtractor().execute(parseContext);
		new TocBasedMdaContentExtractor().execute(parseContext);
	}
	
	private void postprocessFilingDoc(ParseDocumentContext parseContext) throws SymantecAnalysisGeneralException{
		//new WriteSentimentAnalysisOutput().execute(parseContext);
	}
}
