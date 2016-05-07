package com.capiq.sec.filling.analysis.parse.stat;


public class Sec10KDownloadStatData implements IParseStat{
	private static final long serialVersionUID = -1098353487233931201L;
	
	private String fileName;
	private String filePath;
	private Boolean downloaded;
	
	public Sec10KDownloadStatData(String fileName, String filePath, Boolean downloaded) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.downloaded = downloaded;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public Boolean getDownloaded() {
		return downloaded;
	}

	@Override
	public String toString() {
		return fileName + "," + filePath + "," + downloaded;
	}
}
