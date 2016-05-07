package com.sec.filling.analysis.spark.job.util

import com.typesafe.config.ConfigFactory

object AppConf {

  private val confApp = ConfigFactory.load("app")

  val supportedEnv = Set("qa", "prod", "beta")

  def getEnv(envirDescript: String): String = {
    object AppConf {

    }
    val envir =
      getConfStrOrElse(envirDescript, "").trim().toLowerCase()

    if (!supportedEnv.contains(envir)) {
      val errMsg = s"Unssuported enviroument specified in app.conf file: [$envirDescript] = [$envir]; Supported are: " + supportedEnv.mkString("[", ", ", "]")
      throw new Exception(errMsg)
    }
    if (envir.length() > 0) { "-" + envir } else { "-qa" }
  }

  def getConfIntOrElse(key: String, default: Int): Int = {
    try {
      confApp.getInt(key)
    } catch {
      case e: Exception => default
    }
  }

  def getConfLongOrElse(key: String, default: Int): Long = {
    try {
      confApp.getLong(key)
    } catch {
      case e: Exception => default
    }
  }

  def getConfBoolOrElse(key: String, default: Boolean): Boolean = {
    try {
      confApp.getBoolean(key)
    } catch {
      case e: Exception => default
    }
  }

  def getConfStrOrElse(key: String, default: String): String = {
    try {
      confApp.getString(key)
    } catch {
      case e: Exception => default
    }
  }
  def getObj(key: String, default: Object): Object = {
    try {
      confApp.getAnyRef(key)
    } catch {
      case e: Exception => default
    }
  }
}