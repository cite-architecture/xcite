
package edu.holycross.shot.cite


import org.scalatest.FlatSpec


class CtsUrnSubrefTextSpec extends FlatSpec {

  "A CtsUrn" should "have the text part of a passage node subreference equal to the subreference when no index is defined" in  {
    val psgSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath")

    assert(psgSubref.passageNodeSubrefText == "wrath")

    psgSubref.rangeBeginSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    psgSubref.rangeEndSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have a string value for a passage node subreference text content when the subref includes an explicit index" in {
    val psgSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]")

    assert(psgSubref.passageNodeSubrefText == "wrath")

    psgSubref.rangeBeginSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    psgSubref.rangeEndSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve non-existent passage node subreference text content" in pending /*{
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    try {
      noSubref.passageNodeSubrefText
      fail("Should not have found subref content")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No individual node subreference defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }*/

  it should "have a string value for subreference text on the first node of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath-1.2")

    assert(rangeSubref.rangeBeginSubrefText == "wrath")

    rangeSubref.passageNodeSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeEndSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have a string value for the subreference text on the first node of a range when a subref includes an explicit index" in  {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]-1.2")

    assert(rangeSubref.rangeBeginSubrefText == "wrath")

    rangeSubref.passageNodeSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeEndSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve non-existent subreference text content on the first node of a range" in {
     val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
     try {
       noSubref.rangeBeginSubrefText
       fail("Should not have found subref content")
     } catch {
       case citeEx: CiteException => assert(citeEx.message == "No range beginning subreference text defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
       case ex: Throwable => fail("Unrecognized exception " + ex)
     }
  }



  it should "have a string value for subreference text on the second node of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.2@dogs")

    assert(rangeSubref.rangeEndSubrefText == "dogs")

    rangeSubref.passageNodeSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeBeginSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have a string value for the subreference text on the second node of a range when a subref includes an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.2@dogs[1]")

    assert(rangeSubref.rangeEndSubrefText == "dogs")

    rangeSubref.passageNodeSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeBeginSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve non-existent subreference text content on the second node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeEndSubrefText
      fail("Should not have found subref content")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range ending subreference text defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }



  it should "have string values for subreference text on both nodes of a range without index" in {
      val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath-1.2@dogs")

      assert(rangeSubref.rangeBeginSubrefText == "wrath")
      assert(rangeSubref.rangeEndSubrefText == "dogs")

      rangeSubref.passageNodeSubrefTextOption match {
        case None => assert(true)
        case _ => fail("Should not have found a subreference")
      }
  }
  it should "have string values for the subreference text on both nodes of a range when the subrefs includes an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]-1.2@dogs[1]")


    assert(rangeSubref.rangeBeginSubrefText == "wrath")
    assert(rangeSubref.rangeEndSubrefText == "dogs")

    rangeSubref.passageNodeSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve non-existent subreference text content on the either node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeBeginSubrefText
      fail("Should not have found subref content")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range beginning subreference text defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
    try {
      noSubref.rangeEndSubrefText
      fail("Should not have found subref content")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range ending subreference text defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }

  it should "allow period in subreference" in {
    val withPeriod = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@.")
  }

  it should "allow colon in subreference" in {
    val withColon = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@:")
  }

}
