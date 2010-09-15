package org.cansado.scalabot.commands

class UTimeCommand extends BaseCommand {
  def execute(): String = {
    return ((new java.util.Date()).getTime() / 1000).toString()
  }
}
