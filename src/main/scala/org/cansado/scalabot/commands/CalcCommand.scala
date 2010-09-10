package org.cansado.scalabot.commands

class CalcCommand extends ArgumentsCommand {

  def execute(args: Array[String]): String = {
    var stack = new scala.collection.mutable.Stack[Double]
    while (true)  {
      try  {
	for (val token <- args)  {
	  var x = 0.;  var y = 0.
	  try  { x = java.lang.Double.parseDouble(token) }
	  catch  { case _ =>  {
            y = stack.pop;  x = stack.pop
            token match {case "+" => x += y  case "-" => x -= y
                      case "*" => x *= y  case "/" => x /= y
                      case _ => throw new Error } } }
	  stack.push(x)
	}
	if (stack.length > 1) throw new Error
	if (!stack.isEmpty) return stack.pop.toString
      }

      catch  {case _ => { return "error" } }
    }

    return "error"
  }
}
