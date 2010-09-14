package org.cansado.scalabot.twitter

import scala.xml._

class TwitterConfig {
  var consumerKey:String = ""
  var consumerSecret:String = ""
  var tokenKey:String = ""
  var tokenSecret: String = ""
  var name: String = ""
}

object TwitterConfig {
  def fromXML(config:Node):TwitterConfig = {
    val twitterConfig:TwitterConfig = new TwitterConfig()
    twitterConfig.consumerKey = (config \ "@consumerKey").toString
    twitterConfig.consumerSecret = (config \ "@consumerSecret").toString
    twitterConfig.tokenKey = (config \ "@tokenKey").toString
    twitterConfig.tokenSecret = (config \ "@tokenSecret").toString
    twitterConfig.name = (config \ "@name").toString

    return twitterConfig    
  }
}
