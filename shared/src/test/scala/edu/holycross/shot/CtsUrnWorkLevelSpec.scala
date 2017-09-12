package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class CtsUrnWorkLevelSpec extends FlatSpec {

  "A CtsUrn" should "test the work component for exemplar" in  {

    val ex = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tokens:1.1.1")
    val vers = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    assert(ex.isExemplar)
    assert(vers.isExemplar == false)
  }

  it should "test the work component for version" in  {

    val ex = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tokens:1.1.1")
    val vers = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    assert(ex.isVersion == false)
    assert(vers.isVersion)
  }


  it should "test the work component for notional work" in  {

    val ex = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tokens:1.1.1")
    val notional = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert(ex.isNotional == false)
    assert(notional.isNotional)
  }


  it should "test the work component for text group" in  {

    val ex = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tokens:1.1.1")
    val group = CtsUrn("urn:cts:greekLit:tlg0012:")
    assert(ex.isTextGroup == false)
    assert(group.isTextGroup)
  }


}
