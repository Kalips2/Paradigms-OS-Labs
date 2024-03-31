package lab.first.b

import lab.Common.{LabResult, getLinesFromFile, getPrettyFilename, getTestFiles}

object ElementsOnPositionsFinder {

  def findElements(folder: String, number: Int): List[LabResult] = {
    val testFiles = getTestFiles(folder)
    testFiles.map(testFilePath => {
      val lines = getLinesFromFile(testFilePath)
      val elements = lines.head.split(" ").map(_.toInt).toList
      val largestElements = findElementsOnPositionsArePowerOf(number, elements)
      LabResult(getPrettyFilename(testFilePath), number, largestElements)
    })
  }

  private def findElementsOnPositionsArePowerOf(number: Int, elements: List[Int]): List[Int] = {
    // get all possible powers of 2
    val powers: List[Int] = Iterator.iterate(1)(_ * number).takeWhile(_ <= elements.length).toList
    // powers <=> indexes within elements
    powers.map(i => elements(i - 1))
  }
}