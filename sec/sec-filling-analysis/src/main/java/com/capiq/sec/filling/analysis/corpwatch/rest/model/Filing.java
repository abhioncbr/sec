package com.capiq.sec.filling.analysis.corpwatch.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Filing {

	public final String cw_id;
	public final String cik;
	public final String year;
	public final String quarter;
	public final String period_of_report;
	public final String filing_date;
	public final String form_10K_url;
	public final String sec_21_url;
	public final String company_is_filer;

	@JsonCreator
	public Filing(@JsonProperty("cw_id") String cw_id,
			@JsonProperty("cik") String cik, @JsonProperty("year") String year,
			@JsonProperty("quarter") String quarter,
			@JsonProperty("period_of_report") String period_of_report,
			@JsonProperty("filing_date") String filing_date,
			@JsonProperty("form_10K_url") String form_10K_url,
			@JsonProperty("sec_21_url") String sec_21_url,
			@JsonProperty("company_is_filer") String company_is_filer) {
		this.cw_id = cw_id;
		this.cik = cik;
		this.year = year;
		this.quarter = quarter;
		this.period_of_report = period_of_report;
		this.filing_date = filing_date;
		this.form_10K_url = form_10K_url;
		this.sec_21_url = sec_21_url;
		this.company_is_filer = company_is_filer;
	}

	public String getCw_id() {
		return cw_id;
	}

	public String getCik() {
		return cik;
	}

	public String getYear() {
		return year;
	}

	public String getQuarter() {
		return quarter;
	}

	public String getPeriod_of_report() {
		return period_of_report;
	}

	public String getFiling_date() {
		return filing_date;
	}

	public String getForm_10K_url() {
		return form_10K_url;
	}

	public String getSec_21_url() {
		return sec_21_url;
	}

	public String getCompany_is_filer() {
		return company_is_filer;
	}

	@Override
	public String toString() {
		return "Filing [cw_id=" + cw_id + ", cik=" + cik + ", year=" + year
				+ ", quarter=" + quarter + ", period_of_report="
				+ period_of_report + ", filing_date=" + filing_date
				+ ", form_10K_url=" + form_10K_url + ", sec_21_url="
				+ sec_21_url + ", company_is_filer=" + company_is_filer + "]";
	}
}