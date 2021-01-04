package bolt4s

import java.net.InetSocketAddress

import cats.MonadError
import cats.effect.{Blocker, Concurrent, ContextShift, Resource}
import cats.syntax.applicative._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import fs2.Chunk
import fs2.io.tcp.{Socket, SocketGroup}
import scodec.bits.BitVector

import scala.concurrent.duration.FiniteDuration

trait BitVectorSocket[F[_]] {
  def read(nBytes: Int): F[BitVector]
  def write(bits: BitVector): F[Unit]
}

object BitVectorSocket {

  def apply[F[_]: Concurrent: ContextShift](
      host: String,
      port: Int,
      readTimeout: FiniteDuration,
      writeTimeout: FiniteDuration
  ): Resource[F, BitVectorSocket[F]] =
    for {
      blocker     <- Blocker[F]
      socketGroup <- SocketGroup[F](blocker)
      socket      <- socketGroup.client[F](new InetSocketAddress(host, port))
    } yield fromSocket(socket, readTimeout, writeTimeout)

  private def fromSocket[F[_]: MonadError[*[_], Throwable]](
      socket: Socket[F],
      readTimeout: FiniteDuration,
      writeTimeout: FiniteDuration
  ): BitVectorSocket[F] = new BitVectorSocket[F] {

    override def read(nBytes: Int): F[BitVector] =
      socket.readN(nBytes, Some(readTimeout)).flatMap {
        case Some(chunk) => chunk.toBitVector.pure[F]
        case None =>
          new Exception(s"could not read $nBytes bytes.")
            .raiseError[F, BitVector]
      }

    override def write(bits: BitVector): F[Unit] =
      socket.write(Chunk.bytes(bits.toByteArray), Some(writeTimeout))

  }

}
