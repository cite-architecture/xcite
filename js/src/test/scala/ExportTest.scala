package edu.holycross.shot.cite

import org.scalatest._

class ExportTest extends FlatSpec {

  "The CITE library"  should "expose methods of CTS URNs" in {
    val iliad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    assert (iliad.work == "tlg001")
  }

  it should "expose methods of CITE2URNs" in {
    val twelveR = Cite2Urn("urn:cite2:hmt:msA:12r")
    assert (twelveR.collection == "msA")
  }
}
