package com.capiq.sec.filling.analysis.launch;

import java.util.List;
import java.util.Map;

import org.springframework.batch.item.file.FlatFileItemWriter;

import com.capiq.sec.filling.analysis.corpwatch.rest.client.CorpwatchRestProcess;
import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.input.FileInput;
import com.capiq.sec.filling.analysis.model.CompanyDomainData;
import com.capiq.sec.filling.analysis.model.DomainData;
import com.capiq.sec.filling.analysis.model.FilingDomainData;
import com.capiq.sec.filling.analysis.output.ExcelWriter;
import com.capiq.sec.filling.analysis.output.FileOutput;
import com.capiq.sec.filling.common.util.PropertiesUtil;
import com.capiq.sec.filling.common.util.SpringAppContext;

public class CompanyCorpwatchInfoLaunch {
	

	public static void main(String args[]) {
		CompanyCorpwatchInfoLaunch launch = new CompanyCorpwatchInfoLaunch();
		
		try {
			SpringAppContext.loadApplicationContext();
			
			String inputFile =	PropertiesUtil.getProperty("file.cik.input");
			List<String> cikList = FileInput.readFile(inputFile);
			CorpwatchRestProcess process = new CorpwatchRestProcess();

			Map<String, List<CompanyDomainData>> cikInfoMap = process.getCikCompanyInfo(cikList);
			Map<String, List<FilingDomainData>> secFilingMap = process.getSecFilingInfo(cikInfoMap);

			launch.writeCsvDataFiles(cikInfoMap, secFilingMap);
			launch.writeExcelDataFiles(cikInfoMap, secFilingMap);

			SpringAppContext.closeApplicationContext();
		} catch (SymantecAnalysisGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void writeCsvDataFiles(Map<String, List<CompanyDomainData>> cikInfoMap,
									Map<String, List<FilingDomainData>> secFilingMap) throws SymantecAnalysisGeneralException {
		FlatFileItemWriter<CompanyDomainData> companyInfofileWriter = SpringAppContext.getBean("cikCompanyInfo");
		FlatFileItemWriter<FilingDomainData> secFilingfileWriter = SpringAppContext.getBean("secFilingInfo");
		
		// FileOut
		FileOutput fileOutput = new FileOutput();
		fileOutput.writeCompanyInfoCSV(cikInfoMap, companyInfofileWriter);
		fileOutput.writeSecFilingInfoCSV(secFilingMap, secFilingfileWriter);
	}
	
	private void writeExcelDataFiles(Map<String, List<CompanyDomainData>> cikInfoMap,
					Map<String, List<FilingDomainData>> secFilingMap) throws SymantecAnalysisGeneralException {
		ExcelWriter<? extends DomainData> excelWriter = SpringAppContext.getBean("excelDataWriter");
		
		// FileOut
		FileOutput fileOutput = new FileOutput();
		fileOutput.writeExcelFile(cikInfoMap, secFilingMap, excelWriter);
	}

}
