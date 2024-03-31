package lab.third

import lab.Common.getLinesFromFile

import scala.annotation.tailrec

class Automata(statesSize: Int,
               startState: Int,
               transitions: Map[Int, Map[Char, Set[Int]]]) {

  private lazy val allStates: Set[Int] = getAllStates
  lazy val reachableStates: Set[Int] = getReachedStates
  lazy val unReachableStates: Set[Int] = getUnreachedStates

  private def getAllStates: Set[Int] = {
    @tailrec
    def generateStates(current: Int, acc: Set[Int]): Set[Int] = {
      if (current > statesSize - 1)
        acc
      else
        generateStates(current + 1, acc + current)
    }

    generateStates(0, Set.empty)
  }

  private def getReachedStates: Set[Int] = {
    @tailrec
    def loop(reachableStates: Set[Int]): Set[Int] = {
      // calculate such q's: {q | q ∈ δ(q_j, a), q_j ∈ reachableStates, a ∈ Σ }
      val reachableStatesFromCurrentStates = reachableStates
        .flatMap(state => transitions.getOrElse(state, Map()).values).flatten
      // Q_i = Q_(i-1) + q's
      val mergedReachableStates = reachableStates ++ reachableStatesFromCurrentStates

      // condition of exit from iterations
      if (mergedReachableStates == reachableStates && mergedReachableStates.nonEmpty)
        mergedReachableStates
      else
        loop(mergedReachableStates)
    }

    // start iterations from q_0.
    loop(Set(startState))
  }

  private def getUnreachedStates: Set[Int] = {
    allStates -- reachableStates
  }
}

object Automata {
  def initFromFile(filePath: String): Automata = {
    val lines = getLinesFromFile(filePath)
    val statesSize = lines(0).toInt
    val startState = lines(1).toInt

    val transitions = lines.drop(2)
      .map { line =>
        val Array(fromState, transition, toState) = line.split(" ")
        (fromState.toInt, transition.head, toState.toInt)
      }
      .groupBy(_._1).mapValues { stateTransitions =>
        stateTransitions.map { case (_, symbol, nextState) => (symbol, nextState) }
          .groupBy(_._1).mapValues {
            stateTransition =>
              stateTransition.map(_._2).toSet
          }.toMap
      }.toMap

    new Automata(statesSize, startState, transitions)
  }
}
