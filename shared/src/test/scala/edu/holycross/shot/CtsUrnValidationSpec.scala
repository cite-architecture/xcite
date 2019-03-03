package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class CtsUrnValidationSpec extends FlatSpec {

  "A Cts Urn constructor" should "construct a URN object from a well-formed string" in {
    val iliadString = "urn:cts:greekLit:tlg0012.tlg001:"
    val iliadUrn = CtsUrn(iliadString)
    iliadUrn match {
      case u: CtsUrn => assert(true)
      case _ => fail("Did not construct a CtsUrn object from " + iliadString)
    }
  }

  it should "throw an Exception if the the URN string has too few components" in  {
    val noPassage = "urn:cts:greekLit:tlg0012.tlg001"
    val msg = s"Unable to parse URN string ${noPassage}"
    try {
      CtsUrn(noPassage)
      fail("Should not have formed URN")
    } catch {
      case ex : java.lang.Exception => {
        assert(ex.getMessage().contains(msg))
      }
    }
   }

   it should "throw an IllegalArgumentException if the `urn` component is missing" in {
     val noUrn = "XX:cts:greekLit:tlg0012.tlg001:"
     val msg = s"Unable to parse URN string ${noUrn}"
    try {
      CtsUrn(noUrn)
      fail("Should not have formed URN")
    } catch {
      case e: java.lang.Exception => {

        assert (e.getMessage().contains(msg))
      }
    }
  }

  it should "throw an IllegalArgumentException if the `cts` component is missing" in {
    val noCts = "urn:XXX:greekLit:tlg0012.tlg001:"
    val msg = s"Unable to parse URN string ${noCts}"
    try {
      CtsUrn(noCts)
      fail("Should not have formed URN")
    } catch {
      case e: java.lang.Exception => {
        assert (e.getMessage().contains(msg))
      }
    }
  }

  // Syntax of work component:
  it should "guarantee that the work component is non-empty" in {
    val iliad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    assert (iliad.workComponent.nonEmpty)
  }

  it should "throw an IllegalArgumentException if a work component has more than 4 parts" in {
    val tooMany = "urn:cts:greekLit:tlg0012.tlg001.msA.tokens.subexemplar:"
    try {
     CtsUrn(tooMany)
     fail("Should not have formed URN")
   } catch {
     case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: invalid URN syntax. Too many parts in work component tlg0012.tlg001.msA.tokens.subexemplar")
     case unknown: Throwable => fail("Unrecognized exception " + unknown)
   }
  }



  // Syntax of passage component:
  it should "throw a CiteException if a range has an empty first node" in {
    val noFirstNode = "urn:cts:greekLit:tlg0012.tlg001.msA:-1.10"
    val msg = s"rangeBegin function: invalid range syntax in ${noFirstNode}"

    try {
      CtsUrn(noFirstNode)
      fail("Should not have formed URN")
    } catch {
      case e: java.lang.Exception => {
        assert(e.getMessage().contains(msg))

      }

    }
  }

  it should "throw an IllegalArgumentException if a range has an empty second node" in {
    val noRangeEnd = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-"
    val msg = s"invalid range syntax in ${noRangeEnd}"
    try {
      CtsUrn(noRangeEnd)
      fail("Should not have formed URN")
    } catch {
      case e: java.lang.Exception => {
        assert (e.getMessage().contains(msg))
      }

    }
  }
  it should "throw a CiteException if a range has more than two elements" in {
    try {
     CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10-1.17")
     fail("Should not have formed URN")
   } catch {
     case e: CiteException => assert(e.message == "invalid URN string: more than two elements in range 1.1-1.10-1.17")
     case unknown: Throwable => fail("Unrecognized exception " + unknown)
   }
  }
  it should "identify a range reference as a range and not a node" in {
    val rangeUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.10")
    assert (rangeUrn.isRange)
    assert (! rangeUrn.isPoint)
  }
  it should "identify a node reference as a node and not a range" in {
    val pointUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.10")
    assert (! pointUrn.isRange)
    assert (pointUrn.isPoint)
  }
  it should "throw an exception if there are empty components within a passage reference" in {
    val doubleDots = "urn:cts:greekLit:tlg0012.tlg001.msA:1...10:"
    val msg = s"Invalid URN syntax: repeated periods in passage component of ${doubleDots}"
    try {
      val urn = CtsUrn(doubleDots)
      fail("Should not have created urn " + urn)

    } catch {

     case e: java.lang.Exception => {

       assert(e.getMessage().contains(msg))
     }
   }
  }


  it should "throw an exception if there are empty components within the first node of a range reference" in {
    val dottedRange = "urn:cts:greekLit:tlg0012.tlg001.msA:1...1-1.7"
    val msg = s"Invalid URN syntax: repeated periods in passage component of ${dottedRange}"
    try {
     val urn = CtsUrn(dottedRange)
     fail("Should not have created urn " + urn)
   } catch {
     case e: java.lang.Exception => {
       assert(e.getMessage().contains(msg))
     }
   }
  }

  it should "throw an exception if there are empty components within the second node of a range reference" in {
    val dottedRange2 = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1...7"
    val msg = s"Invalid URN syntax: repeated periods in passage component of ${dottedRange2}"
    try {
     val urn = CtsUrn(dottedRange2)
     fail("Should not have created urn " + urn)
   } catch {
     case e: java.lang.Exception => assert(e.getMessage().contains(msg))
   }
 }

  it should "throw an exception if there are empty components within the work reference" in {


    val dottedWork = "urn:cts:greekLit:tlg0012..tlg001:1.1"
    val msg = s"Invalid URN syntax in work component of ${dottedWork}"
    try {
      val urn = CtsUrn(dottedWork)
      fail("Should not have created urn " + urn)
     } catch {
       case e: java.lang.Exception => assert(e.getMessage().contains(msg))
     }
  }

  it should "throw an exception if there are leading periods in the passage component" in {

    val badPassage = "urn:cts:greekLit:tlg0012.tlg001:.1.1"
    val msg = s"Invalid URN syntax: leading period in passage component of ${badPassage}"
    try {
      val urn = CtsUrn(badPassage)
      fail("Should not have created URN with bad passage component inluding leading period")
    } catch {
      case iae: java.lang.IllegalArgumentException => {
        assert(iae.getMessage().contains(msg))
      }
    }
  }

  it should "throw an exception if there are trailing periods in the passage component" in {
    val trailingDot = "urn:cts:greekLit:tlg0012.tlg001:1.1."
    val msg = s"Invalid URN syntax in passage component of ${trailingDot}"
    try {
      val urn = CtsUrn(trailingDot)
      fail("Should not have created URN with bad passage component inluding trailing period")
    } catch {
      case e: java.lang.Exception =>  assert(e.getMessage().contains(msg))
    }
  }

  it should "throw an exception if there are leading periods in the range beginning part" in {
    val illegalLeadingDot = "urn:cts:greekLit:tlg0012.tlg001:.1-12"
    val msg = s"Invalid URN syntax: leading period in passage component of ${illegalLeadingDot}"

    try {
      val urn = CtsUrn(illegalLeadingDot)
      fail("Should not have created URN with bad range reference including leading period")
    } catch {
      case e: java.lang.Exception =>  assert(e.getMessage().contains(msg))
    }
  }
  it should "throw an exception if there are leading periods in the range ending part" in {
    val illegalLeadingDot = "urn:cts:greekLit:tlg0012.tlg001:1-.12"
    val msg = s"Invalid URN syntax: leading period in range ending of ${illegalLeadingDot}"

    try {
      val urn = CtsUrn(illegalLeadingDot)
      fail("Should not have created URN with bad range reference including leading period")
    } catch {
      case e: java.lang.Exception =>  assert(e.getMessage().contains(msg))
    }
  }


  it should "throw an exception if there are trailing periods in the range beginning part" in  {
    val illegalTrailingDot = "urn:cts:greekLit:tlg0012.tlg001:1.-12"
    val msg = s"Invalid URN: trailing period on range beginning of ${illegalTrailingDot}"

    try {
      val urn = CtsUrn(illegalTrailingDot)
      fail("Should not have created URN with bad range reference including trailing period")
    } catch {
      case e: java.lang.Exception =>  {
        assert(e.getMessage().contains(msg))
      }
    }
  }


  it should "throw an exception if there are trailing periods in the range ending part" in {
    val illegalTrailingDot = "urn:cts:greekLit:tlg0012.tlg001:1-12."
    val msg = s"Invalid URN: trailing period on range ending reference of ${illegalTrailingDot}"

    try {
      val urn = CtsUrn(illegalTrailingDot)
      fail("Should not have created URN with bad range reference including trailing period")
    } catch {
      case e: java.lang.Exception =>  assert(e.getMessage().contains(msg))
    }
  }


  it should "identify an empty passage as neither range nor node" in {
    val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    assert (urn.isRange == false)
    assert (urn.isPoint == false)
  }



  it should "throw an exception if the wrong number of components are give n" in {
    val bogus = "NOT_A_URN"
    val msg = s"Unable to parse URN string ${bogus}"
    try {
      val u = CtsUrn(bogus)
      fail("Should not have made a URN from " + u)
    } catch {
      case e: java.lang.Exception => assert (e.getMessage().contains(msg))

    }
  }

}
