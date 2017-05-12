
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class Cite2UrnExtendedRefSpec extends FlatSpec {

  "A Cite2Urn" should " have a none value for all subref options when no subref is defined" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    urn.objectExtensionOption match {
      case None => assert (true)
      case _ => fail("Should not have created extended reference on object")
    }
    urn.rangeBeginExtensionOption match {
      case None => assert (true)
      case _ => fail("Should not have created extended reference on range beginning")
    }
    urn.rangeEndExtensionOption match {
      case None => assert (true)
      case _ => fail("Should not have created extended reference on range ending")
    }
  }

  it should "have a string value for a object subref when the subref is defined" in {
    val urn = Cite2Urn("urn:cite2:demo:imglist:img1@x,y,wdth,hght")
    assert (urn.objectExtension == "x,y,wdth,hght")
  }

  it should "have a string value for a subref on the first node of a range" in {
    val urn = Cite2Urn("urn:cite2:demo:imglist:img1@x,y,wdth,hght-img2")
    assert (urn.rangeBeginExtension == "x,y,wdth,hght")
  }

  it should "throw a Cite exception when trying to retrieve a non-existent subref value on the first node of a range" in  {
    try {
      val u = Cite2Urn("urn:cite2:demo:imglist:img1-img2")
      u.rangeBeginExtension
      fail("Should not have found range beginning extension")
    } catch {
      case ce: CiteException => assert(ce.message == "No extended reference in range beginning of urn:cite2:demo:imglist:img1-img2")
      case e: Throwable => fail("Unexpected exception " + e)
    }
  }

  it should "have a string value for a subref on the second node of a range" in {
    val urn = Cite2Urn("urn:cite2:demo:imglist:img1-img2@x,y,wdth,hght")
    assert (urn.rangeEndExtension == "x,y,wdth,hght")
  }

  it should "throw a Cite exception when trying to retrieve a non-existent subref value on the second node of a range" in {
    try {
      val u = Cite2Urn("urn:cite2:demo:imglist:img1-img2")
      u.rangeEndExtension
      fail("Should not have found range ending extension")
    } catch {
      case ce: CiteException => assert(ce.message == "No extended reference in range ending of urn:cite2:demo:imglist:img1-img2")
      case e: Throwable => fail("Unexpected exception " + e)
    }
  }


  it should "have string values for subrefs on both nodes of a range" in {
    val urn = Cite2Urn("urn:cite2:demo:imglist:img1@a,b,c,d-img2@x,y,wdth,hght")
    assert (urn.rangeEndExtension == "x,y,wdth,hght")
    assert (urn.rangeBeginExtension == "a,b,c,d")
  }
  it should "should throw a Cite exception when trying to retrieve non-existent subref values on either node of a range" in {
    val u = Cite2Urn("urn:cite2:demo:imglist:img1-img2")
    try {
      u.rangeBeginExtension
      fail("Should not have found range beginning extension")
    } catch {
      case ce: CiteException => assert(ce.message == "No extended reference in range beginning of urn:cite2:demo:imglist:img1-img2")
      case e: Throwable => fail("Unexpected exception " + e)
    }
    try {
      u.rangeEndExtension
      fail("Should not have found range ending extension")
    } catch {
      case ce: CiteException => assert(ce.message == "No extended reference in range ending of urn:cite2:demo:imglist:img1-img2")
      case e: Throwable => fail("Unexpected exception " + e)
    }
  }
}
