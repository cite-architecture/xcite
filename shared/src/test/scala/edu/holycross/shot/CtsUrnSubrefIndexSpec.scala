
package edu.holycross.shot.cite


import org.scalatest.FlatSpec


class CtsUrnSubrefIndexSpec extends FlatSpec {

  "A Cts URN" should "have an integer value for a passage node subreference index when the subref includes an explicit index" in {
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
  it should "have an integer index of 1 for a passage node when the subref is defined without index" in {
      val psgSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath")

      assert(psgSubref.passageNodeSubrefIndex == 1)

      psgSubref.rangeBeginSubrefIndexOption match {
        case None => assert(true)
        case _ => fail("Should not have found a subreference")
      }
      psgSubref.rangeEndSubrefIndexOption match {
        case None => assert(true)
        case _ => fail("Should not have found a subreference")
      }
  }
  it should "throw a Cite exception when trying to retrieve a non-existent passage node subreference index value" in  {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    try {
      noSubref.passageNodeSubrefIndex
      fail("Should not have found subref index on passage")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No individual node subreference index defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }



  it should "have a value of 1 for a subreference index on the first node of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath-1.2")

    assert(rangeSubref.rangeBeginSubrefIndex == 1)

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeEndSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have an integer value for a subreference index on the first node of a range when a subref includes an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]-1.2")

    assert(rangeSubref.rangeBeginSubrefIndex == 1)

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeEndSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve a non-existent subreference index on the first node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeBeginSubrefIndex
      fail("Should not have found subref index on range beginning")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range beginning subreference index defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }


  it should "have an integer value of 1 for a subreference index on the second node of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.2@dogs")

    assert(rangeSubref.rangeEndSubrefIndex == 1)

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeBeginSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have an integer value for a subreference index on the second node of a range when a subref includes an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.2@dogs[1]")

    assert(rangeSubref.rangeEndSubrefIndex == 1)

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
    rangeSubref.rangeBeginSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve a non-existent subreference index on the second node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeEndSubrefIndex
      fail("Should not have found subref index on range ending")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range ending subreference index defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }


  it should "have integer values of 1 for subrefs on both nodes of a range without index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath-1.2@dogs")
    assert(rangeSubref.rangeBeginSubrefIndex == 1)
    assert(rangeSubref.rangeEndSubrefIndex == 1)

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "have integer values for subrefs on both nodes of a range when the subrefs include an explicit index" in {
    val rangeSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@wrath[1]-1.2@dogs[1]")
    assert(rangeSubref.rangeBeginSubrefIndex == 1)
    assert(rangeSubref.rangeEndSubrefIndex == 1)

    rangeSubref.passageNodeSubrefOption match {
      case None => assert(true)
      case _ => fail("Should not have found a subreference")
    }
  }
  it should "throw a Cite exception when trying to retrieve non-existent subref indices on either node of a range" in {
    val noSubref = CtsUrn( "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    try {
      noSubref.rangeBeginSubrefIndex
      fail("Should not have found subref index")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range beginning subreference index defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
    try {
      noSubref.rangeEndSubrefIndex
      fail("Should not have found subref index")
    } catch {
      case citeEx: CiteException => assert(citeEx.message == "No range ending subreference index defined in urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
      case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }

}
