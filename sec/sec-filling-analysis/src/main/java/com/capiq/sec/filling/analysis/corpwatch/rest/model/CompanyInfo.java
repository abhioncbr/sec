package com.capiq.sec.filling.analysis.corpwatch.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class CompanyInfo {
	
	public final String cw_id;
    public final String cik;
    public final String company_name;
    public final String irs_number;
    public final String sic_code;
    public final String industry_name;
    public final String sic_sector;
    public final String sector_name;
    public final String source_type;
    public final String raw_address;
    public final String country_code;
    public final String subdiv_code;
    public final String top_parent_id;
    public final String num_parents;
    public final String num_children;
    public final String max_year;
    public final String min_year;

    @JsonCreator
    public CompanyInfo(@JsonProperty("cw_id") String cw_id, @JsonProperty("cik") String cik, @JsonProperty("company_name") String company_name, @JsonProperty("irs_number") String irs_number, @JsonProperty("sic_code") String sic_code, @JsonProperty("industry_name") String industry_name, @JsonProperty("sic_sector") String sic_sector, @JsonProperty("sector_name") String sector_name, @JsonProperty("source_type") String source_type, @JsonProperty("raw_address") String raw_address, @JsonProperty("country_code") String country_code, @JsonProperty("subdiv_code") String subdiv_code, @JsonProperty("top_parent_id") String top_parent_id, @JsonProperty("num_parents") String num_parents, @JsonProperty("num_children") String num_children, @JsonProperty("max_year") String max_year, @JsonProperty("min_year") String min_year){
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

	public String getCik() {
		return cik;
	}

	public String getCompany_name() {
		return company_name;
	}

	public String getIrs_number() {
		return irs_number;
	}

	public String getSic_code() {
		return sic_code;
	}

	public String getIndustry_name() {
		return industry_name;
	}

	public String getSic_sector() {
		return sic_sector;
	}

	public String getSector_name() {
		return sector_name;
	}

	public String getSource_type() {
		return source_type;
	}

	public String getRaw_address() {
		return raw_address;
	}

	public String getCountry_code() {
		return country_code;
	}

	public String getSubdiv_code() {
		return subdiv_code;
	}

	public String getTop_parent_id() {
		return top_parent_id;
	}

	public String getNum_parents() {
		return num_parents;
	}

	public String getNum_children() {
		return num_children;
	}

	public String getMax_year() {
		return max_year;
	}

	public String getMin_year() {
		return min_year;
	}

	@Override
	public String toString() {
		return "CompanyInfo [cw_id=" + cw_id + ", cik=" + cik
				+ ", company_name=" + company_name + ", irs_number="
				+ irs_number + ", sic_code=" + sic_code + ", industry_name="
				+ industry_name + ", sic_sector=" + sic_sector
				+ ", sector_name=" + sector_name + ", source_type="
				+ source_type + ", raw_address=" + raw_address
				+ ", country_code=" + country_code + ", subdiv_code="
				+ subdiv_code + ", top_parent_id=" + top_parent_id
				+ ", num_parents=" + num_parents + ", num_children="
				+ num_children + ", max_year=" + max_year + ", min_year="
				+ min_year + "]";
	}
}
