package lab.fourth

import lab.Common.getLinesFromFile

class Grammar(terminals: Set[String],
              nonTerminals: Set[String],
              axiom: String,
              rules: Map[String, Map[Int, List[String]]]) {

}

object Grammar {
//  def initFromFile(filePath: String): Grammar = {
//    val lines = getLinesFromFile(filePath)
//
//    val axiom = lines.head
//    val terminals = lines(1).split("\\s+").toSet
//    lines.drop(2).foldLeft((List[String](), Set[String]())) { (acc, line) =>
//      val (nonTerminalLines, nonTerminals) = acc
//      val parts = line.split(":")
//      val nonTerminal = parts.head
//    }
//  }
}
