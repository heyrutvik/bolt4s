package bolt4s

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scodec.Codec
import scodec.bits.BitVector

class VersionSpec extends AnyFlatSpec with Matchers {

  "Version encoding".should("represent valid bits").in {
    val encodedVersion: BitVector = Codec.encode(Version(4, 2)).require
    BitVector.fromHex("00000204").contains(encodedVersion)
  }

}
