package lab.second

import lab.Common.LabResult

object Main {
  private val TEST_FOLDER = "src/main/resources/second/tests"
  private val NUMBER_POWER_OF = 2

  def main(args: Array[String]): Unit = {
    val subLists = ListDivider divideList(TEST_FOLDER, NUMBER_POWER_OF)
    subLists.foreach(subList => {
      println(s"Results of the ${subList.last.fileName}")
      subList.foreach(res => {
        val LabResult(_, _, elements) = res
        println(elements)
      })
    })
  }
}
