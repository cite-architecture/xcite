package edu.holycross.shot.cite

import org.scalatest.FlatSpec



class CtsUrnMatchingSpec extends FlatSpec {

  "CTS URN matching" should "determine if a passage reference in one URN contains the passage hierarchy of another URN" in   {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1.lemma")
    assert(containingUrn.passageContains(containedUrn))
  }

  it should "properly protect periods in comparing passage references" in {
    val u1 = CtsUrn("urn:cts:citedemo:arabic.quran.v1:1.1")
    val u2 = CtsUrn("urn:cts:citedemo:arabic.quran.v1:111.7")

    assert( u1.passageContains(u2)  == false)
    assert( u2.passageContains(u1)  == false)
  }

  it should "determine whether two URNs' passage references are URN-similar" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1.lemma")
    assert(containedUrn.passageMatch(containingUrn))
    assert(containingUrn.passageMatch(containedUrn))
  }

  it should "determine if a work reference in one URN contains the work hierarchy of another URN" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    assert(containedUrn.workContains(containingUrn))
  }

  it should "determine if a work reference in one URN contains the work hierarchy of another URN, when there is an exemplar" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt.tokens:1.1")
    assert(containedUrn.workContains(containingUrn))
  }

  it should "properly protect periods in comparing work references" in {
    val u1 = CtsUrn("urn:cts:testurns:group.txt1.ed:1")
    val u2 = CtsUrn("urn:cts:testurns:group.txt11.ed:1")

    assert( u1.workContains(u2)  == false)
    assert( u2.workContains(u1)  == false)
  }
  it should "determine if two URNs' work references are URN-similar" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1")
    assert(containedUrn.workMatch(containingUrn))
    assert(containingUrn.workMatch(containedUrn))
  }

  it should "identify two URNs differing only in the depth of work hierarchy as URN-similar" in {
    val notionalUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    val editionUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    assert  (notionalUrn ~~ editionUrn )
    assert (editionUrn ~~ notionalUrn)
  }

  it should "identify containment direction in two URNs differing only in the depth of work hierarchy" in {
    val notionalUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    val editionUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    assert  (notionalUrn > editionUrn )
    assert (editionUrn < notionalUrn)
  }

  it should "identify two URNs differing only in the depth of the passage hierarchy as URN-similar, commutatively" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1.lemma")
    assert  (containingUrn ~~ containedUrn)
    assert (containedUrn ~~ containingUrn)
  }


  it should "identify two URNs differing only in CTS namespace as non-similar, commutatively" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val u2 = CtsUrn("urn:cts:myOwnNs:tlg5026.msA:1.1")
    assert (u1 >< u2)
    assert (u2 >< u1)
  }

  it should "identify two URNs with conflicting passage values as non-similar, commutatively" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:2.1")
    assert (u1 >< u2)
    assert (u2 >< u1)
  }

  it should "identify two URNs with conflicting work values as non-similar, commutatively" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v2:1")
    assert (u1 >< u2)
    assert (u2 >< u1)
  }

  it should "identify two URNs differing both in the depth of the passage hierarchy and depth of the work hierarchy when containment is maintained" in {
    val urn1 = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val urn2 = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1.lemma")
    assert  (urn1 ~~ urn2)
    assert (urn2 ~~ urn1)
  }

  it should "identify two URNs with the same work component as URN-similar if either passage component is empty" in {
    val passageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val noPassageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert  (passageUrn ~~ noPassageUrn)
    assert (noPassageUrn ~~ passageUrn)
  }


  it should "identify two URNs with URN-similar passage components as URN-similar if either passage component is empty" in {
    val passageUrn = CtsUrn("urn:cts:greekLit:tlg5026:1.1")
    val noPassageUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    assert  (passageUrn.~~(noPassageUrn))
    assert (noPassageUrn.~~(passageUrn))
  }


  it should "identify indexed ranges within a single node as URN-similar to the node" in {
    val nodeUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val subrefRange = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1@μῆνιν-1.1@ἄ")
    assert( nodeUrn ~~ subrefRange)
  }

  it should "recognize that an indexed node in a range is not URN-similar to the single node" in {
    val nodeUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA:1.1")
    val indexedSpanningRef = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1@μῆνιν-2.1@οὐλομένην")
    assert ((nodeUrn ~~ indexedSpanningRef) == false)

  }



  it should "take account of both work and passage hierarchy in the containment functions >= and <=" in {
    val u1 = CtsUrn("urn:cts:ns:tg.w.v1:1.2.1")
    val u2 = CtsUrn("urn:cts:ns:tg.w.v1:1.2")

    assert(u1.passageContains(u2) == false)
    assert(u2.passageContains(u1))
    assert( u2 >= u1)
    assert( !(u1 >= u2))
    assert( u1 <= u2)
    assert( !(u2 <= u1))
  }
 
  it should "identify containment of URNs with passage components" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1.2")
    assert( u1 >= u2)
    assert( !(u2 >= u1))
    assert( u2 <= u1)
    assert( !(u1 <= u2))
  }

  it should "identify containment of URNs without passage components" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1.2")
    val u3 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v2:")
    assert( u1 >= u2)
    assert( !(u2 >= u1))
    assert( !(u1 >= u3))
    assert( !(u3 >= u1))
    assert( !(u3 >= u2))
  }

  it should "identify containment of URNs without passage components and including exemplars" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1.tokens:")
    assert( u1 >= u2)
    assert( !(u2 >= u1))

    assert( u2 <= u1)
    assert( !(u1 <= u2))
  }

  it should "identify containment of URNs with and without passage components and including exemplars" in {
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



  it should "recognize that a unindexed node is URN-similar to an identical unindexed endpoint in a range" in pending
  /*
    val simpleSpanningRef = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.1-2.1")
    assert (nodeUrn ~~ simpleSpanningRef)
  }*/


}
