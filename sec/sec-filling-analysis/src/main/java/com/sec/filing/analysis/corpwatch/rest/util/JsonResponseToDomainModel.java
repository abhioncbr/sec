package com.sec.filing.analysis.corpwatch.rest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sec.filing.analysis.corpwatch.rest.model.CompanyInfo;
import com.sec.filing.analysis.corpwatch.rest.model.Filing;
import com.sec.filing.analysis.corpwatch.rest.model.JsonResponseCik;
import com.sec.filing.analysis.corpwatch.rest.model.JsonResponseFilings;
import com.sec.filing.analysis.model.CompanyDomainData;
import com.sec.filing.analysis.model.FilingDomainData;

public class JsonResponseToDomainModel {
	
	public static List<CompanyDomainData> getCompanyDomainData(JsonResponseCik jsonResonse){
		List<CompanyDomainData> companyDomainList = new ArrayList<CompanyDomainData>();
		
		Map<String, CompanyInfo> info = jsonResonse.result.companies;

		if (info != null)
			for (Entry<String, CompanyInfo> companyEntry : info.entrySet()) {
				CompanyInfo company = companyEntry.getValue();
				companyDomainList.add(new CompanyDomainData(company));
			}
		else {
			CompanyDomainData domainData = new CompanyDomainData();
			domainData.setCik(jsonResonse.meta.parameters.getCik());
			companyDomainList.add(domainData);
		}
		
		return companyDomainList;
	}
	
	public static List<FilingDomainData> getFilingData(JsonResponseFilings jsonResonse){
		List<FilingDomainData> companyDomainList = new ArrayList<FilingDomainData>();
		
		List<Filing> companyFilingsList = jsonResonse.result.filings;
		if(companyFilingsList != null)
			for(Filing filing: companyFilingsList){
				companyDomainList.add(new FilingDomainData(filing));
			}
		
		return companyDomainList;
	}

}
