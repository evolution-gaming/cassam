package com.evolutiongaming.cassam

import org.scalatest.{FreeSpec, Matchers}

class FileLoaderSpec extends FreeSpec with Matchers {
  "FileLoader" - {
    "loads a list of file names from /test-resources" in {
      val paths = FileLoader.filesAt("/test-resources")
      paths.size shouldBe 2
      paths.head.toString should endWith("test_file#1.txt")
      paths.tail.head.toString should endWith("test_file#2.txt")
    }

    "load file data from /test-resources" in {
      val entries = FileLoader.entriesAt("/test-resources")
      entries.size shouldBe 2
      entries.head.path should endWith("test_file#1.txt")
      entries.head.data shouldBe "Just a test file #1"
    }
  }
}
