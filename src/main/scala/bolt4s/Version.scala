package bolt4s

import scodec.Codec
import scodec.bits._
import scodec.codecs._
import scodec.codecs.literals._

final case class Version(major: Int, minor: Int)

object Version {

  private val hex_00: Codec[Unit] = hex"00"

  implicit val codec: Codec[Version] = (hex_00 ~> hex_00 ~> uint8 ~ uint8).xmap(
    tuple => Version(tuple._2, tuple._1),
    version => (version.minor, version.major)
  )

}
