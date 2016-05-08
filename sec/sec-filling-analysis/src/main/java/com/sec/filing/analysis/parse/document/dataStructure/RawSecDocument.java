package com.sec.filing.analysis.parse.document.dataStructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

import com.sec.filing.analysis.file.FileAccessorUtil;
import com.sec.filing.analysis.file.IFileAccessor;
import com.sec.filing.analysis.parse.document.ParseDocumentContext;
import com.sec.filing.analysis.parse.document.ParseDocumentContextEnum;

public class RawSecDocument implements Iterable<String> {
	private final IFileAccessor fileAccessor;
	
	private Integer lineNumber =0;
	private FileIterator fileIterator;
	private BufferedReader bufferedReader;
	
	public Integer getLineNumber() {
		return lineNumber;
	}

	public RawSecDocument(ParseDocumentContext parseContext, String filePath) throws IOException {
		Boolean isHdfsStorage = parseContext.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
		this.fileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfsStorage);
	}
	
	public void open() throws IOException{
		try {
			bufferedReader = fileAccessor.getBufferedReader();
			lineNumber=0;
			fileIterator = new FileIterator();
		} catch (IOException ex) {
			throw ex;
		}
	}

	public void Close() throws IOException{
		try {
			fileAccessor.closeIOStream();
		} catch (IOException ex) {
			throw ex;
		}
	}

	public Iterator<String> iterator() {
		return fileIterator;
	}
	
	public String getLineContent(int randomLineNumber){
		if(lineNumber< randomLineNumber)
			return fileIterator.readLine(randomLineNumber);
		else
			return null;
	}
	public String getNextLineContent(int randomLineNumber){
		fileIterator.hasNext();
		return fileIterator.next();
		
	}

	private class FileIterator implements Iterator<String> {
		private String _currentLine;

		public boolean hasNext() {
			try {
				_currentLine = bufferedReader.readLine();
				lineNumber++;
			} catch (Exception ex) {
				_currentLine = null;
				ex.printStackTrace();
			}

			return _currentLine != null;
		}

		public String next() {
			return _currentLine;
		}
		
		public String readLine(int randomLineNumber){
			for(int i = lineNumber; i < randomLineNumber; i++){
				hasNext();
			}
			return next();
		}

		public void remove() {
		}
	}
}