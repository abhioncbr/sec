package com.capiq.sec.filling.analysis.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;

public class FileInput {

	public static List<String> readFile(String fileName) throws SymantecAnalysisGeneralException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			List<String> lineList = new LinkedList<String>();
			String line = br.readLine();

			while (line != null) {
				lineList.add(line);
				line = br.readLine();
			}
			return lineList;
		} catch(IOException exception){
			new SymantecAnalysisGeneralException("IO Exception", exception);
		} finally {
			try {
				br.close();
			} catch (IOException exception) {
				new SymantecAnalysisGeneralException("IO Exception", exception);
			}
		}
		return null;
	}
	
	public static <E> E readExcelFile(int sheetNumber, ExcelReader<E> excelFileReader) throws SymantecAnalysisGeneralException{
		excelFileReader.openExcelFile();
		E output = excelFileReader.read(sheetNumber);
		excelFileReader.closeExcelFile();
		
		return output;
	}

}
