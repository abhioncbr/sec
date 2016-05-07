package com.sec.filling.analysis.corpwatch.rest.client

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json._
import com.sec.filling.analysis.corpwatch.rest.model.CikFiling
import com.sec.filling.analysis.corpwatch.rest.model.CikCompany
import com.sec.filling.analysis.corpwatch.rest.model.CikCompanyProtocol
import com.sec.filling.analysis.corpwatch.rest.model.CikFilingProtocol

object CorpwatchRestClient {
  private val CORPWATCH_SERVER_URL = "http://api.corpwatch.org/"
	private val CORPWATCH_KEY = "key=11c415c32ccdd0cd89bd2b8c67a76658"
	
	private val CIK_URI = CORPWATCH_SERVER_URL + "companies?cik=918160&" + CORPWATCH_KEY
  private val FILING_URI = CORPWATCH_SERVER_URL + "2014/companies/cw_10007/filings?" + CORPWATCH_KEY
  
  val timeout = 10.seconds
  
  
  def getCik() = {
    println("getting cik")
    
    implicit val system = ActorSystem()  
    import system.dispatcher
 
    import CikCompanyProtocol._
    val pipeline: HttpRequest => Future[CikCompany] = sendReceive ~> unmarshal[CikCompany]
    val response: Future[CikCompany] = pipeline(Get(CIK_URI))
    val cikCompany = Await.result(response, timeout)
    
    println(s"got response $cikCompany")
    system.shutdown()
  }
  
  def getCikFiling() = {
    println("getting cik filing")
    
    implicit val system = ActorSystem()  
    import system.dispatcher

    import CikFilingProtocol._
    val pipeline: HttpRequest => Future[CikFiling] = sendReceive ~> unmarshal[CikFiling]
    val response: Future[CikFiling] = pipeline(Get(FILING_URI))
    val cikFiling = Await.result(response, timeout)

    println(s"got response $cikFiling")
    system.shutdown()
  }
  
  def main(args : Array[String]) {
    println( "Hello World!" )
    getCik()
    getCikFiling()
  }
  
}