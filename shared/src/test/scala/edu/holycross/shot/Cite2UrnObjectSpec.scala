
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
  it should "throw an exception when it sees a trailing hyphen" in {
    try {
      Cite2Urn("urn:cite2:hmt:speeches.v1:speech1-speech4-")
      fail("Should have failed.")
    } catch {
      case badArg: IllegalArgumentException => assert(badArg.getMessage() == "requirement failed: URN cannot end with trailing -")
      case e: Throwable =>fail("Should have gotten IllegalArgument Exception instead of " + e.getMessage())
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


  it should "be able to add a version identifier to a collection" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA:")
    val expected = Cite2Urn("urn:cite2:hmt:msA.release1:")
    assert (urn.addVersion("release1") == expected)
  }

  it should "throw an exception if adding a version identifier to a URN that already has one" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    try {
      urn.addVersion("release2")
      fail("Should not have been able to add selector")
    } catch {
      case iae: IllegalArgumentException => {
        assert(iae.getMessage() == "requirement failed: cannot add version to a URN that already has one urn:cite2:hmt:msA.release1:")
      }
    }
  }


  it should "be able to add an object selector to a version-level URN" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    val expected = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    assert (urn.addSelector("12r") == expected)
  }
  it should "be able to add a selector to a property-level URN" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1.side:")
    val expected = Cite2Urn("urn:cite2:hmt:msA.release1.side:12r")
    assert (urn.addSelector("12r") == expected)
  }

  it should "throw an exception if adding an object to a URN that does not have a version ID" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA:")
    try {
      urn.addSelector("12r")
      fail("Should not have been able to add selector")
    } catch {
      case iae: IllegalArgumentException => {
        assert(iae.getMessage() == "requirement failed: cannot add selector to unversioned URN urn:cite2:hmt:msA:")
      }
    }
  }
  it should "throw an exception if adding an object selector to a URN that already has an object ID" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12v")
    try {
      urn.addSelector("12r")
      fail("Should not have been able to add selector")
    } catch {
      case iae: IllegalArgumentException => {
        assert(iae.getMessage() == "requirement failed: cannot add an object selector to URN that already has one urn:cite2:hmt:msA.release1:12v")
      }
    }
  }

  it should "be able to add a property to a version-level URN" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    val expected = Cite2Urn("urn:cite2:hmt:msA.release1.side:")
    assert (urn.addProperty("side") == expected)
  }
  it should "throw an exception if adding a property to a URN that already has a property ID" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1.side:")
    try {
      urn.addProperty("sequence")
      fail("Should not have been able to add property")
    } catch {
      case iae: IllegalArgumentException => {
        assert(iae.getMessage() == "requirement failed: cannot add a property to a URN that already has one urn:cite2:hmt:msA.release1.side:")
      }
    }
  }
  it should "throw an exception if adding a property to a URN that already does not have a version ID" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA:")
    try {
      urn.addProperty("side")
      fail("Should not have been able to add property")
    } catch {
      case iae: IllegalArgumentException => {
        assert(iae.getMessage() == "requirement failed: cannot add a property to a URN that does not have a version urn:cite2:hmt:msA:")
      }
    }
  }




}
