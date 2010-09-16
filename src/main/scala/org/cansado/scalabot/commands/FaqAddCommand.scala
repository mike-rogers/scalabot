package org.cansado.scalabot.commands

import java.sql._

class FaqAddCommand extends JdbcCommand {
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

    val value:String = try {
      context.args.tail.mkString(" ")
    } catch {
      case _ => { context.bot.sendMessage(context.channel, "you suck. you need a message to add to the faq"); null }
    }

    if (value == null) { context.connection.close(); return "error" }

    try {
      val statement:PreparedStatement = context.connection.prepareStatement("insert into scalabot_faq (`key`, `value`, `channel`) values (?, ?, ?)")
      statement.setString(1, key)
      statement.setString(2, value)
      statement.setString(3, context.channel)

      statement.executeUpdate()
      context.bot.sendMessage(context.channel, key + " -- added to the faq")

      statement.close()
    } catch {
      case _ => context.bot.sendMessage(context.channel, "you suck. try adding something less stupid")
    } finally {
      context.connection.close()
    }

    "success"
  }
}
