package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class CtsUrnPassageSpec extends FlatSpec {

  "The passage component of a CtsUrn" should "have an empty option if no passage is given" in {
    val noPassage = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    noPassage.passageComponentOption match {
      case None => assert(true)
      case _ => fail("Incorrect structure of passage option")
    }
  }
  it should "retrieve a string value for a well-formed passage identifier" in {
    val singleNode = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert(singleNode.passageComponent == "1.1")
  }
  it should "throw a CITE exception when trying to retrieve a non-existent passage component" in {
    val iliad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    try {
      iliad.passageComponent
      fail("Should not have found passage component")
    } catch {
      case ctsEx: CiteException => assert(ctsEx.message == "No passage component defined in urn:cts:greekLit:tlg0012.tlg001:")
      case exc : Throwable => fail("Should have thrown a CiteException: " + exc)
    }
  }

  it should "add a passage component to a URN lacking one" in {
    val original = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    val passage = "1.1"
    val expected = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    assert(original.addPassage(passage) == expected)
  }
  it should "replace a passage component when adding to a URN already having one" in {
      val original = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
      val passage = "1.2"
      val expected = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
      assert(original.addPassage(passage) == expected)
  }
/*





  it should "retrieve a string value for a well-formed version identifier" in {
    val versionLevel = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:")
    assert(versionLevel.version == "msA")
  }
  it should "throw a CtsUrn exception when trying to retrieve a non-existent version value" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    try {
      groupLevel.version
    } catch {
      case ctsEx: CiteException => assert(ctsEx.message == "No version defined in urn:cts:greekLit:tlg0012:")
      case exc : Throwable => fail("Should have thrown a CiteException: " + exc)
    }
  }


  it should "retrieve a string value for a well-formed exemplar identifier" in {
    val versionLevel = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA.tokens:")
    assert(versionLevel.exemplar == "tokens")
  }
  it should "throw a CtsUrn exception when trying to retrieve a non-existent exemplar value" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    try {
      groupLevel.exemplar
    } catch {
      case ctsEx: CiteException => assert(ctsEx.message == "No exemplar defined in urn:cts:greekLit:tlg0012:")
      case exc : Throwable => fail("Should have thrown a CiteException: " + exc)
    }
  }*/
}
