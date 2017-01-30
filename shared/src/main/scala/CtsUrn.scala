package edu.holycross.shot
import scala.scalajs.js
import js.annotation.JSExport

package cite {

  /** A URN for a canonically text or passage of text.
  *
  * @constructor create a new CtsUrn
  * @param urnString String representation of CtsUrn validating
  * againt the CtsUrn specification
  */
  @JSExport case class CtsUrn  (val urnString: String) extends Urn {
    /** Array of top-level, colon-delimited components.
    *
    * The Array will have 4 elements if the optional passage
    * component is omitted;  if will have 5 elements if the passage
    * component is included.
    */
    val components = urnString.split(":")
    require(components.size > 3, "Invalid URN syntax: too few components in " + urnString)
    require(components.size < 6, "Invalid URN syntax: too many components in " + urnString)
    if (components.size == 5) {
      urnString.last match {
        case ':' => throw CiteException("Invalid URN syntax: trailing colon in " + urnString)
        case _ => //
      }
    } else {}


    /** Required namespace component of the URN.*/
    val namespace: String = components(2)
    /** Required work component of the URN.*/
    val workComponent: String = components(3)
    /** Array of dot-separate parts of the workComponent.*/
    val workParts = workComponent.split("""\.""")
    /** Textgroup part of work hierarchy.
    */
    val textGroup: String = workParts(0)



    // Verify syntax of submitted String:
    require(components(0) == "urn", "invalid URN syntax: " + urnString + ". First component must be 'urn'.")
    require(components(1) == "cts", "invalid URN syntax: " + urnString + ". Second component must be 'cts'.")
    require(componentSyntaxOk, "invalid URN syntax: " + urnString + ". Wrong number of components.")
    require((workParts.size < 5), "invalid URN syntax. Too many parts in work component " + workComponent )


    /** Work part of work hierarchy.
    *
    * Value is an empty string if there is no work part.
    */
    def workOption: Option[String] = {
      workParts.size match {
        case w if 2 until 5 contains w => Some(workParts(1))
        case _ => None
      }
    }

    def work = {
      try {
        workOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No work defined in " + urnString)
      }
    }


    /** Version part of work hierarchy.
    *
    * Value is an empty string if there is no version part.
    */
    def versionOption: Option[String] = {
      workParts.size match {
        case v if 3 until 5 contains v => Some(workParts(2))
        case _ => None
      }
    }
    def version: String = {
      try {
        versionOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No version defined in " + urnString)
      }
    }

    /** Exemplar part of work hierarchy.
    *
    * Value is an empty string if there is no exemplar part.
    */
    def exemplarOption: Option[String] = {
      workParts.size match {
        case 4 => Some(workParts(3))
        case _ => None
      }
    }

    def exemplar: String = {
      try {
        exemplarOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No exemplar defined in " + urnString)
      }
    }

    /** Enumerated WorkLevel for this workComponent.*/
    def workLevel = {
      workParts.size match {
        case 1 => WorkLevel.TextGroup
        case 2 => WorkLevel.Work
        case 3 => WorkLevel.Version
        case 4 => WorkLevel.Exemplar
      }
    }

    /** Optional passage component of the URN.
    *
    * Value is an empty string if there is no passage component.
    */
    def passageComponentOption: Option[String] = {
      components.size match {
        case 5 => {
          if (components(4).last == '.') {
            throw CiteException("Invalid URN syntax in passage component " + components(4) + ": trailing period.") }  else {
            Some(components(4))
          }
        }
        case _ => None
      }
    }
    def passageComponent = {
      try {
        passageComponentOption.get
      } catch {
          case e: java.util.NoSuchElementException => throw CiteException("No passage component defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Array of hyphen-separated parts of the passageComponent.
    *
    * The Array will contain 0 elements if passageComponent is empty,
    * 1 element if the passageComponent is a node reference, and
    * 2 elements if the passageComponent is a range reference.
    */
    def passageParts: Array[String] ={
      passageComponentOption match {
        case None => Array.empty[String]
        case s: Some[String] => s.get.split("-")
      }
    }


    /** Single node of the passage component of the URN.
    *
    * Value is an empty string if there is no passage component
    * or if the passage component is a range reference.
    */
    val passageNodeOption: Option[String] = {
      if (passageParts.size == 1) Some(passageParts(0)) else None
    }
    def passageNode: String = {
      try {
        passageNodeOption.get
      }  catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    def passageNodeParts: Array[String] = {
      passageNodeOption match {
        case None => Array.empty[String]
        case _ => passageNode.split("@")
      }
    }
    def passageNodeRefOption: Option[String] = {
      if (passageNodeParts.isEmpty) None else Some(passageNodeParts(0))
    }
    def passageNodeRef = {
      try {
        passageNodeRefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No passage component defined in " + urnString)
      case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Full string value of the passage node's subref.*/
    def passageNodeSubrefOption: Option[String] = {
      try {
        subrefOption(passageNode)
      } catch {
        case e: java.util.NoSuchElementException => None
        case citeEx: CiteException => None
        case otherEx : Throwable => throw( otherEx)
      }
    }

    def passageNodeSubref = {
      try {
        passageNodeSubrefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node subref defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }
    /** Indexed text of the passage node's subref.*/
    def passageNodeSubrefTextOption: Option[String] = {
      passageNodeSubrefOption match {
        case None => None
        case _ => {
          try {
            subrefTextOption(passageNode) match {
              case None => None
              case s: Some[String] => s
            }

          } catch {
            case e: java.util.NoSuchElementException => None
            case otherEx : Throwable => throw( otherEx)
          }
        }
      }
    }
    def passageNodeSubrefText = {
      try {
        passageNodeSubrefTextOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node subreference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Index value of the passage node's subref.*/
    def passageNodeSubrefIndexOption: Option[Int] = {
      try {
       subrefIndexOption(passageNode) match {
        case None => None
        case i: Option[Int] => i
      }

      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node subreference index defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }
    def passageNodeSubrefIndex = {
      try {
        passageNodeSubrefIndexOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node subreference index defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** First range part of the passage component of the URN.
    *
    * Value is an empty string if there is no passage component
    * or if the passage component is a node reference.
    */
    def rangeBeginOption: Option[String] = {
      if (passageParts.size > 1) {
        if (passageParts(0).last == '.') {
          throw CiteException("Invalid URN: trailing period on range beginning reference " + passageParts(0))
        }  else {
          Some(passageParts(0))
        }
      } else None
    }
    def rangeBegin = {
      try {
        rangeBeginOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }
    def rangeBeginParts = {
      rangeBeginOption match {
        case None => Array.empty[String]
        case _ => rangeBegin.split("@")
      }
    }
    def rangeBeginRefOption: Option[String] = {
      if (rangeBeginParts.isEmpty) None else Some(rangeBeginParts(0))
    }
    def rangeBeginRef = {
      try {
        rangeBeginRefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning reference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Full string value of the range beginning's subref.*/
    def rangeBeginSubrefOption = {
      rangeBeginOption match {
        case None => None
        case _ =>  subrefOption(rangeBegin)
      }
    }
    def rangeBeginSubref = {
      try {
        rangeBeginSubrefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning subreference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }
    /** Indexed text of the range beginning's subref.*/
    def rangeBeginSubrefTextOption: Option[String] = {
      rangeBeginOption match {
        case None => None
        case _ => subrefTextOption(rangeBegin)
      }
    }
    def rangeBeginSubrefText = {
      try {
        rangeBeginSubrefTextOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning subreference text defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Index value of the range beginning's subref.*/
    def rangeBeginSubrefIndexOption: Option[Int] = {
      try {
        subrefIndexOption(rangeBegin)
      } catch {
        case e: java.util.NoSuchElementException => None
        case citeEx: CiteException => None
        case otherEx : Throwable => throw( otherEx)
      }
    }

    def rangeBeginSubrefIndex = {
      try {
        rangeBeginSubrefIndexOption.get
      } catch {
        case e: java.util.NoSuchElementException =>throw CiteException("No range beginning subreference index defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Second range part of the passage component of the URN.
    *
    * Value is an empty string if there is no passage component
    * or if the passage component is a node reference.
    */
    def rangeEndOption: Option[String] = {
      if (passageParts.size > 1) {
        if (passageParts(1).last == '.') {
          throw CiteException("Invalid URN: trailing period on range ending reference " + passageParts(1))
        } else {
          Some(passageParts(1))
        }
      } else None
    }
    def rangeEnd = {
      try {
        rangeEndOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending defined in " + urnString + " from passage parts " + passageParts.toVector)
        case otherEx : Throwable => throw( otherEx)
      }
    }
    def rangeEndParts = {
      rangeEndOption match {
        case None => Array.empty[String]
        case _ => rangeEnd.split("@")
      }
    }
    def rangeEndRefOption: Option[String] = {
      if (rangeEndParts.isEmpty) None else Some(rangeEndParts(0))
    }
    def rangeEndRef = {
      try {
        rangeEndRefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending reference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Full string value of the range Endning's subref.*/
    def rangeEndSubrefOption = {
      rangeEndOption match {
        case None => None
        case _ =>  subrefOption(rangeEnd)
      }
    }
    def rangeEndSubref = {
      try {
        rangeEndSubrefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending subreference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }
    /** Indexed text of the range Endning's subref.*/
    def rangeEndSubrefTextOption: Option[String] = {
      rangeEndOption match {
        case None => None
        case _ => subrefTextOption(rangeEnd)
      }
    }
    def rangeEndSubrefText = {
      try {
        rangeEndSubrefTextOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending subreference text defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Index value of the range Endning's subref.*/
    def rangeEndSubrefIndexOption = {
      try {
        subrefIndexOption(rangeEnd)
      } catch {
        case e: java.util.NoSuchElementException => None
        case citeEx: CiteException => None
        case otherEx : Throwable => throw( otherEx)
      }
    }

    def rangeEndSubrefIndex = {
      try {
        rangeEndSubrefIndexOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending subreference index defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    require(passageSyntaxOk, "Invalid URN syntax.  Error in passage component " + passageComponent)

    /** True if the URN refers to a range.*/
    def isRange = {
      passageComponentOption match {
        case None => false
        case s: Some[String] =>   s.get contains "-"
      }
    }
    /** True if the URN refers to a point (leaf node or containing node).*/
    def isPoint = {
      passageComponentOption match {
        case None => false
        case s: Some[String] => ((!isRange) && s.nonEmpty)
      }
    }
    /** True if URN's syntax for required components is valid.*/
    def componentSyntaxOk = {
      components.size match {
        case 5 => true
        case 4 => if (urnString.takeRight(1) == ":") true else false
        case _ => false
      }
    }


    for (p <- workParts) {
      require(p.nonEmpty, "invalid work syntax in " + urnString)
    }
    for (p <- passageParts) {
      require(p.nonEmpty,"invalid passage syntax in " + urnString)
    }
    for (p <- passageNodeParts) {
      require(p.nonEmpty, "invalid passage syntax in passage node " + urnString)
    }
    for (p <- rangeBeginParts) {
      require(p.nonEmpty,"invalid passage syntax in range beginning" + urnString)
    }
    for (p <- rangeEndParts) {
      require(p.nonEmpty,"invalid passage syntax in range ending" + urnString)
    }
    passageComponentOption match {
      case None => assert(true)
      case _ => {
        if (isRange) {
          val r1DotParts = rangeBegin.split("""\.""")
          for (p <- r1DotParts) {
            require(p.nonEmpty,"invalid passage syntax in range beginning of " + urnString)
          }

          val r2DotParts = rangeEnd.split("""\.""")
          for (p <- r2DotParts) {
            require(p.nonEmpty,"invalid passage syntax in range ending of " + urnString)
          }

        }  else {
          val nodeDotParts = passageNode.split("""\.""")
          for (p <- nodeDotParts) {
            require(p.nonEmpty,"invalid passage syntax in " + urnString)
          }
        }
      }
    }

    /** True if URN's syntax for optional passage component is valid.*/
    def passageSyntaxOk = {
      passageParts.size match {
        case 0 => {
          passageComponentOption match {
            case None => true
            case _ => false
          }
        }
        case 1 => if (passageComponent.contains("-")) false else true
        case 2 => ((rangeBegin.nonEmpty) && (rangeEnd.nonEmpty))
        case _ => throw CiteException("invalid URN string: more than two elements in range " + passageComponent)
      }
    }



    def dropPassage = {
      CtsUrn("urn:cts:" + namespace + ":" + workComponent + ":")
    }

    def dropSubref = {
      val baseString = dropPassage
      if (isRange) {
        if (rangeBeginRef == rangeEndRef) {
          CtsUrn(baseString + rangeBeginRef)
        } else {
          CtsUrn(baseString + rangeBeginRef + "-" + rangeEndRef)
        }

      } else {
        CtsUrn(baseString + passageNodeRef)
      }
    }

    /** true if work reference in `urn` is contained
    * in or equal to the work reference of this CtsUrn.
    *
    * @param urn CtsUrn to compare to this one.
    */
    def workContainedIn(urn: CtsUrn): Boolean = {
      val wrk = urn.workComponent
      val str = "(^" + wrk + """\.)|(^""" + wrk + "$)"
      val pttrn = str.r

      val res = pttrn.findFirstIn(workComponent.toString)
      //println("Result of matching  " + str + " in " + urn.toString + " == " + res)
      res match {
        case None => false
        case _ => true
      }
    }
    /** true if passage reference in `urn` is contained
    * in or equal to the passage reference of this CtsUrn.
    *
    * @param urn CtsUrn to compare to this one.
    */
    def passageContainedIn(urn: CtsUrn): Boolean = {
      if ((passageParts.isEmpty) || (urn.passageParts.isEmpty)) {
        true
      } else {
        val psg = urn.dropSubref.passageComponent
        val str = "(^" + psg + """\.)|(^""" + psg + "$)"
        val pttrn = str.r

        val res = pttrn.findFirstIn(dropSubref.passageComponent.toString)
        //("Result of matching  " + str + " in " + urn.toString + " == " + res)
        res match {
          case None => false
          case _ => true
        }
      }
    }

    /** true if the passage reference of either `urn`
    * of this CtsUrn is contained by the other.
    *
    * @param urn CtsUrn to compare to this one
    */
    def passageMatch(urn: CtsUrn): Boolean = {
      passageContainedIn(urn) || urn.passageContainedIn(this)
    }
    /** true if the passage reference of either `urn`
    * of this CtsUrn is contained by the other.
    *
    * @param urn CtsUrn to compare to this one
    */
    def workMatch(urn: CtsUrn): Boolean = {
      workContainedIn(urn) || urn.workContainedIn(this)
    }


    def urnMatch(urn: CtsUrn): Boolean = {
      namespace == urn.namespace && workMatch(urn) && passageMatch(urn)
    }
    def urnMatch (u: Urn) : Boolean = {
      u match {
        case urn: CtsUrn => urnMatch(urn)
        case _ => throw CiteException("Can only match CtsUrn against a second CtsUrn")
      }
    }


    def ~~(u: CtsUrn) : Boolean = {
      urnMatch(u)
    }

    override def toString() = {
      urnString
    }
  }


  /** Enumeration of levels of the CTS work hierarchy. */
  object WorkLevel extends Enumeration {
    val TextGroup, Work, Version, Exemplar = Value
  }



}
