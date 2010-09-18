package org.cansado.scalabot.yahoo

import scala.xml._

class YahooConfig {
  var appId: String = ""
}

object YahooConfig {
  def fromXML(config:Node):YahooConfig = {
    val yahooConfig:YahooConfig = new YahooConfig()
    yahooConfig.appId = (config \ "@appId").toString

    return yahooConfig
  }
}
