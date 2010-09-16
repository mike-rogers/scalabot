package org.cansado.scalabot.commands

import java.sql.Connection

class CommandContext {
  var args: Array[String] = Array[String]()
  var speaker: String = ""
  var channel: String = ""
  var bot: org.cansado.scalabot.ScalaBot = null
  var connection:Connection = null
}
