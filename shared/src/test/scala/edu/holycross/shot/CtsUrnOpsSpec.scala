package edu.holycross.shot.cite

import org.scalatest.FlatSpec



class CtsUrnOpsSpec extends FlatSpec {


  "A CTS URN" should "support collapsing the passage component by a specified number of levels" in {
    val twoTier = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert (twoTier.collapsePassageBy(1) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1"))
  }

  it should "support collapsing the passage component by a specified number of levels, more than 1" in {
    val twoTier = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3.1.1")
    assert (twoTier.collapsePassageBy(2) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3"))
  }

  it should "support collapsing the passage component of a range by a specified number of levels" in {
    val twoTier = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1.1-2.2.2")
    assert (twoTier.collapsePassageBy(2) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1-2"))
  }


  it should "return a URN with no passage component if there are fewer than 2 tiers to the citation hierarchy" in {
    val oneTier = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1")
    assert (oneTier.collapsePassageBy(1) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:"))

  }

  it should "collapse to level 2 a range of 3-level endpoints" in {
    val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.2.3-1.4.5")
    val collapsed = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.2-1.4")
    val collapsed2 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1")
    assert( urn.collapsePassageBy(1) == collapsed)
    assert( urn.collapsePassageTo(2) == collapsed)
    assert( urn.collapsePassageBy(2) == collapsed2)
    assert( urn.collapsePassageTo(1) == collapsed2)
  }

  it should "if the result of a collapsePassage operation is a range with identical endpoints, it should turn it into a single leaf-node urn" in {
    val urn1 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.1.1-1.1.5")
    val urn2 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1.1")
    val urn3 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tok:1")

    assert( urn1.collapsePassageBy(1) == urn2 )
    assert( urn1.collapsePassageTo(2) == urn2 )
    assert( urn1.collapsePassageBy(2) == urn3 )
    assert( urn1.collapsePassageTo(1) == urn3 )

  }



  it should "support collapsing the passage component to a specified level" in {
    val tokenUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1.1")
    assert (tokenUrn.collapsePassageTo(2) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1"))
  }

  it should "support collapsing the passage component of a range-urn to a specified level" in {
    val tokenUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1.1-2.2.2")
    assert (tokenUrn.collapsePassageTo(2) == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-2.2"))
  }

  "A CTS URN with passage component" should "allow dropping the passage component" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert  (iliadUrn.dropPassage == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:"))
  }

  "A CTS URN without a passage component" should "allow dropping the passage component" in {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
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
