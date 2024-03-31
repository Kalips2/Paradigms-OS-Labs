package lab.third

import lab.Common.{getPrettyFilename, getTestFiles}

object Main {
  private val TEST_FOLDER = "src/main/resources/third"

  def main(args: Array[String]): Unit = {
    val testFiles = getTestFiles(TEST_FOLDER)
    testFiles.foreach(testFilePath => {
      val automata = Automata.initFromFile(testFilePath)
      println("Results from " + getPrettyFilename(testFilePath))
      println("Reachable states are: " + automata.reachableStates.mkString(", "))
      println("Unreachable states are: " + automata.unReachableStates.mkString(", "))
    })
  }
}
