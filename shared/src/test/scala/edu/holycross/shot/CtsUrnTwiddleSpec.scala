
package edu.holycross.shot.cite


import org.scalatest.FlatSpec

class CtsUrnTwiddleSpec extends FlatSpec {


  "A CtsUrn" should "twiddle" in {
    val deep = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:")
    val shallow = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    val deepLTshallow = deep <= shallow
    assert(deepLTshallow)
  }

  it should "reject equating different urns, duh" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msAim.hmt:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA:")
    val cf = u1 <= u2
    assert(cf == false)
  }

 it should "twiddle without spamming the console with errors" in {
    val containingUrn = CtsUrn("urn:cts:greekLit:group.work:1.1")
    val containedUrn = CtsUrn("urn:cts:greekLit:group.work:1.1.lemma")
    assert( containingUrn ~~ containedUrn)
  }

  it should "identify containment of URNs with no passage components and including exemplars" in  {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u2 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1.tokens:")

    assert( u1 >= u2)
    assert( (u2 >= u1) == false)

    assert((u1 <= u2) == false)
    assert( u2 <= u1)
  }

  it should "identify containment of URN with passage components" in  {
    val u3 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:1.1")
    val u4 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1.tokens:1.1")

    assert( u3 >= u4)
    assert( (u4 >= u3) == false)
    assert( u4 <= u3)
    assert((u3 <= u4) == false)
  }

  it should "identify containment of URN with mixesof passage components and no passage components" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u4 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1.tokens:1.1")

   assert(u1 >= u4)
   assert((u4 >= u1) == false)
   assert(u4 <= u1)
   assert((u1 <= u4) == false)
  }

  it should "recognize the same urn as both >= and <= to itself" in {
    val u1 = CtsUrn("urn:cts:greekLit:tlg5026.msA.v1:")
    val u2 = u1
    assert (u1 <= u2)
    assert(u2 >= u1)
  }
}
