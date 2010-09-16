package org.cansado.scalabot.commands

import java.sql._

class FaqCommand extends JdbcCommand {
  def execute(context:CommandContext, connection:Connection):String = {
    if (context.connection == null) {
      context.bot.sendMessage(context.channel, "database not configured. try again, idiot")
      return "idiot"
    }

    val key:String = try {
      context.args(0)
    } catch {
      case _ => { context.bot.sendMessage(context.channel, "you suck. try sending one argument"); null }
    }

    if (key == null) return "error"

    try {
      val statement:PreparedStatement = context.connection.prepareStatement("select `value` from scalabot_faq where `key` = ?")
      var result:String = null
      statement.setString(1, key)
      val results:ResultSet = statement.executeQuery()
      while (results.next()) {
	result = results.getString(1)
      }
      if (result == null) {
	context.bot.sendMessage(context.channel, "no faq for " + key)
      } else {
	context.bot.sendMessage(context.channel, result)
      }
    } catch {
      case _ => context.bot.sendMessage(context.channel, "you suck. send a less stupid argument")
    }

    "success"
  }
}
