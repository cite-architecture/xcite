
package edu.holycross.shot.cite


import org.scalatest.FlatSpec


class CtsUrnRangeToVectorSpec extends FlatSpec {

  "A Cts URN" should "have a method for taking a range-urn and returning a vector of two URNs" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.5")
    val twoUrns:Vector[CtsUrn] = Vector(CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1"), CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.5"))
    assert(urn.rangeToUrnVector == twoUrns)
  }

  it should "return a one-item vector if the URN is not a range" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    val oneUrn:Vector[CtsUrn] = Vector(CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1"))
    assert(urn.rangeToUrnVector == oneUrn)
  }

  it should "return a one-item vector if the URN has no passage component" in {
    val urn:CtsUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")
    val oneUrn:Vector[CtsUrn] = Vector(CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:"))
    assert(urn.rangeToUrnVector == oneUrn)
  }





}
