package org.cansado.scalabot.commands

import java.sql._
import scala.actors.Actor._
import org.cansado.scalabot.ScalaBot

class AlertCommand extends JdbcCommand {
  def execute(context:CommandContext, connection:Connection):String = {
    if (context.connection == null) {
      context.bot.sendMessage(context.channel, "database not configured. try again, idiot")
      return "idiot"
    }

    val time:String = try {
      context.args(0)
    } catch {
      case _ => { context.bot.sendMessage(context.channel, "you suck. try %alert <# of minutes> <message>"); null }
    }

    if (time == null) { context.connection.close(); return "error" }

    var longTime:Long = 0

    try {
      longTime = time.toLong
    } catch {
      case _ => { context.bot.sendMessage(context.channel, "you suck. try %alert <# of minutes> <message>"); return "jesus" }
    }

    if (longTime <= 0 || longTime > 10080) { // a week
      context.bot.sendMessage(context.channel, "you suck. i'm not remembering anything for longer than a week.");
      return "toodamnlong"
    }

    val date = new Timestamp((new java.util.Date()).getTime() + (longTime * 60 * 1000))

    val message:String = try {
      context.args.tail.mkString(" ")
    } catch {
      case _ => { context.bot.sendMessage(context.channel, "you suck. try %alert <#of minutes> <message>"); null }
    }

    if (message == null) { context.connection.close(); return "error" }

    try {
      val statement:PreparedStatement = context.connection.prepareStatement("insert into scalabot_alert (`nick`, `when`, `channel`, `message`) values (?, ?, ?, ?)")
      statement.setString(1, context.speaker)
      statement.setTimestamp(2, date)
      statement.setString(3, context.channel)
      statement.setString(4, message)

      statement.executeUpdate()
      context.bot.sendMessage(context.channel, "your alert has been saved")

      createAlert(longTime * 60 * 1000, message, context)

      statement.close()
    } catch {
      case _ => context.bot.sendMessage(context.channel, "you suck. try alerting yourself with something less stupid")
    } finally {
      context.connection.close()
    }

    "success"
  }

  def loadAlerts(connection:Connection, bot:ScalaBot) {
    try {
      var statement:PreparedStatement = connection.prepareStatement("select `nick`, `when`, `channel`, `message` from scalabot_alert")
      var results:ResultSet = statement.executeQuery()

      while (results.next()) {
	val nick = results.getString(1)
	val when = results.getTimestamp(2)
	val channel = results.getString(3)
	val message = results.getString(4)

	val context:CommandContext = new CommandContext()
	context.speaker = nick
	context.bot = bot
	context.channel = channel

	if (when.getTime() <= new java.util.Date().getTime()) {
	  bot.sendMessage(nick, message)
	} else {
	  val longTime:Long = when.getTime() - (new java.util.Date().getTime())
	  createAlert(longTime, message, context)
	}
      }

      results.close()
      statement.close()

      statement = connection.prepareStatement("delete from scalabot_alert where `when` <= now()")
      statement.executeUpdate()
      statement.close()
    } catch {
      case e:Exception => println(e.toString())
    } finally {
      connection.close()
    }
  }

  def createAlert(longTime:Long, message:String, context:CommandContext) {
    actor {
      java.lang.Thread.sleep(longTime)
      context.bot.sendMessage(context.speaker, message)

      val connection = context.bot.createConnection()
      try {
	val statement = connection.prepareStatement("delete from scalabot_alert where `channel` = ? and `nick` = ? and `when` <= now()")
	statement.setString(1, context.channel)
	statement.setString(2, context.speaker)

	statement.executeUpdate()

	statement.close()
      } catch {
	case e:Exception => println(e.toString())
      } finally {
	connection.close()
      }
    }
  }
}
