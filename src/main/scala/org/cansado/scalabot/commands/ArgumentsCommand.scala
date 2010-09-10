package org.cansado.scalabot.commands

trait ArgumentsCommand extends BaseCommand {
  def execute(): String = { execute(Array[String]()) }
  def execute(args: Array[String]): String
}
