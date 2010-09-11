package org.cansado.scalabot

import org.jibble.pircbot.PircBot

import org.cansado.scalabot.commands._

import scala.xml._
import scala.actors.Actor._

object ScalaBot extends PircBot {

  def configure(config: Elem) {
    setName((config \ "@nick").toString)
    setVerbose(true)
    setEncoding("UTF-8")
    connect((config \ "@url").toString)
    if ((config \ "@identify").toString != "")
      identify((config \ "@identify").toString)

    val channelNodes = config \ "channel"
    channelNodes.foreach { channel =>
      joinChannel((channel \ "@name").toString)
    }			 
  }

  def main(args: Array[String]) {
    configure(XML.load("config.xml"))
  }

  override def onMessage(channel: String, sender: String, login: String, hostname: String, message: String) {
    val command = message.split(' ')(0)
    val caller = self

    actor {
      command toUpperCase match {
	case "%POM" =>
	  sendMessage(channel, new PomCommand().execute())
	case "%CRIME" =>
	  sendMessage(channel, new CrimeCommand().execute())
	case "%CALC" =>
	  sendMessage(channel, new CalcCommand().execute(message.split(' ').tail))
	case "%ROLL" =>
	  sendMessage(channel, new RollCommand().execute(message.split(' ').tail))
	case _ =>
      }
    }
  }
}
