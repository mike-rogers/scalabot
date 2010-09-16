package org.cansado.scalabot.commands

import java.sql.Connection

trait JdbcCommand extends ContextCommand {
  def execute(context: CommandContext): String = { execute(context, null) }
  def execute(context: CommandContext, connection: Connection): String
}
