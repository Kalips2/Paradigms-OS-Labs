package lab

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors
import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.util.Using

object Common {
  case class LabResult(fileName: String, n: Int, elements: List[Int])

  def getPrettyFilename(filePath: String): String = {
    new File(filePath).getName
  }

  def getTestFiles(folder: String): List[String] = {
    val directory = Paths.get(folder)
    Files.list(directory)
      .collect(Collectors.toList())
      .asScala
      .map(_.toString)
      .toList
  }

  def getLinesFromFile(filePath: String): List[String] = {
    Using(Source.fromFile(filePath)) { source =>
      source.getLines().toList
    }.getOrElse(List.empty)
  }

}
