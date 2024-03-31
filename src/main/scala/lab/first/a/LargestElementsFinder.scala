package lab.first.a

import lab.Common.{LabResult, getPrettyFilename, getTestFiles}

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors
import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.util.Using

object LargestElementsFinder {

  def findLargestElements(folder: String): List[LabResult] = {
    val testFiles = getTestFiles(folder)
    testFiles.flatMap(testFilePath => {
      Using(Source.fromFile(testFilePath)) { source =>
        val lines = source.getLines().toList
        val n = lines.head.toInt
        val elements = lines.tail.flatMap(_.split(" ").map(_.toInt))
        val largestElements = findNLargestElements(n, elements)
        Some(LabResult(getPrettyFilename(testFilePath), n, largestElements))
      }
    }.getOrElse(None))
  }


  // to preserve order of elements the same as in input list use indexes
  private def findNLargestElements(n: Int, elements: List[Int]): List[Int] = {
    elements
      .zipWithIndex
      .sortBy(-_._1) // sort by an original value in descending order ('-' indicating reverse order)
      .take(n)
      .sortBy(_._2) // sort by an index in the input list
      .map(_._1)
  }

}