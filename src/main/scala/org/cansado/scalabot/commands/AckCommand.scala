package org.cansado.scalabot.commands

import java.sql._

class AckCommand extends JdbcCommand {
  def execute(context:CommandContext, connection:Connection):String = {
    if (context.connection == null) {
      context.bot.sendMessage(context.channel, "database not configured. try again, idiot")
      return "idiot"
    }

    try {
      val statement:PreparedStatement = context.connection.prepareStatement("delete from scalabot_tell where `user` = ? and `channel` = ?")
      statement.setString(1, context.speaker)
      statement.setString(2, context.channel)

      statement.executeUpdate()
      context.bot.sendMessage(context.channel, "messages purged")

      statement.close()
    } catch {
      case _ => context.bot.sendMessage(context.channel, "you suck. send a less stupid argument")
    } finally {
      context.connection.close()
    }

    "success"
  }
}
