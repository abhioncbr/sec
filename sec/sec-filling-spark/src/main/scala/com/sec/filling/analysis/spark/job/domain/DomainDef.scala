package com.sec.filling.analysis.spark.job.domain

object DomainDef extends Enumeration {
  type WeekDay = Value
  val ExtractStat = Value
  
  val tName = Map(ExtractStat -> "ExtractStat")
  def getTName(d: DomainDef.Value) = tName.getOrElse(d, d.toString())
}