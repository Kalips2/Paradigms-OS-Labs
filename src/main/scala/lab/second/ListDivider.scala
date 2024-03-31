package lab.second

import lab.Common.{LabResult, getLinesFromFile, getPrettyFilename, getTestFiles}

import scala.collection.mutable.ListBuffer

object ListDivider {

  def divideList(folder: String, number: Int): List[List[LabResult]] = {
    val testFiles = getTestFiles(folder)
    testFiles.map(testFilePath => {
      val lines = getLinesFromFile(testFilePath)
      val elements = lines.head.split(" ").map(_.toInt).toList
      val largestElements = divideListByPowersOfNumber(number, elements)
      largestElements.map(elems => LabResult(getPrettyFilename(testFilePath), elems.length, elems))
    })
  }

  /**
   * Creates a sub lists from the incoming list.
   * Elements of created sub lists follow next rules:
   *
   * sub_list_0: element < number ^0^
   * sub_list_1: number ^0^ <= element < number ^1^
   * sub_list_2: number ^1^ <= element < number ^2^
   * sub_list_3: number ^2^ <= element < number ^3^
   * and so on
   *
   * General rule:
   * (-∞, 1), [1, 2), [2, 4), [4, 8), [8, 16), [16, 32) ...
   *
   * */
  private def divideListByPowersOfNumber(number: Int, elements: List[Int]): List[List[Int]] = {
    // determine powers of 2, the length of powers = the number of sub lists.
    val powers = Iterator.iterate(1)(_ * number).takeWhile(_ <= elements.max)
    val sublists = ListBuffer[List[Int]]()

    // corner case: add elements that less than 1 (number ^ 0)
    sublists += elements.filter(_ < 1)

    powers.foreach { power =>
      val sublist = elements.filter(elem => elem >= power && elem < power * number)
      // If we have to skip empty lists, just add here filter
      sublists += sublist
    }

    sublists.toList
  }
}