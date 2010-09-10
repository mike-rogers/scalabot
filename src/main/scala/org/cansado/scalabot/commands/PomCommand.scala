package org.cansado.scalabot.commands

import java.util.Date

class PomCommand extends BaseCommand {
  val EPOCH_MINUS_1970: Double = (20 * 365 + 5 - 1)
  val EPSILONg: Double = 279.403303
  val RHOg : Double = 282.768422
  val ECCEN : Double = 0.016713
  val lzero : Double = 318.351648
  val Pzero : Double = 36.340410
  val Nzero : Double = 318.510107

  def execute(): String = {
    return toString(new Date())
  }

  def toString(date: Date): String = {
    val days: Double = date.getTime / 1000.0 / 86400.0 - EPOCH_MINUS_1970
    var today: Double = potm(days) + 0.5


    var retval: String = "The moon is "

    if (today == 100) retval += "Full"
    else if (today == 0) retval += "New"
    else {
      val tomorrow: Int = potm(days + 1).toInt
      if (today == 50)
	retval += (if (tomorrow > today) "at the First Quarter" else "at the Last Quarter")
      else {
	today -= 0.5
	retval += (if (tomorrow > today) "Waxing " else "Waning ")
	retval += (if (today > 50) "Gibbous " else "Crescent ")
	retval += ("(" + (new java.text.DecimalFormat("#.#")).format(today) + "% of Full)")
      }
    }

    return retval
  }

  def potm(days: Double): Double = {
    var N: Double = 360.0 * days / 365.242191                
    N = adj360(N)
    var Msol: Double = N + EPSILONg - RHOg                
    Msol = adj360(Msol)
    var Ec: Double = 360.0 / java.lang.Math.PI * ECCEN * java.lang.Math.sin(dtor(Msol))        
    var LambdaSol: Double = N + Ec + EPSILONg                
    LambdaSol = adj360(LambdaSol)
    var l: Double = 13.1763966 * days + lzero                
    l = adj360(l)
    var Mm: Double = l - (0.1114041 * days) - Pzero            
    Mm = adj360(Mm)
    var Nm: Double = Nzero - (0.0529539 * days)            
    Nm = adj360(Nm)
    var Ev: Double = 1.2739 * java.lang.Math.sin(dtor(2*(l - LambdaSol) - Mm))    
    var Ac: Double = 0.1858 * java.lang.Math.sin(dtor(Msol))                
    var A3: Double = 0.37 * java.lang.Math.sin(dtor(Msol))
    var Mmprime: Double = Mm + Ev - Ac - A3                
    Ec = 6.2886 * java.lang.Math.sin(dtor(Mmprime))            
    var A4: Double = 0.214 * java.lang.Math.sin(dtor(2 * Mmprime))            
    var lprime: Double = l + Ev + Ec - Ac + A4                
    var V: Double = 0.6583 * java.lang.Math.sin(dtor(2 * (lprime - LambdaSol)))    
    var ldprime: Double = lprime + V                    
    var D: Double = ldprime - LambdaSol                
    50.0 * (1 - java.lang.Math.cos(dtor(D)))            
  }

  def dtor(deg: Double): Double = {
    deg * java.lang.Math.PI / 180.0
  }

  def adj360(deg: Double): Double = {
    var answer = deg

    while (answer < 0) {
      answer += 360
    }

    while (answer > 360) {
      answer -= 360
    }

    return answer
  }
}
