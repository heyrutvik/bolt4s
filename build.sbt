import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := "bolt4s",
    description := "Neo4j Bolt driver for Scala",
    scalaVersion := "2.13.4",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      fs2Core,
      fs2Io,
      catsCore,
      catsEffect,
      scodecBits,
      scodecCore
    ))
