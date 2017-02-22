package edu.holycross.shot.cite

object Main {
  def main(args: Array[String]): Unit = {

    val mini = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    println(mini)

    println("Match function: " + mini.~~(CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1")))

  }
}
