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

  it should "throw an IllegalArgumentException if the the URN string has too few components" in {
    try {
      CtsUrn("urn:cts:greekLit:tlg0012.tlg001")
      fail("Should not have formed URN")
    } catch {
      case ex : IllegalArgumentException => assert(ex.getMessage() == "requirement failed: invalid URN syntax: urn:cts:greekLit:tlg0012.tlg001. Wrong number of components.")
      case unknown: Throwable => fail("Unrecognized exception " + unknown)
    }
   }

   it should "throw a CiteException if the the URN string has a trailing :" in {
     try {
       val u = CtsUrn("urn:cts:greekLit:tlg0012:tlg001:")
       fail("Should not have formed URN with " + u.components.size + " components.")
     } catch {
       case ex : IllegalArgumentException => assert(ex.getMessage() == "requirement failed: invalid URN syntax: urn:cts:greekLit:tlg0012:tlg001:. Wrong number of components.")
       case ce : CiteException => assert(ce.message == "Invalid URN syntax: trailing colon in urn:cts:greekLit:tlg0012:tlg001:")
     }
  }

  it should "throw an IllegalArgumentException if the `urn` component is missing" in {

    try {
      CtsUrn("XX:cts:greekLit:tlg0012.tlg001:")
      fail("Should not have formed URN")
    } catch {
      case e: IllegalArgumentException => assert (e.getMessage() == "requirement failed: invalid URN syntax: XX:cts:greekLit:tlg0012.tlg001:. First component must be 'urn'.")
      case unknown: Throwable => fail("Unrecognized exception " + unknown)
    }
  }
  it should "throw an IllegalArgumentException if the `cts` component is missing" in {
    try {
      CtsUrn("urn:XXX:greekLit:tlg0012.tlg001:")
      fail("Should not have formed URN")
    } catch {
      case e: IllegalArgumentException => assert (e.getMessage() == "requirement failed: invalid URN syntax: urn:XXX:greekLit:tlg0012.tlg001:. Second component must be 'cts'.")
    }
  }

  // Syntax of work component:
  it should "guarantee that the work component is non-empty" in {
    val iliad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    assert (iliad.workComponent.nonEmpty)
  }
  it should "throw an IllegalArgumentException if a work component has more than 4 parts" in {
    try {
     CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.tokens.subexemplar:")
     fail("Should not have formed URN")
   } catch {
     case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: invalid URN syntax. Too many parts in work component tlg0012.tlg001.msA.tokens.subexemplar")
     case unknown: Throwable => fail("Unrecognized exception " + unknown)
   }
  }


  // Syntax of passage component:
  it should "throw a CiteException if a range has an empty first node" in {
    try {
      CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:-1.10")
      fail("Should not have formed URN")
    } catch {
      case e: CiteException => assert(e.getMessage() == "No range beginning defined in urn:cts:greekLit:tlg0012.tlg001.msA:-1.10")
      case unknown: Throwable => fail("Unrecognized exception " + unknown)
    }
  }
  it should "throw an IllegalArgumentException if a range has an empty second node" in {
    try {
      CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-")
      fail("Should not have formed URN")
    } catch {
      case e: IllegalArgumentException => assert (e.getMessage() == "requirement failed: Invalid URN syntax.  Error in passage component 1.1-")
      case unknown: Throwable => fail("Unrecognized exception " + unknown)
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
    try {
     val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1...10")
     fail("Should not have created urn " + urn)
   } catch {
     case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: invalid passage syntax in urn:cts:greekLit:tlg0012.tlg001.msA:1...10")
     case otherE: Throwable => throw CiteException("Unexpected exception: " + otherE)
   }
  }


  it should "throw an exception if there are empty components within the first node of a range reference" in {
    try {
     val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1...1-1.7")
     fail("Should not have created urn " + urn)
   } catch {
     case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: invalid passage syntax in range beginning of urn:cts:greekLit:tlg0012.tlg001.msA:1...1-1.7")
     case otherE: Throwable => throw CiteException("Unexpected exception: " + otherE)
   }
  }
  it should "throw an exception if there are empty components within the second node of a range reference" in {
    try {
     val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1...7")
     fail("Should not have created urn " + urn)
   } catch {
     case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: invalid passage syntax in range ending of urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1...7")
     case otherE: Throwable => throw CiteException("Unexpected exception: " + otherE)
   }
  }

  it should "throw an exception if there are empty components within the work reference" in {
    try {
      val urn = CtsUrn("urn:cts:greekLit:tlg0012..tlg001:1.1")
      fail("Should not have created urn " + urn)
      } catch {
        case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: invalid work syntax in urn:cts:greekLit:tlg0012..tlg001:1.1")
      }
  }
  it should "throw an exception if there are leading periods in the passage component" in {
    try {
      val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:.1.1")
      fail("Should not have created URN with bad passage component inluding leading period")
    } catch {
      case e: IllegalArgumentException => assert (e.getMessage() == "requirement failed: invalid passage syntax in urn:cts:greekLit:tlg0012.tlg001:.1.1")
      case exc: Throwable => fail("Should have thrown IllegalArgumentException, not " + exc.getMessage())
    }

  }
  it should "throw an exception if there are trailing periods in the passage component" in {
    try {
      val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1.")
      fail("Should not have created URN with bad passage component inluding trailing period")
    } catch {
      case e: CiteException => assert (e.message == "Invalid URN syntax in passage component 1.1.: trailing period.")
      case exc  : Throwable => fail("Should have thrown CiteException, not " + exc.getMessage())
    }
  }

  it should "throw an exception if there are leading periods in the range beginning part" in {
    try {
      val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:.1-12")
      fail("Should not have created URN with bad range reference including leading period")
    } catch {
      case e: IllegalArgumentException => assert (e.getMessage() == "requirement failed: invalid passage syntax in range beginning of urn:cts:greekLit:tlg0012.tlg001:.1-12")
      case exc  : Throwable => fail("Should have thrown IllegalArgumentException, not " + exc.getMessage())
    }
  }
  it should "throw an exception if there are trailing periods in the range beginning part" in {
    try {
      val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.-12")
      fail("Should not have created URN with bad range reference including trailing period")
    } catch {
      case e: CiteException => assert (e.getMessage() == "Invalid URN: trailing period on range beginning reference 1.")
      case exc  : Throwable => fail("Should have thrown CiteException, not " + exc.getMessage())
    }
  }

  it should "throw an exception if there are leading periods in the range ending part" in {
    try {
      val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1-.12")
      fail("Should not have created URN with bad range reference including leading period")
    } catch {
      case e: IllegalArgumentException => assert (e.getMessage() == "requirement failed: invalid passage syntax in range ending of urn:cts:greekLit:tlg0012.tlg001:1-.12")
      case exc  : Throwable => fail("Should have thrown IllegalArgumentException, not " + exc.getMessage())
    }
  }
  it should "throw an exception if there are trailing periods in the range ending part" in {
    try {
      val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1-12.")
      fail("Should not have created URN with bad range reference including trailing period")
    } catch {
      case e: CiteException => assert (e.getMessage() == "Invalid URN syntax in passage component 1-12.: trailing period.")
      case exc  : Throwable => fail("Should have thrown CiteException, not " + exc.getMessage())
    }
  }


  it should "identify an empty passage as neither range nor node" in {
    val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    assert (urn.isRange == false)
    assert (urn.isPoint == false)
  }



    it should "throw an exception if the wrong number of components are give n" in {

        try {
          val u = CtsUrn("NOT_A_URN")
          fail("Should not have made a URN from " + u)
        } catch {
          case iae: IllegalArgumentException => assert (iae.getMessage() == "requirement failed: Invalid URN syntax: too few components in NOT_A_URN")
          case t : Throwable =>       fail("Should have thrown an IllegalArgumentException instead of " + t)
        }
      }

}
