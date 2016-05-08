package com.sec.filing.analysis.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;

import com.sec.filing.analysis.exception.SymantecAnalysisGeneralException;
import com.sec.filing.analysis.model.CompanyDomainData;
import com.sec.filing.analysis.model.DomainData;
import com.sec.filing.analysis.model.FilingDomainData;
import com.sec.filing.analysis.parse.stat.IParseStat;

public class FileOutput {
	
	public void writeParseStatFile(LinkedList<IParseStat> data, FlatFileItemWriter<IParseStat> fileWriter){
		fileWriter.open(new ExecutionContext());
		try {
			fileWriter.write(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileWriter.close();
	}
	
	public void writeCompanyInfoCSV(Map<String, List<CompanyDomainData>> cikInfoMap , 
			FlatFileItemWriter<CompanyDomainData> fileWriter) throws SymantecAnalysisGeneralException{
		List<CompanyDomainData> companyInfoList = writeCompanyInfoCSV(cikInfoMap);
		fileWriter.open(new ExecutionContext());
		
		try {
			fileWriter.write(companyInfoList);
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		}
		
		fileWriter.close();
	}
	
	public void writeSecFilingInfoCSV(Map<String, List<FilingDomainData>> secFilingMap, 
			FlatFileItemWriter<FilingDomainData> fileWriter) throws SymantecAnalysisGeneralException{
		List<FilingDomainData> secFilingList = writeSecFilingInfoCSV(secFilingMap);
		
		fileWriter.open(new ExecutionContext());
		
		try {
			fileWriter.write(secFilingList);
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		}
		
		fileWriter.close();
	}
	
	public void writeExcelFile(Map<String, List<CompanyDomainData>> cikInfoMap , Map<String, List<FilingDomainData>> secFilingMap, 
			ExcelWriter<? extends DomainData> excelFileWriter) throws SymantecAnalysisGeneralException{
		try {
			excelFileWriter.openExcelFile();
			
			List<CompanyDomainData> companyInfoList = writeCompanyInfoCSV(cikInfoMap);
			excelFileWriter.addSheet("Company Info", CompanyDomainData.getDomainDataTitle());
			excelFileWriter.write(companyInfoList);
			
			List<FilingDomainData> secFilingList = writeSecFilingInfoCSV(secFilingMap);
			excelFileWriter.addSheet("Sec Filing Info", FilingDomainData.getDomainDataTitle());
			excelFileWriter.write(secFilingList);
			
			excelFileWriter.closeExcelFile();
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}
	
	public static void writeDataInfile(String filepath, LinkedList<StringBuilder> content) throws SymantecAnalysisGeneralException{
		File file = new File(filepath);

		try{
			if (!file.exists()) {
				file.createNewFile();
			}
	
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(StringBuilder str: content){
				bw.write(str.toString() + System.getProperty("line.separator"));
			}
			
			bw.close();
		} catch (Exception e) {
			throw new SymantecAnalysisGeneralException(e);
		}
	}

	private List<CompanyDomainData>  writeCompanyInfoCSV(Map<String, List<CompanyDomainData>>  cikInfoMap){
		List<CompanyDomainData> companyInfoList= new LinkedList<CompanyDomainData>();
		
		for (Entry<String, List<CompanyDomainData>> entry : cikInfoMap.entrySet()) {
			List<CompanyDomainData> list = entry.getValue();
			companyInfoList.addAll(list);
		}
		
		return companyInfoList;
	}
	
	
	private List<FilingDomainData> writeSecFilingInfoCSV(Map<String, List<FilingDomainData>> secFilingMap) {
		List<FilingDomainData> secFilingInfoList= new LinkedList<FilingDomainData>();
		
		for (Entry<String, List<FilingDomainData>> entry : secFilingMap.entrySet()) {
			List<FilingDomainData> companyFilingsList = entry.getValue();
				secFilingInfoList.addAll(companyFilingsList);
		}
		
		return secFilingInfoList;
	}
	


}
