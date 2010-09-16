package org.cansado.scalabot

import org.jibble.pircbot.PircBot

import org.cansado.scalabot.commands._, twitter._

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.support.ClassPathXmlApplicationContext

import org.apache.commons.dbcp.PoolingDataSource

import scala.xml._
import scala.collection.mutable.HashMap
import scala.actors.Actor._

class ScalaBot extends PircBot {
  val twitterConfigs = new HashMap[String, TwitterConfig]
  var beanFactory:BeanFactory = null

  def createContext(channel:String, speaker: String, args: Array[String]): CommandContext = {
    val dataSource:PoolingDataSource = if (beanFactory == null) null else 
      beanFactory.getBean("pooledDataSource").asInstanceOf[PoolingDataSource]
    val context: CommandContext = new CommandContext()
    context.channel = channel
    context.speaker = speaker
    context.args = args
    context.bot = this

    if (dataSource != null) {
      try {
	context.connection = dataSource.getConnection()
      } catch {
	case e => { println(e.toString()) }
      }
    }

    return context
  }

  def configure(config: Elem) = {
    setName((config \ "@nick").toString)
    setVerbose(true)
    setEncoding("UTF-8")
    connect((config \ "@url").toString)
    if ((config \ "@identify").toString != "")
      identify((config \ "@identify").toString)

    val channelNodes = config \ "channel"
    channelNodes.foreach { channel =>
      joinChannel((channel \ "@name").toString)
      if ((channel \ "twitter").length == 1) {
	twitterConfigs += (channel \ "@name").toString -> TwitterConfig.fromXML((channel \ "twitter")(0))
      }
    }
  }

  override def onMessage(channel: String, sender: String, login: String, hostname: String, message: String) = {
    val command = message.split(' ')(0)
    val caller = self

    actor {
      command toUpperCase match {
	case "%POM" =>
	  sendMessage(channel, new PomCommand().execute())
	case "%CRIME" =>
	  sendMessage(channel, new CrimeCommand().execute())
	case "%UTIME" =>
	  sendMessage(channel, new UTimeCommand().execute())
	case "%CALC" =>
	  sendMessage(channel, new CalcCommand().execute(message.split(' ').tail))
	case "%ROLL" =>
	  sendMessage(channel, new RollCommand().execute(message.split(' ').tail))
	case "%STOCK" =>
	  sendMessage(channel, new StockCommand().execute(message.split(' ').tail))
	case "%TWEET" =>
	  new TweetCommand(twitterConfigs.get(channel).get).execute(createContext(channel, sender, message.split(' ').tail))
	case "%FAQ" =>
	  new FaqCommand().execute(createContext(channel, sender, message.split(' '). tail))
	case _ =>
      }
    }
  }
}

object ScalaBot {
  def main(args: Array[String]) {
    val factory:BeanFactory = try {
      new ClassPathXmlApplicationContext("spring-context.xml")
    } catch {
      case e: java.lang.Exception => { println(e.toString()); null }
    }

    val bot:ScalaBot = new ScalaBot()
    bot.configure(XML.load("config.xml"))
    bot.beanFactory = factory
  }
}
