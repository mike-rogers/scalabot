package org.cansado.scalabot.commands

class CommandContext {
  var args: Array[String] = Array[String]()
  var speaker: String = ""
  var channel: String = ""
  var bot: org.cansado.scalabot.ScalaBot = null
}
