
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class Cite2UrnRangeSpec extends FlatSpec {

  "A Cite2Urn" should "should have an empty option for range beginning if no range is given" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    urn.rangeBeginOption match {
      case None => assert(true)
      case _ => fail("Should not have found range beginning")
    }
  }
  it should "should have an empty option for range ending if no range is given" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    urn.rangeEndOption match {
      case None => assert(true)
      case _ => fail("Should not have found range ending")
    }
  }
  it should "thow a CiteException if the object range has more than two parts" in {
    try {
       Cite2Urn("urn:cite2:hmt:msA.release1:12r-14v-22r")
       fail("Should not have constructed urn")
    } catch {
      case ce: CiteException => assert(ce.message == "Invalid object component syntax: urn:cite2:hmt:msA.release1:12r-14v-22r")
      case e: Throwable => fail("Unexpected exception " + e)
    }
  }


  it should "should retrieve a string value for a valid range beginning identifier" in {
   val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r-22r")
   assert (urn.rangeBegin == "12r")
  }
  it should "throw a CITE exception when trying to retrieve a non-existent range beginning identifier" in {
    try {
       val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
       urn.rangeBegin
       fail("Should not have constructed range begnning")
    } catch {
      case ce: CiteException => assert(ce.message == "No range beginning defined in urn:cite2:hmt:msA.release1:12r")
      case e: Throwable => fail("Unexpected exception " + e)
    }
  }


  it should "should retrieve a string value for a valid range ending identifier" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r-22r")
    assert (urn.rangeEnd == "22r")
  }
  it should "throw a CITE exception when trying to retrieve a non-existent object 2 identifier" in {
    try {
      val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
      val re = urn.rangeEnd
      fail("Should not have created range end for " + urn)
    } catch {
      case ce: CiteException => assert(ce.message == "No range ending defined in urn:cite2:hmt:msA.release1:12r")
      case e: Throwable => fail("Unexpected exception " + e)
    }
  }

  it should "throw an IllegalArgumentException if a range has an empty first node" in {
    try {
      val urn = Cite2Urn("urn:cite2:hmt:msA.release1:-12r")
      fail("Should not have created urn " + urn)
    } catch {
      case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: invalid value: empty value in object component in urn:cite2:hmt:msA.release1:-12r")
      case otherE: Throwable => fail("Unexpected exception " + otherE)
    }
  }
  it should "throw an IllegalArgumentException if a range has an empty second node" in {
    try {
      val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r-")
      fail("Should not have created urn " + urn)
    } catch {
      case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: URN cannot end with trailing -")
      case otherE: Throwable => fail("Unexpected exception " + otherE)
    }
  }

  it should "create a URN for range beginning" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:1r-12r")
    val expected = Cite2Urn("urn:cite2:hmt:msA.release1:1r")
    assert(urn.rangeBeginUrn == expected)
  }

  it should "create a URN for range end" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:1r-12r")
    val expected = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    assert(urn.rangeEndUrn == expected)
  }

}
