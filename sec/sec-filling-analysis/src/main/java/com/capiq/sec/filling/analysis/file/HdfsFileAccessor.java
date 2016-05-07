package com.capiq.sec.filling.analysis.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.capiq.sec.filling.common.util.PropertiesUtil;

public final class HdfsFileAccessor implements IFileAccessor{
	private final String filePath;
	
	private FileSystem hdfs = null;
	private PrintWriter printWriter = null;
	private FSDataOutputStream  outputStream = null;
	private BufferedReader bufferedReader = null;
	private InputStreamReader inputStreamReader = null;
	
	public HdfsFileAccessor(String filePath) throws IOException{
		if(StringUtils.isNotBlank(filePath)){
			this.filePath = filePath;
		} else throw new IOException("File Path is blank.");
	}

	public void copyFileToLocal(String destFile) throws IOException {
		Path sourceFilePath = new Path(filePath);
		Path destFilePath = new Path(destFile);

		openHdfsFileSystem();
		hdfs.copyToLocalFile(sourceFilePath, destFilePath);
	}
	
	public void copyFileToHDFS(String sourceFile) throws IOException {
		Path sourceFilePath = new Path(sourceFile);
		Path destFilePath = new Path(filePath);

		openHdfsFileSystem();
		hdfs.copyFromLocalFile(sourceFilePath, destFilePath);
	}
	
	public boolean isFileExist() throws IOException{
		Path file = new Path(filePath);
		openHdfsFileSystem();
		
		if ( hdfs.exists( file )) {
			return true;
		} else return false;
	}
	
	public InputStreamReader getInputStreamReader() throws IOException{
		Path file = new Path(filePath);
		openHdfsFileSystem();
		
		if ( hdfs.exists( file )) { 
			inputStreamReader =  new InputStreamReader(hdfs.open(file));
			return inputStreamReader;
		} else return null;
	}
	
	public BufferedReader getBufferedReader() throws IOException {
		Path file = new Path(filePath);
		openHdfsFileSystem();

		if ( hdfs.exists( file )) { 
			bufferedReader = new BufferedReader(new InputStreamReader(hdfs.open(file)));
			return bufferedReader;
		} else return null;
	}

	public PrintWriter getPrintWriter() throws IOException{
		Path file = new Path(filePath);
		openHdfsFileSystem();

		outputStream = hdfs.create(file);
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter( outputStream, UTF8_ENCODING));
		return printWriter;
	}
	
	public OutputStream getOutputStream() throws IOException {
		Path file = new Path(filePath);
		openHdfsFileSystem();

		outputStream = hdfs.create(file);
		return outputStream;
	}
	
	public String getFileSize() throws IOException {
		Path file = new Path(filePath);
		openHdfsFileSystem();
		
	    ContentSummary cSummary = hdfs.getContentSummary(file);
	    Double bytes = ((double) cSummary.getLength() / (1024 * 1024));
		return String.format("%.4f", bytes) + " MB";
	}

	public void closeIOStream() throws IOException{
		if(this.inputStreamReader != null){
			this.inputStreamReader.close();
		}
		
		if(this.bufferedReader != null){
			this.bufferedReader.close();
		}
		
		if(this.printWriter != null){
			this.printWriter.flush();
			this.printWriter.close();
		}
		
		if(this.outputStream != null){
			this.outputStream.hflush();
			this.outputStream.close();
		}
		
		if(this.hdfs != null) {
			this.hdfs.close();
			this.hdfs = null;
		}
	}
	
	private void openHdfsFileSystem() throws IOException{
		if(hdfs == null){
			this.hdfs = FileSystem.newInstance(getConfiguration());
		} 
	}

	private Configuration getConfiguration(){
		Configuration configuration = new Configuration();
		
		configuration.set("fs.defaultFS", PropertiesUtil.getProperty("file.sec.hdfs.property.defaultFS"));
		
		//configuration.addResource(new Path(PropertiesUtil.getProperty("file.sec.hdfs.property.core-site.conf.path")));
		//configuration.addResource(new Path(PropertiesUtil.getProperty("file.sec.hdfs.property.hdfs-site.conf.path")));

		return configuration;
	}

}
