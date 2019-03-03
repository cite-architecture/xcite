
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class CtsUrnStructureSpec extends FlatSpec {
  val simplePassageUrn = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
  //val simpleRangeUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")

  "A CtsUrn" should "have a namespace" in {
    assert(simplePassageUrn.namespace == "greekLit")
  }
  it should "have a hierarchical work component" in {
    assert(simplePassageUrn.workComponent == "tlg0012.tlg001.msA")
  }
  it should "determine the level of the work component" in {
    assert(simplePassageUrn.workLevel == WorkLevel.Version)
  }



}
