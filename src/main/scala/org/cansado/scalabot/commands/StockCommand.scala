package org.cansado.scalabot.commands

class StockCommand extends ArgumentsCommand {

  def execute(args: Array[String]): String = {
    val Extractor = """.*?(\d+\.\d+).*""".r
    val symbol = args(0)
    val url = "http://download.finance.yahoo.com/d/quotes.csv?s=" + java.net.URLEncoder.encode(symbol) + "&f=snk1"
    val dataArray = scala.io.Source.fromURL(url).mkString.split(',')

    var retval = dataArray(0).replaceAll("\"", "") + " (" + dataArray(1).replaceAll("\"", "") + "): "

    dataArray(2).trim match {
      case Extractor(quote) => retval += quote
      case _ => retval += "bad regex parsing ftl"
    }

    return retval
  }
}
