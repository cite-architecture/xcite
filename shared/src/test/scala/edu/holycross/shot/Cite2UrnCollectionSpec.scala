
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class Cite2UrnCollectionSpec extends FlatSpec {

  "A Cite2Urn" should "always have a collection identifier" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    assert (urn.collection == "msA")
  }

  it should "have a none option for version if no version is given" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA:12r")
    urn.versionOption match {
      case None => assert(true)
      case _ => fail("Should not have found a version option")
    }
  }
  it should "retrieve a string value for a valid version" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    assert (urn.version == "release1")
  }

  it should "throw a Cite exception when trying to retrieve a non-existent version value" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA:12r")
    try {
      urn.version
      fail("Should not have reached this point")
    } catch {
      case ce: CiteException => assert (ce.message == "No version defined in urn:cite2:hmt:msA:12r")
      case e: Throwable => fail("Unexpected exception " + e)
    }
  }

}
