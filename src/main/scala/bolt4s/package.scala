import scodec.bits._

package object bolt4s {

  val port = 57687 // TODO

  // Bolt Identification constant
  val id: BitVector = hex"6060B017".bits

}
