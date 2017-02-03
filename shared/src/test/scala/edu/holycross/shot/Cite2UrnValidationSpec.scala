package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class Cite2UrnValidationSpec extends FlatSpec {

  "A Cite2Urn constructor" should "construct a URN object from a well-formed string with object identifer" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    urn match {
      case u: Cite2Urn => assert(true)
      case _ => fail("Failed to construct Cite2Urn")
    }
  }

  it should "construct a URN object from a well-formed string with no object identifer" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    urn match {
      case u: Cite2Urn => assert(true)
      case _ => fail("Failed to construct Cite2Urn")
    }
  }
  it should "throw an IllegalArgumentException if there are too few top-level components" in {
     try {
      Cite2Urn("urn:cite2:hmt:")
      fail("Should not have created too short URN")
     } catch {
       case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: wrong number of components in  urn:cite2:hmt: - 3")
       case ex : Throwable => fail("Constructor should have thrown an IllegalArgumentException exception " + ex)
     }
  }
  it should   "throw an IllegalArgumentException if there are too many top-level components" in  {
    try {
      Cite2Urn("urn:cite2:hmt:msA:12r:subobject")
      fail("Should not have created too long URN")
    } catch {
      case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: wrong number of components in  urn:cite2:hmt:msA:12r:subobject - 6")
      case ex: Throwable => fail ("Unrecognized error: " + ex)
   }
  }
 it should  "throw an IllegalArgumentException if 'urn' component is missing" in {
   try {
    Cite2Urn("XX:cite:hmt:msA:12r")
    fail("Should not have created URN without 'urn' component")
  } catch {
    case e: IllegalArgumentException => assert(true)
    case ex: Throwable => fail("Unrecognized exception " + ex)
    }
  }
  it should "throw an IllegalArgumentException if required 'cite' component is missing" in  {
     try {
       Cite2Urn("urn:XX:hmt:msA:12r")
       fail("Should not have created URN without 'urn' component")
     } catch {
       case e: java.lang.IllegalArgumentException => assert(true)
       case ex: Throwable => fail("Unrecognized exception: " + ex)
    }
  }
  it should "throw an IllegalArgumentException if the required collection hierarchy exceeds 2 levels" in {
    try {
     Cite2Urn("urn:cite2:hmt:msA.12r.v1.subversion:")
     fail("Shold not have created URN with too long collection hierarchy")
   } catch {
     case e: java.lang.IllegalArgumentException => assert(true)
     case ex: Throwable => fail("Unrecognized exception: " + ex)
   }
  }

  it should "have a non-empty namespace" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    assert (urn.namespace == "hmt")
  }
  it should "have a non-empty hierarchical collection identifier" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    assert (urn.collection == "msA")
  }
  it should "allow a none option for object component" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    urn.objectComponentOption match {
      case None => assert(true)
      case _ => fail("Should have created none option")
    }
  }

  it should "allow a property-level identifier on the collection component" in {
      val urn = Cite2Urn("urn:cite2:hmt:msA.release1.rv:")
      assert (urn.collection == "msA")
  }

  it should "identify a range reference as a range and not a node" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r-24v")
    assert (urn.isRange)
  }
  it should "identify a node reference as a node and not a range" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:12r")
    assert (urn.isObject)
  }
  it should "identify a none optoion for passage as neither range nor node" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.release1:")
    assert (urn.noObject)
  }
}
