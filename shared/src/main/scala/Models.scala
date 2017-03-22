package edu.holycross.shot


package cite {

  /** The superclass of Urn objects, implemented by CtsUrn and CiteUrn.
  */
  abstract class Urn {
    def ~~(u: Urn): Boolean
  }


  class CiteExport(urn: Urn )
  /** Trait for any citable scholarly resource.
  *
  * Implementing classes must have a Urn value
  * identifying the object, and a CitableExport function
  * delivering a two-column string labelling URNs with a type.
  */
  trait Citable {
    def urn: Urn
    def citeExport: Vector[CiteExport]
  }

}
