package com.capiq.sec.filling.analysis.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hdfs.server.namenode.UnsupportedActionException;

public final class FileSystemFileAccessor implements IFileAccessor{
	private final String filePath;
	
	private PrintWriter printWriter = null;
	private OutputStream outputStream = null;
	private BufferedReader bufferedReader = null;
	private InputStreamReader inputStreamReader = null;
	
	public FileSystemFileAccessor(String filePath) throws IOException {
		if(StringUtils.isNotBlank(filePath))
			this.filePath = filePath;
		else throw new IOException("File Path is blank.");
	}
	
	public InputStreamReader getInputStreamReader() throws IOException{
		File file = new File(filePath);
		if(file.exists()) {
			inputStreamReader = new InputStreamReader(new FileInputStream(file));
			return inputStreamReader;
		} else return null;
	}
	
	@Override
	public BufferedReader getBufferedReader() throws IOException {
		File file = new File(filePath);
		if(file.exists()) {
			bufferedReader = new BufferedReader(new FileReader(filePath));
			return bufferedReader;
		} else return null;
	}
	
	public boolean isFileExist() throws IOException{
		File file = new File(filePath);
		if ( file.exists()) {
			return true;
		} else return false;
	}

	public PrintWriter getPrintWriter() throws IOException{
		printWriter = new PrintWriter(filePath, UTF8_ENCODING);
		return printWriter;
	}
	
	public OutputStream getOutputStream() throws IOException {
		outputStream = new FileOutputStream(filePath);
		return outputStream;
	}
	
	public String getFileSize() throws IOException {
		File file = new File(filePath);
		if ( file.exists()) {
			Double bytes = ((double) file.length() / (1024 * 1024));
			return String.format("%.4f", bytes) + " MB";
		}
		return null;
	}

	public void closeIOStream() throws IOException{
		if(inputStreamReader != null){
			inputStreamReader.close();
		}
		
		if(bufferedReader != null){
			bufferedReader.close();
		}
		
		if(printWriter != null){
			printWriter.flush();
			printWriter.close();
		}
		
		if(outputStream != null){
			outputStream.flush();
			outputStream.close();
		}
	}

	public void copyFileToLocal(String destFile) throws IOException {
		throw new UnsupportedActionException("Copy file to local file system is not supported as it is not required.");
	}

	public void copyFileToHDFS(String sourceFile) throws IOException {
		throw new UnsupportedActionException("Copy file to Hdfs is not supported as it is not required.");
	}

}
