package lab.first.b

import lab.Common.LabResult

object Main {
  private val TEST_FOLDER = "src/main/resources/first/b/tests"
  private val NUMBER_POWER_OF = 2

  def main(args: Array[String]): Unit = {
    val largestElementsPerFile = ElementsOnPositionsFinder findElements(TEST_FOLDER, NUMBER_POWER_OF)
    largestElementsPerFile.foreach(res => {
      val LabResult(testFile, n, elements) = res
      println(s"Results of the $testFile")
      println(elements)
    })
  }
}
