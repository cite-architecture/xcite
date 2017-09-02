package edu.holycross.shot.cite

import org.scalatest.FlatSpec



class CtsUrnOpsSpec extends FlatSpec {


  "A CTS URN" should "support collapsing the passage component by a specified number of levels" in {
    val twoTier = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert (twoTier.collapsePassageBy(1) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1"))
  }
  it should "return a URN with no passage component if there are fewer than 2 tiers to the citation hierarchy" in {
    val oneTier = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1")
    assert (oneTier.collapsePassageBy(1) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:"))

  }



  it should "support collapsing the passage component to a specified level" in {
    val tokenUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1.1")
    assert (tokenUrn.collapsePassageTo(2) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1"))
  }

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

  "A version-level CTS URN with passage component" should "be able to drop the version" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert (iliadUrn.dropVersion == expected)

  }
  it should "be able to add a new version identifer" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_diplomatic:1.1")
    assert (iliadUrn.addVersion("msA_diplomatic") == expected)
  }



}
