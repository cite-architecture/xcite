package edu.holycross.shot.cite

import org.scalatest.FlatSpec



class CtsUrnMatchingSpec2 extends FlatSpec {

  "CTS URN matching" should  "identify two URNs with the same work component as URN-similar if either passage component is empty" in  pending /*{
    val passageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val noPassageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert  (passageUrn ~~ noPassageUrn)
    assert (noPassageUrn ~~ passageUrn)
  }*/

  it should "recognize that empty passages do not contain another passage" in {
    val noPsg = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val hasPsg = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1.2")
    assert(noPsg.passageContains(hasPsg) == false)
    assert(hasPsg.passageContains(noPsg) == false)
  }

  it should "identify >= relation of URNs without passage components" in  {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1.2")

    println("\n\nCF " + u1 + " >= " + u2)
    assert( u1 >= u2)
    println("\n\n NOW CF " + u2 + " >= " + u1)
    assert((u2 >= u1) == false)

    val u3 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v2:")
    assert( (u1 >= u3) == false)
    assert((u3 >= u1) == false)
    assert( (u3 >= u2) == false)
  }



  it should "identify containment of URNs without passage components and including exemplars" in  pending /*{
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1.tokens:")
    assert( u1 >= u2)
    assert( !(u2 >= u1))

    assert( u2 <= u1)
    assert( !(u1 <= u2))
  }*/

  it should "identify containment of URNs with and without passage components and including exemplars" in  pending /*{
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1.tokens:")
    val u3 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1.1")
    val u4 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1.tokens:1.1")
    assert( u1 >= u2)
    assert( !(u2 >= u1))
    assert( u3 >= u4)
    assert(u1 >= u4)
    assert( !(u4 >= u3) )

    assert( u2 <= u1)
    assert( !(u1 <= u2))
    assert( u4 <= u3)
    assert(u4 <= u1)
    assert( !(u3 <= u4) )
  }
*/


  it should "recognize that a unindexed node is URN-similar to an identical unindexed endpoint in a range" in pending
  /*
    val simpleSpanningRef = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1-2.1")
    assert (nodeUrn ~~ simpleSpanningRef)
  }*/


}
