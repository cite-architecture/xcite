package edu.holycross.shot
import scala.scalajs.js
import scala.scalajs.js.annotation._

package cite {

  /** A URN for a canonically citable text or passage of text.
  *
  * @constructor create a new [[CtsUrn]]
  * @param urnString String representation of [[CtsUrn]] validating
  * againt the CtsUrn specification
  */
  @JSExportAll case class CtsUrn  (val urnString: String) extends Urn {



    /** Array of top-level, colon-delimited components.
    *
    * The Array will have 4 elements if the optional passage
    * component is omitted;  if will have 5 elements if the passage
    * component is included.
    */
    val components = urnString.split(":")
    require(components.size > 3, "Invalid URN syntax: too few components in " + urnString)
    require(components.size < 6, "Invalid URN syntax: too many components in " + urnString)

    /** Required namespace component of the URN.*/
    val namespace: String = components(2)
    /** Required work component of the URN.*/
    val workComponent: String = components(3)
    /** Array of dot-separate parts of the workComponent.*/
    val workParts = workComponent.split("""\.""")
    /** Required textgroup part of work hierarchy.*/
    val textGroup: String = workParts(0)

    /** Optional work part of work hierarchy.    */
    def workOption: Option[String] = {
      workParts.size match {
        case w if 2 until 5 contains w => Some(workParts(1))
        case _ => None
      }
    }

    /** String value of optional work part of work hierarchy.  */
    def work = {
      try {
        workOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No work defined in " + urnString)
      }
    }

    /** Create a new [[CtsUrn]] by collapsing the passage hierarchy by `i` levels.
    *
    * @param i Number of levels to drop from passage hierarchy.
    */
    def collapsePassageBy(i: Int) : CtsUrn = {
      if (passageNodeParts.size == 0) {
        this.dropPassage
      } else {
        val citationLevels = passageNodeParts(0).split("\\.")
        if (citationLevels.size > 1) {
          CtsUrn(this.dropPassage.toString + citationLevels.dropRight(1).mkString("."))
        } else {
          this.dropPassage
        }
      }
    }

    /** Create a new [[CtsUrn]] by collapsing the passage hierarchy to a specified level.
    *
    * @param i Number of levels to include in the passage hierarchy.
    */
    def collapsePassageTo(i: Int) : CtsUrn = {
      if (passageNodeParts.size == 0) {
        throw CiteException("Two few levels in " + urnString + " - cannot collapse to " +  i + " levels.")
      } else {
        val citationLevels = passageNodeParts(0).split("\\.")
        if (citationLevels.size >= i) {
          CtsUrn(this.dropPassage.toString + citationLevels.take(i).mkString("."))
        } else {
          throw CiteException("Two few levels in " + urnString + " - cannot collapse to " +  i + " levels.")
        }
      }
    }


    /** Optional version part of work hierarchy.
    */
    def versionOption: Option[String] = {
      workParts.size match {
        case v if 3 until 5 contains v => Some(workParts(2))
        case _ => None
      }
    }

    /** String value of optional version part of work hierarchy.
    */
    def version: String = {
      try {
        versionOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No version defined in " + urnString)
      }
    }

    /** Optional exemplar part of work hierarchy.
    */
    def exemplarOption: Option[String] = {
      workParts.size match {
        case 4 => Some(workParts(3))
        case _ => None
      }
    }

    /** String value of optional exemplar part of work hierarchy.
    */
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

    /** Optional passage component of the [[CtsUrn]].
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

    /** String value of optional passage component of the URN.
    */
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


    /** Optional single passage node.
    */
    val passageNodeOption: Option[String] = {
      if (passageParts.size == 1) Some(passageParts(0)) else None
    }

    /** String value of the optional single passage node.
    */
    def passageNode: String = {
      try {
        passageNodeOption.get
      }  catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Array splitting optional single passage node into reference and extended reference.
    */
    def passageNodeParts: Array[String] = {
      passageNodeOption match {
        case None => Array.empty[String]
        case _ => passageNode.split("@")
      }
    }

    /** Reference part of optional passage node.
    */
    def passageNodeRefOption: Option[String] = {
      if (passageNodeParts.isEmpty) None else Some(passageNodeParts(0))
    }

    /** String value of reference part of optional passage node.
    */
    def passageNodeRef = {
      try {
        passageNodeRefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No passage component defined in " + urnString)
      case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Extract optional subference component from optional passage node
    * using packag object's [[subrefOption]] function.*/
    def passageNodeSubrefOption: Option[String] = {
      try {
        subrefOption(passageNode)
      } catch {
        case e: java.util.NoSuchElementException => None
        case citeEx: CiteException => None
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** String value of optional subference component from optional passage node. */
    def passageNodeSubref = {
      try {
        passageNodeSubrefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node subref defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Optional indexed text of the optional passage node.*/
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

    /** String value of optional indexed text of the optional passage node.*/
    def passageNodeSubrefText = {
      try {
        passageNodeSubrefTextOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node subreference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Optional index value of the optional passage node.*/
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

    /** Integer value of 0ptional index of the optional passage node.*/
    def passageNodeSubrefIndex: Int= {
      try {
        passageNodeSubrefIndexOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No individual node subreference index defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** First part of an optional range expression in optional passage component.
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

    /** String value of first part of an optional range expression in optional passage component.
    */
    def rangeBegin = {
      try {
        rangeBeginOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Array splitting first part of optional range expression into reference and extended reference.
    */
    def rangeBeginParts = {
      rangeBeginOption match {
        case None => Array.empty[String]
        case _ => rangeBegin.split("@")
      }
    }

    /** Reference part of first part of range in optional passage node.*/
    def rangeBeginRefOption: Option[String] = {
      if (rangeBeginParts.isEmpty) None else Some(rangeBeginParts(0))
    }

    /** String value of reference part of first part of range in optional passage node.*/
    def rangeBeginRef = {
      try {
        rangeBeginRefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning reference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Extract optional subference component from first part of ragne in optional passage node using packag object's [[subrefOption]] function.*/
    def rangeBeginSubrefOption = {
      rangeBeginOption match {
        case None => None
        case _ =>  subrefOption(rangeBegin)
      }
    }

    /** String value of optional subference component from first part of range in optional passage node.*/
    def rangeBeginSubref = {
      try {
        rangeBeginSubrefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning subreference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Optional indexed text of the first part of range in optional passage node. */
    def rangeBeginSubrefTextOption: Option[String] = {
      rangeBeginOption match {
        case None => None
        case _ => subrefTextOption(rangeBegin)
      }
    }

    /** String value of optional indexed text of the the first part of range of optional passage node.
    */
    def rangeBeginSubrefText = {
      try {
        rangeBeginSubrefTextOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range beginning subreference text defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Optional index value of the first part of range of optional passage node.*/
    def rangeBeginSubrefIndexOption: Option[Int] = {
      try {
        subrefIndexOption(rangeBegin)
      } catch {
        case e: java.util.NoSuchElementException => None
        case citeEx: CiteException => None
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Integer value of optional index of the first part of range of optional passage node.*/
    def rangeBeginSubrefIndex = {
      try {
        rangeBeginSubrefIndexOption.get
      } catch {
        case e: java.util.NoSuchElementException =>throw CiteException("No range beginning subreference index defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Second part of an optional range expression in optional passage component.*/
    def rangeEndOption: Option[String] = {
      if (passageParts.size > 1) {
        if (passageParts(1).last == '.') {
          throw CiteException("Invalid URN: trailing period on range ending reference " + passageParts(1))
        } else {
          Some(passageParts(1))
        }
      } else None
    }

    /** String value of second part of an optional range expression in optional passage component.*/
    def rangeEnd = {
      try {
        rangeEndOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending defined in " + urnString + " from passage parts " + passageParts.toVector)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Array splitting second part of optional range expression into reference and extended reference.*/
    def rangeEndParts = {
      rangeEndOption match {
        case None => Array.empty[String]
        case _ => rangeEnd.split("@")
      }
    }

    /** Reference part of second part of range in optional passage node.*/
    def rangeEndRefOption: Option[String] = {
      if (rangeEndParts.isEmpty) None else Some(rangeEndParts(0))
    }

    /** String value of reference part of second part of range in optional passage node.*/
    def rangeEndRef = {
      try {
        rangeEndRefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending reference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Extract optional subference component from second part of ragne in optional passage node using packag object's [[subrefOption]] function*/
    def rangeEndSubrefOption = {
      rangeEndOption match {
        case None => None
        case _ =>  subrefOption(rangeEnd)
      }
    }

    /** String value of optional subference component from second part of range in optional passage node
    */
    def rangeEndSubref = {
      try {
        rangeEndSubrefOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending subreference defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Optional indexed text of the second part of range in optional passage node. */
    def rangeEndSubrefTextOption: Option[String] = {
      rangeEndOption match {
        case None => None
        case _ => subrefTextOption(rangeEnd)
      }
    }

    /** String value of optional indexed text of the second part of range in optional passage node. */
    def rangeEndSubrefText = {
      try {
        rangeEndSubrefTextOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending subreference text defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Optional index value of the second part of range of optional passage node.*/
    def rangeEndSubrefIndexOption = {
      try {
        subrefIndexOption(rangeEnd)
      } catch {
        case e: java.util.NoSuchElementException => None
        case citeEx: CiteException => None
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Integer value of optional index of the second part of range of optional passage component.*/
    def rangeEndSubrefIndex = {
      try {
        rangeEndSubrefIndexOption.get
      } catch {
        case e: java.util.NoSuchElementException => throw CiteException("No range ending subreference index defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** True if the passage component refers to a range.*/
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


    /** Create a new [[CtsUrn]] by dropping the passage component from
    * this URN.
    */
    def dropPassage: CtsUrn = {
      CtsUrn("urn:cts:" + namespace + ":" + workComponent + ":")
    }


    /** Create a new [[CtsUrn]] by dropping the version part  from
    * the work component.
    */
    def dropVersion: CtsUrn = {
      val psg = passageComponentOption match {
        case None => ""
        case _ => passageComponentOption.get
      }
      workLevel match {
        case  WorkLevel.TextGroup => this
        case WorkLevel.Work => this
        case WorkLevel.Version =>   CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + ":" + psg)
        case WorkLevel.Exemplar =>   CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + ":" + psg)
      }
    }


    /** Create a new [[CtsUrn]] by adding or replacing the version part
    * of the passage component with a given value.
    *
    * @param v Version identifier for new URN.
    */
    def addVersion(v: String): CtsUrn = {
      val psg = passageComponentOption match {
        case None => ""
        case _ => passageComponentOption.get
      }

      workLevel match {

        case  WorkLevel.TextGroup => throw (CiteException("Cannot add version to group-level URN"))
        case WorkLevel.Work =>  CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + v + ":" + psg)
        case WorkLevel.Version =>    CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + v + ":" + psg)
        case WorkLevel.Exemplar =>   CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + v + ":" + psg)
      }
    }

    /** Create a new [[CtsUrn]] by dropping any extended reference
    * parts from this CtsUrn.
    */
    def dropSubref: CtsUrn = {
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

    /** True if work reference in `urn` is contained
    * in or equal to the work reference of this CtsUrn.
    *
    * @param urn CtsUrn to compare to this one.
    */
    def workContains(urn: CtsUrn): Boolean = {
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
    def passageContains(urn: CtsUrn): Boolean = {
      if ((passageParts.isEmpty) || (urn.passageParts.isEmpty)) {
        true
      } else {
        val psg = urn.dropSubref.passageComponent.replaceAll("\\.","\\\\.")
        val str = "(^" + psg + "\\.)|(^" + psg + "$)"

        val pttrn = str.r

        val res = pttrn.findFirstIn(dropSubref.passageComponent.toString)

        res match {
          case None => false
          case _ => true
        }
      }
    }

    /** True if the passage reference of either  this
    * or a given [[CtsUrn]] is contained by the other.
    *
    * @param urn CtsUrn to compare to this one
    */
    def passageMatch(urn: CtsUrn): Boolean = {
      passageContains(urn) || urn.passageContains(this)
    }
    /** true if the passage reference of either `urn`
    * of this CtsUrn is contained by the other.
    *
    * @param urn CtsUrn to compare to this one
    */
    def workMatch(urn: CtsUrn): Boolean = {
      workContains(urn) || urn.workContains(this)
    }

    /** True if this [[CtsUrn]] contains a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def >(urn: CtsUrn): Boolean = {
      ((this >= urn) && (urn != this))
    }


    /** True if this [[CtsUrn]] contains or is equal to a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def >=(urn: CtsUrn): Boolean = {
      ((workContains(urn) || (urn.workComponent == this.workComponent )) && (passageContains(urn)) || (urn.passageComponent == this.passageComponent))
    }


    /** True if this [[CtsUrn]] is contained by or equal to a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def <=(urn: CtsUrn): Boolean = {
      ((urn.workContains(this) || (urn.workComponent == this.workComponent )) && (urn.passageContains(this)) || (urn.passageComponent == this.passageComponent))
    }

    /** True if this [[CtsUrn]] is contained by a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def <(urn: CtsUrn): Boolean = {
        ((this <= urn) && (urn != this))
    }

    /** True if this [[CtsUrn]] is URN-similar a given [[CtsUrn]].
    *
    * @param u URN to compare.
    */
    def ~~(urn: CtsUrn): Boolean = {
      namespace == urn.namespace && workMatch(urn) && passageMatch(urn)
    }


    /** True if this [[CtsUrn]] is URN-similar to a given [[Urn]].
    * Comparison URN must be a CTS URN.
    *
    * @param u URN to compare.
    */
    def ~~ (u: Urn) : Boolean = {
      u match {
        case urn: CtsUrn => ~~(urn)
        case _ => throw CiteException("Can only match CtsUrn against a second CtsUrn")
      }
    }





    /** True if this [[CtsUrn]] is NOT URN-similar to a given [[CtsUrn]].
    *
    * @param urn URN to compare.
    */
    def ><(urn: CtsUrn): Boolean = {
      namespace != urn.namespace || (! workMatch(urn)) || (! passageMatch(urn))
    }


    /** True if this [[CtsUrn]] is NOT URN-similar to a given [[Urn]].
    * The comparison [[Urn]] must be a [[CtsUrn]]
    *
    * @param u URN to compare.
    */
    def >< (u: Urn) : Boolean = {
      u match {
        case urn: CtsUrn => ><(urn)
        case _ => throw CiteException("Can only match CtsUrn against a second CtsUrn")
      }
    }

    /** Override default toString function. */
    override def toString() = {
      urnString
    }

    /** True if value submitted to construct this [[CtsUrn]] complies
    * fully with the CtsUrn specification.
    */
    def fullyValid: Boolean = {



      require(components(0) == "urn", "invalid URN syntax: " + urnString + ". First component must be 'urn'.")
      require(components(1) == "cts", "invalid URN syntax: " + urnString + ". Second component must be 'cts'.")
      require(componentSyntaxOk, "invalid URN syntax: " + urnString + ". Wrong number of components.")
      require((workParts.size < 5), "invalid URN syntax. Too many parts in work component " + workComponent )

      require(passageSyntaxOk, "Invalid URN syntax.  Error in passage component " + passageComponent)


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
      if (components.size == 5) {
        urnString.last match {
          case ':' => throw CiteException("Invalid URN syntax: trailing colon in " + urnString)
          case _ =>  true
        }
      } else {
        true
      }
    }

    // Require fully valid syntax:
    require(fullyValid)
  }


  /** Enumeration of levels of the CTS work hierarchy. */
  object WorkLevel extends Enumeration {
    val TextGroup, Work, Version, Exemplar = Value
  }



}
