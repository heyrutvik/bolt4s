package bolt4s

import scodec.Codec
import scodec.bits._
import scodec.codecs._
import scodec.codecs.literals._

final case class Version(major: Int, minor: Int)

object Version {

  lazy val `v3.0`: Version = Version(3, 0)
  lazy val `v4.0`: Version = Version(4, 0)
  lazy val `v4.1`: Version = Version(4, 1)
  lazy val `v4.2`: Version = Version(4, 2)

  lazy val versions = List(`v4.2`, `v4.1`, `v4.0`, `v3.0`)

  private val hex_00: Codec[Unit] = hex"00"

  implicit val codec: Codec[Version] = (hex_00 ~> hex_00 ~> uint8 ~ uint8).xmap(
    tuple => Version(tuple._2, tuple._1),
    version => (version.minor, version.major)
  )

}
