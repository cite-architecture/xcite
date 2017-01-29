package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class CtsUrnRegExSpec extends FlatSpec {


  "The package object's subref option " should "extract the subreference part from a passage node"  in {
    subrefOption("1.1@μῆνιν") match {
      case None => fail("Should have extracted subreference")
      case subr: Some[String] => assert(subr.get == "μῆνιν")
    }
  }
  it should "match nothing when no subreference is given" in {
    subrefOption("1.1") match {
      case None => assert(true)
      case subr: Some[String] => fail("Should not have matched " + subr)
    }
  }

  "The package object's subref text option" should "extract the text  component of a subreference" in {
    subrefTextOption("1.1@μῆνιν[1]") match {
      case None => fail("Should have extracted subreference")
      case subr: Some[String] => assert(subr.get == "μῆνιν")
    }
  }
  it should "extract the text contents of a subreference when it is implicitly indexed to 1" in {
    subrefTextOption("1.1@μῆνιν") match {
      case None => fail("Should have extracted subreference")
      case subr: Some[String] => assert(subr.get == "μῆνιν")
    }
  }
  it should "match nothing when no subref is given" in {
    subrefTextOption("1.1") match {
      case None => assert(true)
      case subr: Some[String] => fail("Should not have matched " + subr)
    }
  }

  "The package object's subref index option" should "extract an integer value from a subreference" in {
    subrefIndexOption("1.1@μῆνιν[1]") match {
      case None => fail("Should have extracted subreference index")
      case idx: Some[Int] => assert(idx.get == 1)
    }
  }

  it should "match nothing when no subref is given" in {
    subrefIndexOption("1.1") match {
      case None => assert(true)
      case idx: Some[Int] => fail("Should not have matched " + idx)
    }
  }

  it should "return 1 when the subreference exists but is not explicitly  indexed" in {
    subrefIndexOption("1.1@μῆνιν") match {
      case None => fail("Should have extracted subreference index")
      case idx: Some[Int] => assert(idx.get == 1)
    }
  }



  "The package object's text matcher RE" should "extract text contents when subref has no index" in {
    "μῆνιν" match {
      case subrefTextRE(sub) => assert (sub == "μῆνιν")
      case _ => fail("Failed to match text contents of subreference")
    }
  }
  it should "extract text contents when the subref has an index" in {
    "μῆνιν[1]" match {
      case subrefTextRE(sub) => assert (sub == "μῆνιν")
      case _ => fail("Failed to match text contents of subreference")
    }
  }
  "The package object's index matcher RE" should "extract an index string when the subref has an index" in {
    "μῆνιν[1]" match {
      case subrefIndexRE(idx) => assert (idx == "1")
      case _ => fail("Failed to match index value of subreference")
    }
  }
  it should "match nothing when then subref does not have an index" in {
    "μῆνιν" match {
      case subrefIndexRE(idx) => fail("Should not have matched string " + idx)
      case s => { assert(s == "μῆνιν") }
    }
  }

}
