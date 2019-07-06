
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class Cite2UrnNullableSpec extends FlatSpec {

  "The Cite2Urn object" should "create an Option[Cite2Urn] from a valid URN string" in {
    val urnOpt = Cite2Urn.nullable("urn:cite2:hmt:msA.release1:")
    assert (urnOpt.get.collection == "msA")
  }

  it should "recognize the string 'null' as a special object identifier" in {
    val urnOpt = Cite2Urn.nullable("urn:cite2:hmt:msA.release1:null")
    urnOpt match {
      case None => assert(true)
      case opt: Option[Cite2Urn] => fail("Should not have created a Some")
    }
  }



}
