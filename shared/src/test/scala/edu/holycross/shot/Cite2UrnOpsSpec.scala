
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class Cite2UrnOpsSpec extends FlatSpec {

  "A Cite2Urn" should "be able to strip subreferences from an object" in {
    val u = Cite2Urn("urn:cite2:demo:imglist:img1@x,y")
    assert (u.dropExtensions == Cite2Urn("urn:cite2:demo:imglist:img1"))
  }
  it should "be able to strip subreferences from the first node of a range" in {
    val u = Cite2Urn("urn:cite2:demo:imglist:img1@x,y-img2")
    assert (u.dropExtensions == Cite2Urn("urn:cite2:demo:imglist:img1-img2"))

  }
  it should "be able to strip subreferences from the second node of a range" in {
    val u = Cite2Urn("urn:cite2:demo:imglist:img1-img2@x,y")
    assert (u.dropExtensions == Cite2Urn("urn:cite2:demo:imglist:img1-img2"))
  }
  it should "be able to strip subreferences from both nodes of a range" in {
    val u = Cite2Urn("urn:cite2:demo:imglist:img1@a,b-img2@x,y")
    assert (u.dropExtensions == Cite2Urn("urn:cite2:demo:imglist:img1-img2"))
  }

  it should "be able to drop the property component" in {
    val u = Cite2Urn("urn:cite2:hmt:speeches.r1.speaker:speech1")
    assert (u.dropProperty == Cite2Urn("urn:cite2:hmt:speeches.r1:speech1"))
  }
  it should "be able to drop the property component when there is no object reference" in {
    val u = Cite2Urn("urn:cite2:hmt:speeches.r1.speaker:")
    assert (u.dropProperty == Cite2Urn("urn:cite2:hmt:speeches.r1:"))
  }

}
