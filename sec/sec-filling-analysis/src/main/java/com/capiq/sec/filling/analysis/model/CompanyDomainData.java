package com.capiq.sec.filling.analysis.model;

import java.util.LinkedList;
import java.util.List;

import com.capiq.sec.filling.analysis.corpwatch.rest.model.CompanyInfo;

public class CompanyDomainData implements DomainData {

	private String cw_id;
	private String cik;
	private String company_name;
	private String irs_number;
	private String sic_code;
	private String industry_name;
	private String sic_sector;
	private String sector_name;
	private String source_type;
	private String raw_address;
	private String country_code;
	private String subdiv_code;
	private String top_parent_id;
	private String num_parents;
	private String num_children;
	private String max_year;
	private String min_year;
	
	public CompanyDomainData() {
		
	}
	
	public CompanyDomainData(CompanyInfo companyInfoJsonModel) {
		this.cw_id = companyInfoJsonModel.cw_id;
		this.cik = companyInfoJsonModel.cik;
		this.company_name = companyInfoJsonModel.company_name;
		this.irs_number = companyInfoJsonModel.irs_number;
		this.sic_code = companyInfoJsonModel.sic_code;
		this.industry_name = companyInfoJsonModel.industry_name;
		this.sic_sector = companyInfoJsonModel.sic_sector;
		this.sector_name = companyInfoJsonModel.sector_name;
		this.source_type = companyInfoJsonModel.source_type;
		this.raw_address = companyInfoJsonModel.raw_address;
		this.country_code = companyInfoJsonModel.country_code;
		this.subdiv_code = companyInfoJsonModel.subdiv_code;
		this.top_parent_id = companyInfoJsonModel.top_parent_id;
		this.num_parents = companyInfoJsonModel.num_parents;
		this.num_children = companyInfoJsonModel.num_children;
		this.max_year = companyInfoJsonModel.max_year;
		this.min_year = companyInfoJsonModel.min_year;
	}

	public CompanyDomainData(String cw_id, String cik, String company_name,
			String irs_number, String sic_code, String industry_name,
			String sic_sector, String sector_name, String source_type,
			String raw_address, String country_code, String subdiv_code,
			String top_parent_id, String num_parents, String num_children,
			String max_year, String min_year) {
		this.cw_id = cw_id;
		this.cik = cik;
		this.company_name = company_name;
		this.irs_number = irs_number;
		this.sic_code = sic_code;
		this.industry_name = industry_name;
		this.sic_sector = sic_sector;
		this.sector_name = sector_name;
		this.source_type = source_type;
		this.raw_address = raw_address;
		this.country_code = country_code;
		this.subdiv_code = subdiv_code;
		this.top_parent_id = top_parent_id;
		this.num_parents = num_parents;
		this.num_children = num_children;
		this.max_year = max_year;
		this.min_year = min_year;
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

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getIrs_number() {
		return irs_number;
	}

	public void setIrs_number(String irs_number) {
		this.irs_number = irs_number;
	}

	public String getSic_code() {
		return sic_code;
	}

	public void setSic_code(String sic_code) {
		this.sic_code = sic_code;
	}

	public String getIndustry_name() {
		return industry_name;
	}

	public void setIndustry_name(String industry_name) {
		this.industry_name = industry_name;
	}

	public String getSic_sector() {
		return sic_sector;
	}

	public void setSic_sector(String sic_sector) {
		this.sic_sector = sic_sector;
	}

	public String getSector_name() {
		return sector_name;
	}

	public void setSector_name(String sector_name) {
		this.sector_name = sector_name;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

	public String getRaw_address() {
		return raw_address;
	}

	public void setRaw_address(String raw_address) {
		this.raw_address = raw_address;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getSubdiv_code() {
		return subdiv_code;
	}

	public void setSubdiv_code(String subdiv_code) {
		this.subdiv_code = subdiv_code;
	}

	public String getTop_parent_id() {
		return top_parent_id;
	}

	public void setTop_parent_id(String top_parent_id) {
		this.top_parent_id = top_parent_id;
	}

	public String getNum_parents() {
		return num_parents;
	}

	public void setNum_parents(String num_parents) {
		this.num_parents = num_parents;
	}

	public String getNum_children() {
		return num_children;
	}

	public void setNum_children(String num_children) {
		this.num_children = num_children;
	}

	public String getMax_year() {
		return max_year;
	}

	public void setMax_year(String max_year) {
		this.max_year = max_year;
	}

	public String getMin_year() {
		return min_year;
	}

	public void setMin_year(String min_year) {
		this.min_year = min_year;
	}

	@Override
	public List<String> getDomainData() {
		List<String> output = new LinkedList<String>();
		
		output.add(this.cik);
		output.add(this.cw_id);
		output.add(this.company_name);
		output.add(this.irs_number);
		output.add(this.sic_code);
		output.add(this.industry_name);
		output.add(this.sic_sector);
		output.add(this.sector_name);
		output.add(this.source_type);
		output.add(this.raw_address);
		output.add(this.country_code);
		output.add(this.subdiv_code);
		output.add(this.top_parent_id);
		output.add(this.num_parents);
		output.add(this.num_children);
		output.add(this.max_year);
		output.add(this.min_year);
		
		return output;
	}
	
	public static List<String> getDomainDataTitle() {
		List<String> output = new LinkedList<String>();
		
		output.add("CIK");
		output.add("CWID");
		output.add("Company Name");
		output.add("IRS Number");
		output.add("SIC Code");
		output.add("Industry Name");
		output.add("SIC Sector");
		output.add("Sector Name");
		output.add("Source Type");
		output.add("Raw Address");
		output.add("Country Code");
		output.add("Subdiv Code");
		output.add("Top Parent ID");
		output.add("Num Parents");
		output.add("Num Children");
		output.add("Max Year");
		output.add("Min Year");
		
		return output;
	}

}
