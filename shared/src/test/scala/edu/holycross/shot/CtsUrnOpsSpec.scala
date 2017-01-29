package edu.holycross.shot.cite

import org.scalatest.FlatSpec



class CtsUrnOpsSpec extends FlatSpec {


  "A CTS URN with passage component" should "allow dropping the passage component" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert  (iliadUrn.dropPassage == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:"))
  }

  "A CTS URN with a subreference" should "allow stripping off the subreference" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1@μῆνιν")
    assert  (iliadUrn.dropSubref == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1"))
  }

  "A CTS URN with subreference on a range beginning node" should "allow stripping off the subreference" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1@μῆνιν-1.2")
    assert(iliadUrn.dropSubref == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.2"))
  }


  "A CTS URN with subreference on a range ending node" should "allow stripping off the subreference" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.2@οὐλομένην")
    assert(iliadUrn.dropSubref == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.2"))
  }

  "A CTS URN with subreference on both range nodes" should "allow stripping off the subreferences" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1@μῆνιν-1.2@οὐλομένην")
    assert(iliadUrn.dropSubref == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.2"))
  }

  "A CTS URN with subreference on both range nodes" should "return a single point URN if both subreferences are on the same citable node. E pluribus unum." in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1@μῆνιν-1.1@θεά")
    assert(iliadUrn.isRange)
    assert(iliadUrn.dropSubref == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1"))
    assert(iliadUrn.dropSubref.isPoint)
  }

}
