package com.capiq.sec.filling.analysis.model;

import java.util.LinkedList;
import java.util.List;

import com.capiq.sec.filling.analysis.corpwatch.rest.model.Filing;

public class FilingDomainData implements DomainData {
	
	private String cw_id;
	private String cik;
	private String year;
	private String quarter;
	private String period_of_report;
	private String filing_date;
	private String form_10K_url;
	private String sec_21_url;
	private String company_is_filer;
	
	public FilingDomainData() {
	}

	public FilingDomainData(Filing filingJsonModel) {
		this.cw_id = filingJsonModel.cw_id;
		this.cik = filingJsonModel.cik;
		this.year = filingJsonModel.year;
		this.quarter = filingJsonModel.quarter;
		this.period_of_report = filingJsonModel.period_of_report;
		this.filing_date = filingJsonModel.filing_date;
		this.form_10K_url = filingJsonModel.form_10K_url;
		this.sec_21_url = filingJsonModel.sec_21_url;
		this.company_is_filer = filingJsonModel.company_is_filer;
	}

	public FilingDomainData(String cw_id, String cik, String year,
			String quarter, String period_of_report, String filing_date,
			String form_10k_url, String sec_21_url, String company_is_filer) {
		this.cw_id = cw_id;
		this.cik = cik;
		this.year = year;
		this.quarter = quarter;
		this.period_of_report = period_of_report;
		this.filing_date = filing_date;
		this.form_10K_url = form_10k_url;
		this.sec_21_url = sec_21_url;
		this.company_is_filer = company_is_filer;
	}
	
	public String getCw_id() {
		return cw_id;
	}

	public void setCw_id(String cw_id) {
		this.cw_id = cw_id;
	}

	public String getCik() {
		return cik;
	}

	public void setCik(String cik) {
		this.cik = cik;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getPeriod_of_report() {
		return period_of_report;
	}

	public void setPeriod_of_report(String period_of_report) {
		this.period_of_report = period_of_report;
	}

	public String getFiling_date() {
		return filing_date;
	}

	public void setFiling_date(String filing_date) {
		this.filing_date = filing_date;
	}

	public String getForm_10K_url() {
		return form_10K_url;
	}

	public void setForm_10K_url(String form_10k_url) {
		form_10K_url = form_10k_url;
	}

	public String getSec_21_url() {
		return sec_21_url;
	}

	public void setSec_21_url(String sec_21_url) {
		this.sec_21_url = sec_21_url;
	}

	public String getCompany_is_filer() {
		return company_is_filer;
	}

	public void setCompany_is_filer(String company_is_filer) {
		this.company_is_filer = company_is_filer;
	}

	@Override
	public List<String> getDomainData() {
		List<String> output = new LinkedList<String>();
		
		output.add(this.cik);
		output.add(this.cw_id);
		output.add(this.year);
		output.add(this.quarter);
		output.add(this.period_of_report);
		output.add(this.filing_date);
		output.add(this.form_10K_url);
		output.add(this.sec_21_url);
		output.add(this.company_is_filer);
		
		return output;
	}

	public static List<String> getDomainDataTitle() {
		List<String> output = new LinkedList<String>();
		
		output.add("CIK");
		output.add("CWID");
		output.add("Year");
		output.add("Quarter");
		output.add("Period Of Report");
		output.add("Filing Date");
		output.add("Form 10K URL");
		output.add("Sec 21 URL");
		output.add("Company Is Filter");
		
		return output;
	}
}
