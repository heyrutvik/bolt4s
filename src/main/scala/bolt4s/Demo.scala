package bolt4s

import cats.effect.{Concurrent, ContextShift, ExitCode, IO, IOApp, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import scodec.Codec

import scala.concurrent.duration._

object Demo extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = runF[IO]

  def runF[F[_]: Concurrent: ContextShift]: F[ExitCode] =
    for {
      _ <- Sync[F].delay(println(bolt4sArt))
      _ <- handshake[F]
    } yield ExitCode.Success

  def handshake[F[_]: Concurrent: ContextShift]: F[Unit] =
    BitVectorSocket.apply("localhost", port, 10.seconds, 10.seconds).use { socket =>
      for {
        _       <- socket.write(id)
        _       <- socket.write(Version.versions.map(v => Codec.encode(v).require).reduce(_ ++ _))
        version <- socket.read(4).map(c => Codec.decode[Version](c).require.value)
        _       <- Sync[F].delay(println(s"server responded with version: $version"))
      } yield ()
    }

  private val bolt4sArt =
    """ _           _ _     ___
      || |         | | |   /   |
      || |__   ___ | | |_ / /| |___
      || '_ \ / _ \| | __/ /_| / __|
      || |_) | (_) | | |_\___  \__ \
      ||_.__/ \___/|_|\__|   |_/___/
      |""".stripMargin

}
