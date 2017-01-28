package edu.holycross.shot


package cite {

  /** The superclass of Urn objects, implemented by CtsUrn and CiteUrn.
  */
  abstract class Urn {
    def urnMatch(u: Urn): Boolean
  }

  /** Trait for any citable scholarly resource.
  *
  * Implementing classes must have a Urn value
  * identifying the object.
  */
  trait Citable {
    val urn: Urn
  }

}
