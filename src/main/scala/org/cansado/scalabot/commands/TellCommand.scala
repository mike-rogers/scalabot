package org.cansado.scalabot.commands

import java.sql._

class TellCommand extends JdbcCommand {
  def execute(context:CommandContext, connection:Connection):String = {
    if (context.connection == null) {
      context.bot.sendMessage(context.channel, "database not configured. try again, idiot")
      return "idiot"
    }

    val user:String = try {
      context.args(0)
    } catch {
      case _ => { context.bot.sendMessage(context.channel, "you suck. try sending some arguments"); null }
    }

    if (user == null) { context.connection.close(); return "error" }

    if ((false /: context.bot.getUsers(context.channel)) { (ret, elem) => ret || (elem.getNick() == user) }) {
      context.bot.sendMessage(context.channel, "feh, that user is currently online. send your own message.")
      return "lazybastard"
    }

    val message:String = try {
      context.args.tail.mkString(" ")
    } catch {
      case _ => { context.bot.sendMessage(context.channel, "you suck. try sending a message"); null }
    }

    if (message == null) { context.connection.close(); return "error" }

    try {
      val statement:PreparedStatement = context.connection.prepareStatement("insert into scalabot_tell (`user`, `channel`, `message`, `from`) values (?, ?, ?, ?)")
      statement.setString(1, user)
      statement.setString(2, context.channel)
      statement.setString(3, message)
      statement.setString(4, context.speaker)

      statement.executeUpdate()
      context.bot.sendMessage(context.channel, "message stored for " + user)

      statement.close()
    } catch {
      case _ => context.bot.sendMessage(context.channel, "you suck. send a less stupid argument")
    } finally {
      context.connection.close()
    }

    "success"
  }

  def checkForMessages(context:CommandContext) = {
    try {
      val statement:PreparedStatement = context.connection.prepareStatement("select `channel`, `from`, `message` from scalabot_tell where `user` = ?")
      statement.setString(1, context.speaker)

      val results:ResultSet = statement.executeQuery()
      var messagesFound:Boolean = false;
      while (results.next()) {
	val channel:String = results.getString(1)
	val speaker:String = results.getString(2)
	val message:String = results.getString(3)
	context.bot.sendMessage(context.speaker, "(" + channel + ") <" + speaker + "> " + message)
	messagesFound = true
      }

      if (messagesFound) {
	context.bot.sendMessage(context.speaker, "to purge the messages, send '%ack' in the channel the messages were sent from")
      }

      results.close()
      statement.close()
    } catch {
      case e => println(e.toString())
    } finally {
      context.connection.close()
    }
  }
}
