package org.cansado.scalabot.commands

import java.util.Random

class RollCommand extends ArgumentsCommand {

  def execute(args: Array[String]): String = {
    val extractor = """^(\d+)d(\d+)$""".r
    val random = new Random((new java.util.Date()).getTime())

    var dieValue = args(0)

    dieValue match {
      case extractor(count, side) => {
	var retvalue = 0
	if (count.toInt > 1000) return "now you're just being a dick"
	for (i <- 1 to count.toInt) {
	  retvalue += (random.nextInt(side.toInt) + 1)
	}
        return retvalue.toString
      }
      case _ => "error"
    }

    return "error"
  }
}
