package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class CtsUrnExemplarSpec extends FlatSpec {

  val venetusA = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")

  "A CtsUrn" should "be able to drop the exemplar component from an exemplar-level value" in {
    val exemplarLevel = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tokens:")
    assert(exemplarLevel.dropExemplar == venetusA)
  }

  it should "return the same URN when no exemplar is present" in {
    assert(venetusA.dropExemplar == venetusA)
  }

  it should "maintain the passage component unchanged" in {
    val exemplarWithPassage = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tokens:1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    assert (exemplarWithPassage.dropExemplar == expected)
  }

  "An exemplar-level CTS URN with exemplar component" should "be able to add a new exemplar identifer"  in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_diplomatic:1.1")
    assert (iliadUrn.addVersion("msA_diplomatic") == expected)
  }

}
