package edu.holycross.shot.cite

import org.scalatest.FlatSpec



class CtsUrnMatchingSpec extends FlatSpec {

  "CTS URN matching" should "determine if a passage reference in one URN appears in the passage hierarchy of another URN" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1.lemma")
    assert(containedUrn.passageContainedIn(containingUrn))
  }

  it should "determine if either of two URNs' passage reference is contained by the either" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1.lemma")
    assert(containedUrn.passageMatch(containingUrn))
    assert(containingUrn.passageMatch(containedUrn))
  }

  it should "determine if a work reference in one URN appears in the work hierarchy of another URN" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    assert(containedUrn.workContainedIn(containingUrn))
  }

  it should "determine if either of two URNs' work reference is contained by the either" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    assert(containedUrn.workMatch(containingUrn))
    assert(containingUrn.workMatch(containedUrn))
  }


  it should "match two URNs differing only in the depth of work hierarchy" in {
    val notionalUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    val editionUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    assert  (notionalUrn.urnMatch(editionUrn))
    assert (editionUrn.urnMatch(notionalUrn))
  }

  it should "match two URNs differing only in the depth of the passage hierarchy" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1.lemma")
    assert  (containingUrn.urnMatch(containedUrn))
    assert (containedUrn.urnMatch(containingUrn))
  }

  it should "match two URNs differing both in the depth of the passage hierarchy and depth of the work hierarchy" in {
    val urn1 = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val urn2 = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1.lemma")
    assert  (urn1.urnMatch(urn2))
    assert (urn2.urnMatch(urn1))
  }

  it should "match two URNs with the same work component if either passage component is empty" in {
    val passageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val noPassageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert  (passageUrn.urnMatch(noPassageUrn))
    assert (noPassageUrn.urnMatch(passageUrn))
  }

  it should "match two URNs in the depth of work hierarchy if either passage component is empty" in {
    val passageUrn = CtsUrn("urn:cts:greekLit:tlg5026:1.1")
    val noPassageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert  (passageUrn.urnMatch(noPassageUrn))
    assert (noPassageUrn.urnMatch(passageUrn))
  }


  it should "allow operator-like syntax with ~~" in {
    val passageUrn = CtsUrn("urn:cts:greekLit:tlg5026:1.1")
    val noPassageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert  (passageUrn ~~ noPassageUrn)
  }
}
