
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class Cite2UrnMatchingSpec extends FlatSpec {

  "Cite2Urn matching" should "match collection values when they are identical" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r")
    val u2 = Cite2Urn("urn:cite2:hmt:msA:12v")
    assert (u1.collectionsMatch(u2))
    assert (u2.collectionsMatch(u1))
  }

  it should "match collection values when one collection is versioned and the other is not" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r")
    val u2 = Cite2Urn("urn:cite2:hmt:msA.release1:12v")
    assert (u1.collectionsMatch(u2))
    assert (u2.collectionsMatch(u1))
  }

  it should "match object values when they are identical" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r")
    val u2 = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    assert (u1.objectsMatch(u2))
  }

  it should "match object values when they differ only in extended reference" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r@x,y")
    val u2 = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    assert (u1.objectsMatch(u2))
  }

  it should "match range values when they are identical" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r-14v")
    val u2 = Cite2Urn("urn:cite2:hmt:msA.release1:12r-14v")
    assert (u1.objectsMatch(u2))
  }

  it should "match range values when they differ only in extended reference" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r-14v")
    val u2 = Cite2Urn("urn:cite2:hmt:msA.release1:12r@a,b-14v")
    assert (u1.objectsMatch(u2))

    val u3 = Cite2Urn("urn:cite2:hmt:msA.release1:12r-14v@a,b")
    assert (u1.objectsMatch(u3))

    val u4 = Cite2Urn("urn:cite2:hmt:msA.release1:12r@x,y-14v@a,b")
    assert (u1.objectsMatch(u4))
  }

  it should "match URNs when they are identical" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r-14v")
    val u2 = Cite2Urn("urn:cite2:hmt:msA:12r-14v")
    assert (u1.urnMatch(u2))
    assert (u2.urnMatch(u1))
  }
  it should "match URNs when they are differ only in collection level" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA.release1:12r-14v")
    val u2 = Cite2Urn("urn:cite2:hmt:msA:12r-14v")
    assert (u1.urnMatch(u2))
    assert (u2.urnMatch(u1))
  }

  it should "match URNs when objects differ only in object extensions" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    val u2 = Cite2Urn("urn:cite2:hmt:msA:12r@x,y")
    assert (u1.urnMatch(u2))
    assert (u2.urnMatch(u1))
  }
  it should "match URNs when ranges differ only in object extensions" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA:12r-14v")
    val u2 = Cite2Urn("urn:cite2:hmt:msA:12r@x,y-14v")
    assert (u1.urnMatch(u2))
    assert (u2.urnMatch(u1))

    val u3 = Cite2Urn("urn:cite2:hmt:msA:12r-14v@x,y")
    assert (u1.urnMatch(u3))
    assert (u3.urnMatch(u1))

    val u4 = Cite2Urn("urn:cite2:hmt:msA:12r@a,b-14v@x,y")
    assert (u1.urnMatch(u4))
    assert (u4.urnMatch(u1))

  }

  it should "match URNs when objects differ only in collection level and object extensions" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    val u2 = Cite2Urn("urn:cite2:hmt:msA:12r@x,y")
    assert (u1.urnMatch(u2))
    assert (u2.urnMatch(u1))
  }
  it should "match URNs when ranges differ only in object extensions and collection level" in {
    val u1 = Cite2Urn("urn:cite2:hmt:msA.v1:12r-14v")
    val u2 = Cite2Urn("urn:cite2:hmt:msA:12r@x,y-14v")
    assert (u1.urnMatch(u2))
    assert (u2.urnMatch(u1))

    val u3 = Cite2Urn("urn:cite2:hmt:msA:12r-14v@x,y")
    assert (u1.urnMatch(u3))
    assert (u3.urnMatch(u1))

    val u4 = Cite2Urn("urn:cite2:hmt:msA:12r@a,b-14v@x,y")
    assert (u1.urnMatch(u4))
    assert (u4.urnMatch(u1))

  }
}
