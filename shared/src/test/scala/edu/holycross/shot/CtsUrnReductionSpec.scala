package edu.holycross.shot.cite

import org.scalatest.FlatSpec



class CtsUrnReductionSpec extends FlatSpec {

  "A CTS URN" should "be able to reduce a URN to its text group" in   {
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg5026:")
    assert(urn.toTextGroup == expected)
  }

  it should "be able to reduce a URN to its work" in {
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    assert(urn.toWork == expected)
  }

  it should "return the next lowest level of the hierarchy if no work is included" in {
    val urn = CtsUrn("urn:cts:greekLit:tlg5026:")
    val expected = CtsUrn("urn:cts:greekLit:tlg5026:")
    assert(urn.toWork == expected)
  }

  it should "be able to reduce a URN to its version" in {
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert(urn.toVersion == expected)
  }

  it should "return the next lowest level of the hierarchy if no version is included" in {
    val groupUrn = CtsUrn("urn:cts:greekLit:tlg5026:")
    val expectedGroup = CtsUrn("urn:cts:greekLit:tlg5026:")
    assert(groupUrn.toVersion == expectedGroup)

    val workUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    val expectedWork = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    assert(workUrn.toVersion == expectedWork)
  }



  it should "be able to reduce a URN to its exemplar" in {
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt.tokens:1.1.1")
    val expected = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt.tokens:")
    assert(urn.toExemplar == expected)
  }

  it should "return the next lowest level of the hierarchy if no exemplar is included" in {
    val groupUrn = CtsUrn("urn:cts:greekLit:tlg5026:")
    val expectedGroup = CtsUrn("urn:cts:greekLit:tlg5026:")
    assert(groupUrn.toExemplar == expectedGroup)

    val workUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    val expectedWork = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    assert(workUrn.toExemplar == expectedWork)


    val versionUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    val expectedVersion = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert(versionUrn.toExemplar == expectedVersion)
  }

}
