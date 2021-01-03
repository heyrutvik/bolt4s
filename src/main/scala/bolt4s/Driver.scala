package bolt4s

import java.net.InetSocketAddress

import cats.data.OptionT
import cats.effect.{Blocker, Concurrent, ContextShift, ExitCode, IO, IOApp, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import fs2.Chunk
import fs2.io.tcp.SocketGroup
import scodec.Codec

import scala.concurrent.duration._

object Driver extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = runF[IO]

  def runF[F[_]: Concurrent: ContextShift]: F[ExitCode] =
    for {
      _ <- Sync[F].delay(println(bolt4sArt))
      _ <- connect[F]
    } yield ExitCode.Success

  def connect[F[_]: Concurrent: ContextShift]: F[Unit] =
    Blocker[F].use { blocker =>
      SocketGroup[F](blocker).use { socketGroup =>
        client[F](socketGroup)
      }
    }

  def client[F[_]: Concurrent: ContextShift](socketGroup: SocketGroup): F[Unit] =
    socketGroup.client(new InetSocketAddress("localhost", port)).use { socket =>
      for {
        _ <- socket.write(Chunk.bytes(id.toByteArray))
        _ <- socket.write(Chunk.bytes(Version.versions.map(v => Codec.encode(v).require.toByteArray).reduce(_ ++ _)))
        version <- OptionT(socket.readN(4, Some(30.seconds)))
          .map(c => Codec.decode[Version](c.toBitVector).require.value)
          .value
        _ <- Sync[F].delay(println(s"server responds with version: $version"))
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
