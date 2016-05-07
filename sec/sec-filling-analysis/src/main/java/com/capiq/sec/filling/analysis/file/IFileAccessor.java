package com.capiq.sec.filling.analysis.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface IFileAccessor {
	public static final String UTF8_ENCODING = "UTF-8";
	
	public String getFileSize() throws IOException;
	public void closeIOStream() throws IOException;
	public boolean isFileExist() throws IOException;
	public PrintWriter getPrintWriter() throws IOException;
	public OutputStream getOutputStream() throws IOException;
	public BufferedReader getBufferedReader() throws IOException;
	public InputStreamReader getInputStreamReader() throws IOException;
	
	public void copyFileToLocal(String destFile) throws IOException;
	public void copyFileToHDFS(String sourceFile) throws IOException;
}
