package bolt4s

import cats.effect.{ExitCode, IO, IOApp}

object Driver extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    IO.delay(println(bolt4sArt)).map(_ => ExitCode.Success)

  private val bolt4sArt =
    """ _           _ _     ___
      || |         | | |   /   |
      || |__   ___ | | |_ / /| |___
      || '_ \ / _ \| | __/ /_| / __|
      || |_) | (_) | | |_\___  \__ \
      ||_.__/ \___/|_|\__|   |_/___/
      |""".stripMargin

}
