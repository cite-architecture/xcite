
package edu.holycross.shot.cite


import org.scalatest.FlatSpec


class CtsUrnCitationDepthSpec extends FlatSpec {

  "A Cts URN" should "have a method for reporting depth of citation" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    val depth:Vector[Int] = Vector(2)
    assert(urn.citationDepth == depth)
  }


  it should "work if the URN is a range" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    val depth:Vector[Int] = Vector(2,2)
    assert(urn.citationDepth == depth)
  }

  it should "work if the URN is a mixed range" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1-2.10")
    val depth:Vector[Int] = Vector(1,2)
    assert(urn.citationDepth == depth)
  }

  it should "work if the URN is a mixed range in another way" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-2")
    val depth:Vector[Int] = Vector(2,1)
    assert(urn.citationDepth == depth)
  }

  it should "work for deep hierarchies" in {
    val urn:CtsUrn = CtsUrn("urn:cts:namespace:group.work.ed:1.2.3.4.5")
    val depth:Vector[Int] = Vector(5)
    assert(urn.citationDepth == depth)
  }

  it should "return an empty vector if there is no passage component" in {
    val urn:CtsUrn = CtsUrn("urn:cts:namespace:group.work.ed:")
    val depth:Vector[Int] = Vector()
    assert(urn.citationDepth == depth)
  }

  it should "work if the URN has a subref with a period" in {
    val urn:CtsUrn = CtsUrn("urn:cts:namespace:group.work.ed:1.2@a.b")
    val depth:Vector[Int] = Vector(2)
    assert(urn.citationDepth == depth)
  }

  it should "work if the URN has a subref" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@μῆνιν")
    val depth:Vector[Int] = Vector(2)
    assert(urn.citationDepth == depth)
  }

  



}
