package com.sec.filling.analysis.corpwatch.rest.model

import spray.json._
case class FilingParameters(limit: Long, index: Long, year: String )
case class CompanyParameters(cik: String, limit: Long, index: Long, year: String )

case class FilingMeta(success: Long, status: Long, status_string: String, total_results: String, results_complete: Long, api_version: String, parameters: FilingParameters) 
case class CompanyMeta(success: Long, status: Long, status_string: String, total_results: String, results_complete: Long, api_version: String, parameters: CompanyParameters) 

case class Company(cik: String, company_name: String, irs_number: String, sic_code:String , industry_name: String, sic_sector: String, sector_name: String, 
                   source_type: String, raw_address: String , country_code: String, subdiv_code: String, top_parent_id: String, num_parents: String, num_children: 
                   String, max_year: String, min_year: String)

case class Filing(cw_id: String, cik: String, year: String, quarter: String, period_of_report: Option[String],
                  filing_date: String, form_10K_url: String, sec_21_url: String, company_is_filer: String) 
                     
case class ResultCompany(companies: Map[String, Company]) 
case class ResultFiling(filings: List[Filing])

case class CikCompany(meta: CompanyMeta, result: ResultCompany)
case class CikFiling(meta: FilingMeta, result: ResultFiling)

object CikCompanyProtocol extends DefaultJsonProtocol {
  implicit val parametersFormat = jsonFormat(CompanyParameters, "cik", "limit", "index", "year")
  
  implicit val metaFormat = jsonFormat(CompanyMeta, "success", "status", "status_string", "total_results", "results_complete", "api_version", "parameters")
                                              
  implicit val companyFormat = jsonFormat(Company, "cik", "company_name", "irs_number", "sic_code", "industry_name", "sic_sector", "sector_name", "source_type",
                                                   "raw_address", "country_code", "subdiv_code", "top_parent_id", "num_parents", "num_children", "max_year", "min_year") 
                                                   
  implicit val resultFormat =  jsonFormat(ResultCompany, "companies")  
  
  implicit val cikCompanyFormat = jsonFormat(CikCompany, "meta", "result")
}

object CikFilingProtocol extends DefaultJsonProtocol {
  implicit val parametersFormat = jsonFormat(FilingParameters, "limit", "index", "year")
  
  implicit val metaFormat = jsonFormat(FilingMeta, "success", "status", "status_string", "total_results", "results_complete", "api_version", "parameters")
                                              
  implicit val filingFormat = jsonFormat(Filing, "cw_id", "cik", "year", "quarter", "period_of_report", "filing_date", "form_10K_url", "sec_21_url", "company_is_filer") 
                                                   
  implicit val resultFormat =  jsonFormat(ResultFiling, "filings")  
  
  implicit val cikFilingFormat = jsonFormat(CikFiling, "meta", "result")
}

object Test extends App {
  import CikCompanyProtocol._
  val obj = CikCompany(CompanyMeta(0L, 0L, "default", "1", 0L, "0.04(2016-02-13)", CompanyParameters("000000", 0L, 0L, "most_recent")), 
                       ResultCompany(Map("cw_id" -> Company("000000", "abc", "000000", "000000", "abc", "abc", "abc", "abc", "xyz", "XX", "xyz", "abc", "abc", "abc", "abc", "abc"))))
  
  val ast = obj.toJson
  println(obj)
  println(ast.prettyPrint)
  println(ast.convertTo[CikCompany])

  import CikFilingProtocol._
  val obj1 = CikFiling(FilingMeta(0L, 0L, "default", "1", 0L, "0.04(2016-02-13)", FilingParameters(0L, 0L, "most_recent")), 
                       ResultFiling(List(Filing("cw_id", "cik", "year", "quarter", Some("period_of_report"), "filing_date", "form_10K_url", "sec_21_url", "company_is_filer"))))
  
  val ast1 = obj1.toJson
  println(obj1)
  println(ast1.prettyPrint)
  println(ast1.convertTo[CikFiling])
 }