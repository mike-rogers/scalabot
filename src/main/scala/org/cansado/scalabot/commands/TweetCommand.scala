package org.cansado.scalabot.commands

import twitter4j._
import twitter4j.http._
import scala.xml._
import org.cansado.scalabot.twitter._

class TweetCommand(twitterConfig: TwitterConfig) extends ContextCommand {

  def execute(context:CommandContext): String = {
    if (null == twitterConfig.tokenKey || null == twitterConfig.tokenSecret ||
	null == twitterConfig.consumerKey || null == twitterConfig.consumerSecret) {
      context.bot.sendMessage(context.channel, "you need to initialize twitter. please try '%tweet init' for instructions")
      return "error"
    }

    val message = "<" + context.speaker + "> " + context.args.mkString(" ")
    if (message.length > 140) {
      context.bot.sendMessage(context.channel, "you suck: message too long")
    } else {
      context.bot.sendMessage(context.channel, "http://twitter.com/" + twitterConfig.name + "/status/" + update(message))
    }

    "success"
  }

  def update(message: String): String = {
    val twitter:Twitter = new TwitterFactory().getOAuthAuthorizedInstance(twitterConfig.consumerKey, twitterConfig.consumerSecret)
    val accessToken: AccessToken = new AccessToken(twitterConfig.tokenKey, twitterConfig.tokenSecret)
    twitter.setOAuthAccessToken(accessToken)
    val status:Status = twitter.updateStatus(message)
    status.getId().toString()
  }

  
}
