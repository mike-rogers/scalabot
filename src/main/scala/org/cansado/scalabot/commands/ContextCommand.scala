package org.cansado.scalabot.commands

trait ContextCommand extends BaseCommand {
  def execute(): String = { execute(new CommandContext()) }
  def execute(context: CommandContext): String
}
