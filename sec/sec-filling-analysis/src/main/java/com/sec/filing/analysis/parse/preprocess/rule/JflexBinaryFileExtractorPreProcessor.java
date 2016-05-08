package com.sec.filing.analysis.parse.preprocess.rule;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.file.FileAccessorUtil;
import com.sec.filing.analysis.file.IFileAccessor;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.document.ParseDocumentContextEnum;
import com.sec.filing.analysis.parse.jflex.JflexBinaryFilesExtractor;
import com.sec.filing.analysis.parse.jflex.JflexToken;
import com.sec.filing.analysis.parse.jflex.JflexTokenType;
import com.sec.filing.analysis.parse.preprocess.AbstractPreProcess;

public class JflexBinaryFileExtractorPreProcessor extends AbstractPreProcess {
	
	@Override
	public boolean condition(ParseDocumentContext context) {
		String rawFilePath = context.getContextValue(ParseDocumentContextEnum.RAW_DOCUMENT_PATH, String.class);
		if(StringUtils.isBlank(rawFilePath))
			return false;
		
		String nonBinaryFilePath = context.getContextValue(ParseDocumentContextEnum.NON_BINARY_DOCUMENT_PATH, String.class);
		if(StringUtils.isBlank(nonBinaryFilePath))
			return false;

		return true;
	}

	@Override
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException {
		String rawFilePath = context.getContextValue(ParseDocumentContextEnum.RAW_DOCUMENT_PATH, String.class);
		String nonBinaryFilePath = context.getContextValue(ParseDocumentContextEnum.NON_BINARY_DOCUMENT_PATH, String.class);
		
		try {
			StringBuilder builder = performLexing(rawFilePath, context);
		
			Boolean isHdfsStorage = context.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
			IFileAccessor fileAccessor = FileAccessorUtil.getFileAccesor(nonBinaryFilePath, isHdfsStorage);

			PrintWriter writer = fileAccessor.getPrintWriter();
			writer.print(builder.toString());
			fileAccessor.closeIOStream();
				
		} catch (IOException e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}
	
	private StringBuilder performLexing(String filePath, ParseDocumentContext context) throws IOException {
		Boolean isHdfsStorage = context.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
		IFileAccessor fileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfsStorage);
		
		JflexBinaryFilesExtractor lexer = new JflexBinaryFilesExtractor(fileAccessor.getInputStreamReader());
		
		while (true) {
			JflexToken token = lexer.yylex();
			
			JflexTokenType type = token.getTokenType();
			
			if (type == JflexTokenType.EOF) break;
		}
		
		fileAccessor.closeIOStream();
		return lexer.getContent();
	}
	
	public static void main(String[] argsv) throws IOException{
		JflexBinaryFileExtractorPreProcessor proc = new JflexBinaryFileExtractorPreProcessor();
		ParseDocumentContext context = new ParseDocumentContext();
		proc.performLexing(argsv[0], context);
	}
}
