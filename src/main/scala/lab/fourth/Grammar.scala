package lab.fourth

import lab.Common.getLinesFromFile

import scala.annotation.tailrec

class Grammar(terminals: Set[String],
              nonTerminals: Set[String],
              axiom: String,
              rules: Map[String, Map[Int, List[String]]]) {
  private val EPSILON = "eps"

  private lazy val first_1: Map[String, Set[String]] = calculateFirst1()
  private lazy val follow_1: Map[String, Set[String]] = calculateFollow1()

  private def calculateFollow1(): Map[String, Set[String]] = {
    var initMap: Map[String, Set[String]] = Map.empty[String, Set[String]]

    // create σ_0
    initMap = initMap.updated(axiom, Set(EPSILON))

    @tailrec
    def loop(followMap: Map[String, Set[String]],
             reachedNonTerminals: Set[String]): (Map[String, Set[String]], Set[String]) = {
      var newFollowMap = followMap
      var newReachedNonTerminals = reachedNonTerminals
      nonTerminals.foreach(nt => {
        reachedNonTerminals.foreach(reachedNt => {
          // A_j -> a A_i b, where in our case A_i - firstLexeme, b - followlexeme
          rules(reachedNt).values.foreach(ntRule => {
            (ntRule :+ EPSILON).sliding(2).foreach(lexemes => {
              val firstLexeme = lexemes.head
              val followLexeme = lexemes.tail
              if (firstLexeme equals nt) {
                // first(b) + follow(A_j)
                val firstB = getSumOfFirstFrom(followLexeme, first_1)
                val followAj = newFollowMap.getOrElse(reachedNt, Set())
                val newTerminals = sum(firstB, followAj)

                newFollowMap = newFollowMap.updated(nt, newFollowMap.getOrElse(nt, Set()) ++ newTerminals)
                newReachedNonTerminals = newReachedNonTerminals + firstLexeme
              }
            })
          })
        })
      })

      if (newFollowMap equals followMap) (newFollowMap, newReachedNonTerminals)
      else loop(newFollowMap, newReachedNonTerminals)
    }

    loop(initMap, Set(axiom))._1
  }

  private def calculateFirst1(): Map[String, Set[String]] = {
    var initMap: Map[String, Set[String]] = Map.empty[String, Set[String]]

    // create F_0 map - додаємо начальні нетермінали у яких є вивід тільки з терміналів
    nonTerminals.foreach(nt => {
      rules(nt).values.foreach(ntRule => {
        if (isRuleWithoutNonTerminals(ntRule)) {
          val oldTerminals = initMap.getOrElse(nt, Set())
          val newTerminals = oldTerminals + ntRule.head
          initMap = initMap.updated(nt, newTerminals)
        }
      })
    })

    // F_i(a) = a
    terminals.foreach(t => initMap = initMap.updated(t, Set(t)))

    @tailrec
    def loop(firstMap: Map[String, Set[String]]): Map[String, Set[String]] = {
      var newFirstMap = firstMap
      nonTerminals.foreach(nt => {
        rules(nt).values.foreach(ntRule => {
          // nt -> a_1 a_2 ... a_n
          // derivedTerminals = F_(i-1)(ntRule) = F_(i-1)(a_1) + ... F_(i-1)(a_n)
          val derivedTerminals = getSumOfFirstFrom(ntRule, firstMap)
          val currentTerminals = newFirstMap.getOrElse(nt, Set())
          // new terminals = terminals_(i-1) ++ derivedTerminals
          val newTerminals = derivedTerminals ++ currentTerminals
          newFirstMap = newFirstMap.updated(nt, newTerminals)
        })
      })

      if (newFirstMap equals firstMap) newFirstMap
      else loop(newFirstMap)
    }

    loop(initMap)
  }

  private def getSumOfFirstFrom(ntRule: List[String], firstMap: Map[String, Set[String]]): Set[String] = {
    if (ntRule.forall(l => firstMap.contains(l) && firstMap(l).nonEmpty)) {
      ntRule.foldLeft(Set[String]()) { (result, nt) => sum(result, firstMap.getOrElse(nt, Set())) }
    } else Set()
  }

  /**
   * Direct sum of length 1.
   * */
  private def sum(f: Set[String], s: Set[String]): Set[String] = {
    if (f.isEmpty) return s
    if (s.isEmpty) return f

    var result: Set[String] = Set.empty
    for {
      str1 <- f
      str2 <- s
    } yield {
      if (str1 == EPSILON) result += str2
      else if (str2 == EPSILON) result += str1
      else result += str1
    }
    result
  }

  private def isTerminal(maybeTerminal: String): Boolean = terminals.contains(maybeTerminal) || maybeTerminal.equals(EPSILON)

  private def isRuleWithoutNonTerminals(rule: List[String]): Boolean = rule.forall(l => isTerminal(l))

  def printFirstMap(): Unit = {
    println("First_1:")
    first_1.keys.toSeq.sorted.foreach { key =>
      if (!isTerminal(key)) println(s"$key: {${first_1(key).toSeq.sorted.mkString(", ")}}")
    }
  }

  def printFollowMap(): Unit = {
    println("Follow_1:")
    follow_1.keys.toSeq.sorted.foreach { key =>
      println(s"$key: {${follow_1(key).toSeq.sorted.mkString(", ")}}")
    }
  }
}

object Grammar {
  def initFromFile(filePath: String): Grammar = {
    val lines = getLinesFromFile(filePath)

    val axiom = lines.head
    val terminals = lines(1).split("\\s+").toSet

    val (nonTerminalLines, nonTerminals) = lines.drop(2).foldLeft((List[String](), List[String]())) { (acc, line) =>
      val (nonTerminalLines, nonTerminals) = acc
      val parts = line.split(":")
      val nonTerminal = parts.head.trim
      val updatedNonTerminals = nonTerminals :+ nonTerminal
      (nonTerminalLines :+ parts(1).trim, updatedNonTerminals)
    }

    val rules = nonTerminalLines.zip(nonTerminals).map { case (ruleLine, nonTerminal) =>
      val rulesList = ruleLine.split("\\|").map(_.trim.split("\\s+").toList)
      val ruleMap = rulesList.zipWithIndex.map { case (rule, index) => index -> rule }.toMap
      nonTerminal -> ruleMap
    }.toMap

    new Grammar(terminals, nonTerminals.toSet, axiom, rules)
  }
}
