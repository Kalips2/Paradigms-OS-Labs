package lab.fourth

import lab.Common.getTestFiles

object Main {
  private val TEST_FOLDER = "src/main/resources/fourth"

  def main(args: Array[String]): Unit = {
    val testFiles = getTestFiles(TEST_FOLDER)
    testFiles.foreach(testFilePath => {
      val grammar = Grammar.initFromFile(testFilePath)
      println("Results for " + testFilePath)
      grammar.printFirstMap()
      grammar.printFollowMap()
    })
  }
}
