package com.evolutiongaming.cassam

import java.nio.file._
import scala.collection.JavaConverters._

import scala.io.Source

object FileLoader {
  case class Entry(path: String, data: String)

  def filesAt(path: String): List[Path] = {
    val root = {
      val clazz = getClass
      val url = clazz.getResource(path)
      val uri = url.toURI
      uri.getScheme match {
        case "jar" =>
          val fs = try {
            FileSystems.getFileSystem(uri)
          } catch {
            case _: FileSystemNotFoundException =>
              FileSystems.newFileSystem(uri, Map.empty[String, AnyRef].asJava)
          }
          fs.getPath(path)

        case _ =>
          Paths.get(uri)
      }
    }
    Files.walk(root, 1).iterator.asScala.filterNot(_ == root).toList
  }

  def entriesAt(path: String): List[Entry] = {
    for {
      file <- filesAt(path)
    } yield {
      val stream = Files.newInputStream(file)
      try {
        val data = Source.fromInputStream(stream).mkString
        Entry(file.toString, data)
      } finally {
        stream.close()
      }
    }
  }
}
