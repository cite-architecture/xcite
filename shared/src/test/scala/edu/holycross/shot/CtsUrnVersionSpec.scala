package edu.holycross.shot.cite

import org.scalatest.FlatSpec

class CtsUrnVersionSpec extends FlatSpec {

  val iliad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
  "A CtsUrn" should "be able to drop the version component from a version-level value" in {
    val versionLevel = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")
    assert(versionLevel.dropVersion == iliad)
  }

  it should "be able to drop the version component from an exemplar-level value" in {
    val exemplarLevel = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.pnormal:")
    assert(exemplarLevel.dropVersion == iliad)
  }
  


}
