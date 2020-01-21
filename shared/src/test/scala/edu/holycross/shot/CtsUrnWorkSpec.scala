package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class CtsUrnWorkSpec extends FlatSpec {

  "The work component of a CtsUrn" should "always have a text group identifier" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    assert(groupLevel.textGroup == "tlg0012")
  }

  it should "have a none option for work if no work is given" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    groupLevel.workOption match {
      case None => assert(1 == 1)
      case _ => fail("Incorrect option for work")
    }
  }
  it should "have a none option for version if no version is given" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    groupLevel.versionOption match {
      case None => assert(true)
      case _ => fail("Incorrect option for work")
    }
  }
  it should "have an none option for exemplar if no exemplar is given" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    groupLevel.versionOption match {
      case None => assert(true)
      case _ => fail("Incorrect option for work")
    }
  }


  it should "retrieve a string value for a well-formed work identifier" in {
    val workLevel = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001:")
    assert(workLevel.work == "tlg001")
  }
  
  /*
  it should "throw a Cite exception when trying to retrieve a non-existent work value" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    try {
      groupLevel.work
      fail("Should not have found work element")
    } catch {
      case ctsEx: CiteException => assert(ctsEx.message == "No work defined in urn:cts:greekLit:tlg0012:")
      case exc : Throwable => fail("Should have thrown a CiteException: " + exc)
    }
  }
  */

  it should "retrieve a string value for a well-formed version identifier" in {
    val versionLevel = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:")
    assert(versionLevel.version == "msA")
  }

  /*
  it should "throw a Cite exception when trying to retrieve a non-existent version value" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    try {
      groupLevel.version
      fail("Should not have found version on group")
    } catch {
      case ctsEx: CiteException => assert(ctsEx.message == "No version defined in urn:cts:greekLit:tlg0012:")
      case exc : Throwable => fail("Should have thrown a CiteException: " + exc)
    }
  }
  */

  it should "retrieve a string value for a well-formed exemplar identifier" in {
    val versionLevel = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA.tokens:")
    assert(versionLevel.exemplar == "tokens")
  }

  /*
  it should "throw a Cite exception when trying to retrieve a non-existent exemplar value" in {
    val groupLevel = CtsUrn("urn:cts:greekLit:tlg0012:")
    try {
      groupLevel.exemplar
      fail("Should not have exemplar on group")
    } catch {
      case ctsEx: CiteException => assert(ctsEx.message == "No exemplar defined in urn:cts:greekLit:tlg0012:")
      case exc : Throwable => fail("Should have thrown a CiteException: " + exc)
    }
  }
  */
}
