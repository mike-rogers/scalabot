package org.cansado.scalabot.commands

import org.cansado.scalabot.yahoo.YahooConfig

import scala.xml._

class SpellCommand(yahooConfig: YahooConfig) extends ArgumentsCommand {

  def execute(args: Array[String]): String = {
    val word = try {
      args(0)
    } catch {
      case _ => return "supply at least one argument. idiot."
    }

    val url = "http://search.yahooapis.com/WebSearchService/V1/spellingSuggestion?appid=" + java.net.URLEncoder.encode(yahooConfig.appId) + "&query=" + word
    val xmlReturn = XML.load(url)
    val resultNodes = xmlReturn \ "Result"

    val retval:String = (("" /: resultNodes) { (retval, currentNode) => retval + " " + currentNode.text }).trim

    return if (retval == "") "no suggestions found" else "suggested spelling(s): " + retval
  }
}

