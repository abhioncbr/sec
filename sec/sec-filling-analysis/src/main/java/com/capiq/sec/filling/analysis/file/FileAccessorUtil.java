package com.capiq.sec.filling.analysis.file;

import java.io.IOException;

public class FileAccessorUtil {
	
	public static synchronized IFileAccessor getFileAccesor(String filePath, Boolean isHdfsStorage) throws IOException{
		if(isHdfsStorage){
			return new HdfsFileAccessor(filePath);
		} else {
			return new FileSystemFileAccessor(filePath);
		}
	}

}
