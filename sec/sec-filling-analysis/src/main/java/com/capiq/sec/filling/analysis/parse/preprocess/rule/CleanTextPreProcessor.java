package com.capiq.sec.filling.analysis.parse.preprocess.rule;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.file.FileAccessorUtil;
import com.capiq.sec.filling.analysis.file.IFileAccessor;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContext;
import com.capiq.sec.filling.analysis.parse.document.ParseDocumentContextEnum;
import com.capiq.sec.filling.analysis.parse.preprocess.AbstractPreProcess;
import com.capiq.sec.filling.common.util.PropertiesUtil;
import com.capiq.sec.filling.common.util.SpringAppContext;

public class CleanTextPreProcessor extends AbstractPreProcess {

	@Override
	public boolean condition(ParseDocumentContext context) {
		String nonBinaryInputFilePath = context.getContextValue(ParseDocumentContextEnum.NON_BINARY_DOCUMENT_PATH, String.class);
		if(StringUtils.isBlank(nonBinaryInputFilePath))
			return false;
		
		String cleanDocumentPath = context.getContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, String.class);
		if(StringUtils.isBlank(cleanDocumentPath))
			return false;

		
		return true;
	}

	@Override
	public void process(ParseDocumentContext context) throws SymantecAnalysisGeneralException{
		ExecResult result;
		try {
			
			String cmd= PropertiesUtil.getProperty("shell.script.clean-form-10k.file");
			Resource resource = SpringAppContext.getResource("classpath:" + cmd);
			String path = resource.getURI().getPath();
			
			String dest = null;
			String source = null;
			
			Boolean temp = context.getContextValue(ParseDocumentContextEnum.IS_HDFS_STORAGE, Boolean.class);
		   
			if(temp){
		    	String cleanFilePath = context.getContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, String.class);
		    	String nonBinaryFilePath = context.getContextValue(ParseDocumentContextEnum.NON_BINARY_DOCUMENT_PATH, String.class);
		    	
		    	String tempSourceFilePath = context.getContextValue(ParseDocumentContextEnum.TEMP_SOURCE_FILE_PATH, String.class);
		    	String tempDestFilePath = context.getContextValue(ParseDocumentContextEnum.TEMP_DEST_FILE_PATH, String.class);
		    	
		    	
		    	IFileAccessor accessor = FileAccessorUtil.getFileAccesor(nonBinaryFilePath, temp);
		    	accessor.copyFileToLocal(tempSourceFilePath);
		    	
		    	
		    	source = tempSourceFilePath;
		    	dest = tempDestFilePath;
		    	
		    	execCmd("chmod 777 " + path, 0);
		    	result = execCmd(source, dest, path, 0);
		    	
		    	IFileAccessor accessor1 = FileAccessorUtil.getFileAccesor(cleanFilePath, temp);
		    	accessor1.copyFileToHDFS(tempDestFilePath);
		    	deleteTempFiles(new String[]{tempSourceFilePath,tempDestFilePath});
		    } else {
		    	source = context.getContextValue(ParseDocumentContextEnum.NON_BINARY_DOCUMENT_PATH, String.class);
		    	dest = context.getContextValue(ParseDocumentContextEnum.CLEAN_DOCUMENT_PATH, String.class);
		    	
		    	execCmd("chmod 777 " + path, 0);
		    	result = execCmd(source, dest, resource.getURI().getPath(), 0);
		    }
			
			List<String> lines = result.getLines();
		    for (String line:lines) {
		         System.out.println(line);
		    }
		    
		    
		} catch (Exception ex) {
			throw new SymantecAnalysisGeneralException(ex);
		}
	     
	}
	
	private void deleteTempFiles(String[] filePaths){
		for(String path: filePaths){
			File file = new File(path);
			file.deleteOnExit();
		}
	}

	private ExecResult execCmd(String cmd, int exitValue) throws Exception {
		return execCmd(null,null,cmd,exitValue);
	}
	
	private ExecResult execCmd(String source, String dest, String cmd, int exitValue) throws Exception {
		//initialising command line with shell script
		CommandLine commandLine = CommandLine.parse(cmd);
		
		//adding raw & clean file path as an arguments for shell script execution
		if(source != null) commandLine.addArgument(source);
		if(dest != null) commandLine.addArgument(dest);
		
		//initialising executor for execuIFileAccessortion of script
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(exitValue);
		
		ExecResult result = new ExecResult();
		executor.setStreamHandler(new PumpStreamHandler(result));
		result.setExitCode(executor.execute(commandLine));
		
		return result;
	}
	
	static class ExecResult extends LogOutputStream {
		private int exitCode;

		public int getExitCode() {
			return exitCode;
		}

		public void setExitCode(int exitCode) {
			this.exitCode = exitCode;
		}

		private final List<String> lines = new LinkedList<String>();

		@Override
		protected void processLine(String line, int level) {
			lines.add(line);
		}

		public List<String> getLines() {
			return lines;
		}
	}

}
