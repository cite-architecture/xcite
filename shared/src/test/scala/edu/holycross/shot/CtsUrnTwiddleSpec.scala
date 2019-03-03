
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class CtsUrnTwiddleSpec extends FlatSpec {


  "A CtsUrn" should "twiddle" in {
    val deep = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    val shallow = CtsUrn("urn:cts:greekLit:tlg5026.msA:")


    val deepLTshallow = deep <= shallow
    assert(deepLTshallow)
  }

  it should "reject equating different urns, duh" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msAim.hmt:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    val cf = u1 <= u2
    assert(cf == false)
  }

}
