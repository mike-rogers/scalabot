package org.cansado.scalabot.commands

import java.sql._

class FaqDeleteCommand extends JdbcCommand {
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

    if (key == null) { context.connection.close(); return "error" }

    try {
      val statement:PreparedStatement = context.connection.prepareStatement("delete from scalabot_faq where `key` = ?")
      statement.setString(1, key)
      statement.executeUpdate()

      context.bot.sendMessage(context.channel, key + " removed from the faq")

      statement.close()
    } catch {
      case _ => context.bot.sendMessage(context.channel, "you suck. send a less stupid argument")
    } finally {
      context.connection.close()
    }

    "success"
  }
}
