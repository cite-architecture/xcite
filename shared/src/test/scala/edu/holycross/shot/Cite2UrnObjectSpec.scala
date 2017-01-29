
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



}
