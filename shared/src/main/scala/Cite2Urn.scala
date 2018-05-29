package edu.holycross.shot
import scala.scalajs.js
import scala.scalajs.js.annotation._

package cite {


  /** A URN for a citable object in a collection.
  *
  * @constructor create a new Cite2Urn.  The long
  * constructor validates a submitted string
  * against the complex syntactic requirements of the CITE2 URN
  * specification, and defines a number of functions for manipulating
  * CITE2 URN values.
  * @param urnString String representation of Cite2Urn validating
  * against the Cite2Urn specification
  */
  @JSExportAll  case class Cite2Urn (val urnString: String) extends Urn {

    require(urnString.endsWith("-") == false, "URN cannot end with trailing -")
    /** Array of top-level, colon-delimited components.
    */
    val components = urnString.split(":")
    if (components.size == 4) {
      require(urnString.endsWith(":") == true, s"${urnString} is invalid: collection component must be separated from empty object selection with :")
    } else {
      require((components.size == 5), s"wrong number of components in  '${urnString}' (${components.size})")
    }

    /////////// Validate component-level syntax of submitted String:
    require(components(0) == "urn", "invalid URN syntax: " + urnString + ". First component must be 'urn'.")
    require(components(1) == "cite2", "invalid URN syntax: " + urnString + ". Second component must be 'cite2'.")

    /** Required namespace component of the URN.*/
    val namespace = components(2)


    /////////// Validate collection component
    /** Required work component of the URN.*/
    val collectionComponent = components(3)
    val collectionParts = collectionComponent.split("""\.""").toVector
    val collection = collectionParts(0)


    /** Optional version part of collection component.
    */
    def versionOption: Option[String] = {
      collectionParts.size match {
        case 2 => Some(collectionParts(1))
        case 3 => Some(collectionParts(1))
        case _ => None
      }
    }

    /** String value of optional version part of
    * collection component.
    */
    def version = {
      try {
        versionOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No version defined in " + urnString)
      }
    }

    /** Optional property part of collection component.
    */
    def propertyOption: Option[String] = {
      collectionParts.size match {
        case 3 => Some(collectionParts(2))
        case _ => None
      }
    }

    /** String value of optional property part of
    * collection component.
    */
    def property = {
      try {
        propertyOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No property defined in " + urnString)
      }
    }

    require((1 until 4 contains collectionParts.size), "invalid syntax in collection component of " + urnString + "; wrong size collectionParts = " + collectionParts.size)

    for (p <- collectionParts) {
      require(p.nonEmpty, "invalid value: empty value in collection component in " + urnString)
    }

    //
    /////////// End collection component validation.
    //
    /////////// Validate object selector component.

    /** Optional object selector component.
    */
    def objectComponentOption: Option[String] = {
      components.size match {
        case 5 => Some(components(4))
        case _ => None
      }
    }

    /** String value of optional object selector component.
    */
    def objectComponent = {
      try {
        objectComponentOption.get
      } catch {
        case e: NoSuchElementException => throw CiteException("No object component defined in " + urnString)
        case otherE: Throwable => "ERROR: " + otherE
      }
    }

    /** For non-empty object component, hyphen-separated
    * parts allowed in ordered collections.
    */
    val objectParts: Vector[String] = {
      objectComponentOption match {
        case None => Vector.empty[String]
        case s: Some[String] => {
          val parts = s.get.split("-")
          parts.size match {
            case 2 => parts.toVector
            case 1 => parts.toVector
            case _ => throw CiteException("Invalid object component syntax: " + urnString)
          }
        }
      }
    }

    /** True if URN identifies a range within
    * an ordered collection.
    */
    val isRange = {
      (objectParts.size == 2)
    }

    /** True if URN includes a selector
    * for a single object.
    */
    val isObject = {
      objectParts.size == 1
    }

    /** True if URN does not include an
    * object selector component.
    */
    val noObject = {
      objectParts.isEmpty
    }


    /** Single object identifier as an Option.
    */
    val objectOption: Option[String] = {
      objectParts.size match {
        case 1 => Some(objectParts(0))
        case _ => None
      }
    }


    /** First part of range identifier as an Option.
    */
    val rangeBeginOption : Option[String] = {
      objectParts.size match {
        case 2 => Some(objectParts(0))
        case _ => None
      }
    }

    /** String value of first part of range identifier.
    */
    def rangeBegin: String = {
      try {
        rangeBeginOption.get
      } catch {
        case e: NoSuchElementException => throw CiteException("No range beginning defined in " + urnString)
        case ex: Throwable => throw(ex)
      }
    }

    /** Second part of range identifier as an Option.
    */
    val rangeEndOption : Option[String] = {
      objectParts.size match {
        case 2 => Some(objectParts(1))
        case _ => None
      }
    }

    /** String value of second part of range identifier.
    */
    def rangeEnd = {
      try {
        rangeEndOption.get
      } catch {
        case e: NoSuchElementException => throw CiteException("No range ending defined in " + urnString)
        case ex: Throwable => throw(ex)
      }
    }

    // final requirements for syntax of object selector
    require(((objectParts.size >= 0) && (objectParts.size <= 2)), "invalid syntax in object component of " + urnString)
    if (isObject) {
      require ((urnString.contains("-") == false),"invalid range syntax in object component of " + urnString)
    } else {
      if (isRange) {
        require (urnString.contains("-"), "invalid range syntax in object component of " + urnString)
      } else {}
    }
    for (p <- objectParts) {
      require(p.nonEmpty, "invalid value: empty value in object component in " + urnString)
    }

    // extended references
    def singleObjectParts: Vector[String] = {

      objectParts.size match {
        case 1 => {
          val objParts = objectParts(0).split("@")
          if (objParts.size > 2) {
            throw CiteException("Invalid extended reference in " + urnString)
          } else {
            objParts.toVector
          }
        }
        case _ => Vector.empty[String]
      }

    }
    def rangeBeginParts: Vector[String] = {
      if (objectParts.isEmpty) {
        Vector.empty[String]
      } else {
        if (isRange) {
          val rbegin = rangeBegin.split("@").toVector
          if (rbegin.size > 2) {
            throw CiteException("Invalid extended reference on range beginning part of " + urnString)
          } else {
            rbegin
          }

        } else {
          Vector.empty[String]
        }
      }
    }
    def rangeEndParts: Vector[String] = {
      if (objectParts.isEmpty) {
        Vector.empty[String]
      } else {
        if (isRange) {
          val rend = rangeEnd.split("@").toVector
          if (rend.size > 2) {
            throw CiteException("Invalid extended reference on range ending part of " + urnString)
          } else {
            rend
          }

        } else {
          Vector.empty[String]
        }
      }
    }


    def rangeBeginUrn: Cite2Urn = {
      if (this.isRange) {
        Cite2Urn(this.dropSelector + this.rangeBegin)
      } else {
        throw CiteException(s"Function rangeBeginUrn only applicable to range expressions: ${urnString}")
      }
    }

    def rangeEndUrn: Cite2Urn = {
      if (this.isRange) {
        Cite2Urn(this.dropSelector + this.rangeEnd)
      } else {
        throw CiteException(s"Function rangeEndUrn only applicable to range expressions: ${urnString}")
      }
    }

    def objectExtensionOption : Option[String] = {
      singleObjectParts.size match {
        case 0 => None
        case 1 => None
        case 2 => Some(singleObjectParts(1))
      }
    }
    def objectExtension = {
      objectExtensionOption match {
        case None => throw CiteException("No extended reference in " + urnString)
        case s: Some[String] => s.get
      }
    }



    def rangeBeginExtensionOption : Option[String] = {
      rangeBeginParts.size match {
        case 0 => None
        case 1 => None
        case 2 => Some(rangeBeginParts(1))
      }
    }
    def rangeBeginExtension = {
      rangeBeginExtensionOption match {
        case None => throw CiteException("No extended reference in range beginning of " + urnString)
        case s: Some[String] => s.get
      }
    }

    def rangeEndExtensionOption : Option[String] = {
      rangeEndParts.size match {
        case 0 => None
        case 1 => None
        case 2 => Some(rangeEndParts(1))
      }
    }
    def rangeEndExtension = {
      rangeEndExtensionOption match {
        case None => throw CiteException("No extended reference in range ending of " + urnString)
        case s: Some[String] => s.get
      }
    }



    /** Trims any extended citation components off of
    * passage or range selectors.
    */
    def dropExtensions: Cite2Urn = {
      val baseStr = Vector("urn","cite2",namespace,collectionComponent).mkString(":")

      if (isRange) {
        Cite2Urn(baseStr + ":" + rangeBeginParts(0) + "-" + rangeEndParts(0))
      } else if (singleObjectParts.size > 0) {
        Cite2Urn(baseStr + ":" + singleObjectParts(0))
      } else {
        Cite2Urn(baseStr + ":" )
      }
    }

    /** Trims the property identifier off of a URN,
    * if it is present.  This effectively converts a
    * property citation to an object citation.
    */
    def dropProperty: Cite2Urn = {
      val baseStr = Vector("urn","cite2",namespace,collection).mkString(":") + "." + version

      objectComponentOption match {
        case obj: Some[String] =>   Cite2Urn(baseStr + ":" + objectComponent)
        case None =>   Cite2Urn(baseStr + ":")
      }
    }


    /** Adds a version identifier to the object hierarchy.
    *
    * @param versionId Identifier for the property.
    */
    def addVersion(versionId: String) : Cite2Urn = {

      require(versionOption == None, "cannot add version to a URN that already has one " + this.toString)
      val baseStr = Vector("urn","cite2",namespace).mkString(":")

      val objectPart = objectOption match  {
        case None => ""
        case _ => objectComponent
      }


      Cite2Urn(baseStr + ":" + collection + "." + versionId + ":" + objectPart)
    }


    def dropVersion : Cite2Urn = {
      val baseStr = Vector("urn","cite2",namespace).mkString(":")
      Cite2Urn(baseStr + ":" + collection + ":" + objectComponent)
    }

    /** Adds a property identifier to the object hierarchy.
    *
    * @param propertyId Identifier for the property.
    */
    def addProperty(propertyId: String) : Cite2Urn = {
      require(propertyOption == None, "cannot add a property to a URN that already has one " + this.toString)

      require(versionOption != None, "cannot add a property to a URN that does not have a version " + this.toString)

      val baseStr = Vector("urn","cite2",namespace,collection).mkString(":")

      objectComponentOption match {
        case None => Cite2Urn(baseStr + "." + version + "." + propertyId + ":")
        case o: Some[String] => Cite2Urn(baseStr + "." + version + "." + propertyId + ":" + o.get)
      }
    }

    /** Adds an object selector to the URN.
    *
    * @param selectorId Identifier for the selected object.
    */
    def addSelector(selectorId: String) : Cite2Urn = {
      require(versionOption != None, "cannot add selector to unversioned URN " + this.toString)

      require(objectOption == None, "cannot add an object selector to URN that already has one " + this.toString)

      Cite2Urn(this.toString + selectorId)
    }

    /** Trims the selector component off of a URN,
    * if it is present.  This effectively converts a
    * citation of a single value to a citation of a
    * collection of values (objects or properties).
    */
    def dropSelector: Cite2Urn = {

      val baseStr = Vector("urn","cite2",namespace,collection).mkString(":")

      this.versionOption match {
        case s: Some[String] => {
          val baseVersion = baseStr + "." + s.get
          this.propertyOption match {
            case s: Some[String] => Cite2Urn(baseVersion + "." + s.get + ":")
            case None => Cite2Urn(baseVersion + ":")
          }
        }
        case None => Cite2Urn(baseStr + ":")
      }

    }


    def collectionContainedIn(urn: Cite2Urn): Boolean = {
      val coll = urn.collectionComponent
      val str = "(^" + coll + """\.)|(^""" + coll + "$)"
      val pttrn = str.r

      val res = pttrn.findFirstIn(collectionComponent.toString)
      //println("Result of matching  " + str + " in " + urn.toString + " == " + res)
      res match {
        case None => false
        case _ => true
      }
    }
    /// urn matching
    def collectionsMatch(u: Cite2Urn) : Boolean = {
        (collectionContainedIn(u) || u.collectionContainedIn(this))
    }

    def objectsMatch(u: Cite2Urn) : Boolean = {

      objectComponentOption match {
        case None => true
        case _ =>  {
          u.objectComponentOption match {
            case None => true
            case _ => this.dropExtensions.objectComponent == u.dropExtensions.objectComponent
          }
        }
      }
    }

    def ~~(u: Urn): Boolean = {
      u match {
        case urn: Cite2Urn => ~~(urn)
        case _ => throw CiteException("Can only match Cite2Urn against a second Cite2Urn")
      }
    }
    def ~~(u: Cite2Urn): Boolean = {
      (objectsMatch(u) && collectionsMatch(u))
    }


    /// stringifications

    override def toString() = {
      urnString
    }
    val labels = Vector(
      "URN",
      "Collection component",
      "Collection parts",
      "Collection",
      "Version option",
      "Object component option",
      "Object parts",
      "Is range",
      "Is object",
      "Single object parts",
      "Object extension option",
      "Range begin parts",
      "Range end parts",
      "Range begin extension option",
      "Range end extension option"

    )
    def debugString = {
      val valueList = Vector(
        urnString,
        collectionComponent,
        collectionParts,
        collection,
        versionOption.getOrElse("none"),
        objectComponentOption.getOrElse("none"),
        objectParts,
        isRange,
        isObject,
        singleObjectParts,
        objectExtensionOption.getOrElse("none"),
        rangeBeginParts,
        rangeEndParts,
        rangeBeginExtensionOption.getOrElse("none"),
        rangeEndExtensionOption.getOrElse("none")



      )
      labels.zip(valueList).map {case (lbl,display) => lbl + ": " + display }
    }
  }



}
