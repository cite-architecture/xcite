
package edu.holycross.shot.cite


import org.scalatest.FlatSpec


class CtsUrnSubrefSpec extends FlatSpec {

  "A Cts URN" should "have a none value for all subref options when no subref is defined" in {
    val noSubref = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

    noSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    noSubref.rangeBeginSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    noSubref.rangeEndSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }


  it should "have none options for all subref text and index pieces when there is no subref" in {
    val noSubref = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

    noSubref.passageNodeSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    noSubref.passageNodeSubrefIndexOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }

    noSubref.rangeBeginSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    noSubref.rangeBeginSubrefIndexOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }

    noSubref.rangeEndSubrefTextOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    noSubref.rangeEndSubrefIndexOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }


  it should "have a string value for a passage node when the subref is defined without index" in {
    val psgSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath")

    assert(psgSubref.passageNodeSubref == "wrath")

    psgSubref.rangeBeginSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    psgSubref.rangeEndSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have a string value for a passage node subref when the subref includes an explicit index" in {
    val psgSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]")

    assert(psgSubref.passageNodeSubref == "wrath[1]")

    psgSubref.rangeBeginSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    psgSubref.rangeEndSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve a non-existent passage node subref value" in  {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    try {
      noSubref.passageNodeSubref
      fail("Should not have found subref")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No individual node subref defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }



  it should "have a string value for a subref on the first node of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath-1.2")

    assert(rangeSubref.rangeBeginSubref == "wrath")

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeEndSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have a string value for a subref on the first node of a range when a subref includes an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]-1.2")

    assert(rangeSubref.rangeBeginSubref == "wrath[1]")

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeEndSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve a non-existent subref value on the first node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeBeginSubref
      fail("Should not have found subref")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range beginning subreference defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }



  it should "have a string value for a subref on the second node of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.2@dogs")

    assert(rangeSubref.rangeEndSubref == "dogs")

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeBeginSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have a string value for a subref on the second node of a range when a subref includes an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.2@dogs[1]")

    assert(rangeSubref.rangeEndSubref == "dogs[1]")

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeBeginSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve a non-existent subref value on the second node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeEndSubref
      fail("Should not have found subref")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range ending subreference defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }


  it should "have string values for subrefs on both nodes of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath-1.2@dogs")

    assert(rangeSubref.rangeBeginSubref == "wrath")
    assert(rangeSubref.rangeEndSubref == "dogs")

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have a string value for subrefs on both nodes of a range when the subrefs include an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]-1.2@dogs[1]")


    assert(rangeSubref.rangeBeginSubref == "wrath[1]")
    assert(rangeSubref.rangeEndSubref == "dogs[1]")

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve non-existent subref values on either node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeBeginSubref
      fail("Should not have found subref")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range beginning subreference defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
    try {
      noSubref.rangeEndSubref
      fail("Should not have found subref")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range ending subreference defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }


  /*(){




    val rangeBothSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath-1.2@dogs")
    val rangeBothIndexedSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]-1.2@dogs[1]")
  }

*/




}
