
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class Cite2UrnObjectSpec extends FlatSpec {

  "A Cite2Urn" should "should have an empty option if no object is given" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    urn.objectComponentOption match {
      case None => assert(true)
      case _ => fail("Should have created none option")
    }
  }
  it should "should retrieve a string value for a well-formed object component" in {
      val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
      assert (urn.objectComponent == "12r")
  }
  it should "throw a CITE exception when trying to retrieve a non-existent passage component" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    try {
      urn.objectComponent
      fail("Should not have reached this: " + urn.objectComponent)
    } catch {
      case e: CiteException => { assert(e.message == "No object component defined in urn:cite2:hmt:msA.release1:")}
      case exc: Throwable => fail("Should have thrown CiteException:  " + exc)
    }
  }

  it should "be able to drop the selector component" in {
    val urnObj = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    val trimmedObj = Cite2Urn("urn:cite2:hmt:msA.release1:")

    val urnProp = Cite2Urn("urn:cite2:hmt:msA.release1.side:12r")
    val trimmedProp = Cite2Urn("urn:cite2:hmt:msA.release1.side:")

    assert (urnObj.dropSelector ==  trimmedObj)
    assert (urnProp.dropSelector ==  trimmedProp)
  }


  it should "be able to drop the selector property identifier" in {
    val urnObj = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    val trimmedObj = Cite2Urn("urn:cite2:hmt:msA.release1:12r")

    val urnProp = Cite2Urn("urn:cite2:hmt:msA.release1.side:12r")
    val trimmedProp = Cite2Urn("urn:cite2:hmt:msA.release1:12r")

    assert (urnObj.dropProperty ==  trimmedObj)
    assert (urnProp.dropProperty ==  trimmedProp)
  }

}
