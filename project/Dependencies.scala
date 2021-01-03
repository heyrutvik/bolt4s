import sbt._

object Dependencies {
  private lazy val fs2Version = "2.5.0"
  private lazy val catsVersion = "2.3.1"

  lazy val scalaTest  = "org.scalatest" %% "scalatest"   % "3.2.2"
  lazy val fs2Core    = "co.fs2"        %% "fs2-core"    % fs2Version
  lazy val fs2Io      = "co.fs2"        %% "fs2-io"      % fs2Version
  lazy val catsCore   = "org.typelevel" %% "cats-core"   % catsVersion
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % catsVersion
  lazy val scodecBits = "org.scodec"    %% "scodec-bits" % "1.1.23"
  lazy val scodecCore = "org.scodec"    %% "scodec-core" % "1.11.7"
}
