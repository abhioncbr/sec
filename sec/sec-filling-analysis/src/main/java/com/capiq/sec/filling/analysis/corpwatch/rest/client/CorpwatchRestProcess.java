package com.capiq.sec.filling.analysis.corpwatch.rest.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.capiq.sec.filling.analysis.corpwatch.rest.model.JsonResponseCik;
import com.capiq.sec.filling.analysis.corpwatch.rest.model.JsonResponseFilings;
import com.capiq.sec.filling.analysis.corpwatch.rest.util.JsonResponseToDomainModel;
import com.capiq.sec.filling.analysis.model.CompanyDomainData;
import com.capiq.sec.filling.analysis.model.FilingDomainData;

public class CorpwatchRestProcess {
	
	private CorpwatchRestJsonClient jsonClient= new CorpwatchRestJsonClient();
	
	public Map<String, List<CompanyDomainData>> getCikCompanyInfo(List<String> cikList){
		Map<String, List<CompanyDomainData>>  cikInfoMap = new HashMap<String, List<CompanyDomainData>>();
		
		for(String cik: cikList){
			JsonResponseCik response = jsonClient.getCik(cik);
			cikInfoMap.put(cik, JsonResponseToDomainModel.getCompanyDomainData(response));
		}
		
		return cikInfoMap;
	}
	
	public Map<String, List<FilingDomainData>> getSecFilingInfo(List<String> cikList){
		return getSecFilingInfo(getCikCompanyInfo(cikList));
	}
	
	public Map<String, List<FilingDomainData>> getSecFilingInfo(Map<String, List<CompanyDomainData>>  cikInfoMap){
		Map<String, List<FilingDomainData>>  secFilingInfoMap = new HashMap<String, List<FilingDomainData>>();
		
		for (Entry<String, List<CompanyDomainData>> entry : cikInfoMap.entrySet()) {
			List<CompanyDomainData> info = entry.getValue();

			if (info != null)
				for (CompanyDomainData companyEntry : info) {
					List<FilingDomainData> filingList = new LinkedList<FilingDomainData>();
					
					String cwId = companyEntry.getCw_id();
					Integer startYear = companyEntry.getMin_year() != null ? Integer.valueOf(companyEntry.getMin_year()) : null;
					Integer endYear = companyEntry.getMax_year() != null ? Integer. valueOf(companyEntry.getMax_year()) : null;
					
					if(cwId != null && startYear != null && endYear != null) {
						for(Integer i=startYear; i<=endYear; i++){
							JsonResponseFilings response = jsonClient.getFiling(i.toString(), cwId);
							filingList.addAll(JsonResponseToDomainModel.getFilingData(response));
						}
						secFilingInfoMap.put(entry.getKey(), filingList);
					}
					
				}
			else {
				//TODO: when company Info not present.
			}
		}
		
		return secFilingInfoMap;
	}

}
