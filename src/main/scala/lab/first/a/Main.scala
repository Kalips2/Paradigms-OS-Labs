package lab.first.a

import lab.Common.LabResult

object Main {
  private val TEST_FOLDER = "src/main/resources/first/a/tests"

  def main(args: Array[String]): Unit = {
    val largestElementsPerFile = LargestElementsFinder findLargestElements TEST_FOLDER
    largestElementsPerFile.foreach(res => {
      val LabResult(testFile, n, largest) = res
      println(s"$n largest elements in the $testFile")
      println(largest)
    })
  }
}
