package edu.holycross.shot
import scala.scalajs.js
import scala.scalajs.js.annotation._


import wvlet.log._
//import wvlet.log.LogFormatter.SourceCodeLogFormatter



package cite {

  /** A URN for a canonically citable text or passage of text.
  *
  * @constructor create a new [[CtsUrn]]
  * @param urnString String representation of [[CtsUrn]] validating
  * againt the CtsUrn specification
  */
  @JSExportAll
  case class CtsUrn  (val urnString: String) extends Urn with LogSupport {

    // Find top-level components of CtsUrn syntax
    val componentsRE = "urn:cts:([^:]+):([^:]+):(.*)".r
    val componentsTriple = try {
      val componentsRE(namespace, workComponent, passageComponent) = urnString
      (namespace, workComponent, passageComponent)
    } catch {
      case t : Throwable => {
        val msg = "Unable to parse URN string " + urnString + ".  " + t
        warn(msg)
        throw new Exception(msg)
      }
    }
    /** Required namespace component of the URN.*/
    val namespace = componentsTriple._1
    /** Required work component of the URN.*/
    val workComponent = componentsTriple._2
    /** Optional passage component of the URN.*/
    val passageComponent = componentsTriple._3

    /** Array of dot-separate parts of the workComponent.*/
    val workParts = workComponent.split("""\.""").toVector
    /** Required textgroup part of work hierarchy.*/
    val textGroup: String = workParts(0)

    /** Optional work part of work hierarchy.    */
    def workOption: Option[String] = {
      workParts.size match {
        case w if 2 until 5 contains w => Some(workParts(1))
        case _ => None
      }
    }

    /** String value of optional work part of work hierarchy.
      * DEPRECATED in favor of workOption
      */
    def work = {
      try {
        workOption match {
          case Some(s) => s
          case None => ""
        }
      } catch {
        case e: java.util.NoSuchElementException => {
          val msg = "No work defined in " + urnString
          warn(msg)
          throw CiteException(msg)
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
      * DEPRECATED in favor of versionOption
      */
    def version: String = {
      try {
        versionOption match {
          case Some(s) => s
          case None => ""
        }
      } catch {
        case e: java.util.NoSuchElementException => {
          val msg = "No version defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
      * DEPRECATED in favor of exemplarOption
      */
    def exemplar: String = {
      try {
        exemplarOption match {
          case Some(s) => s
          case None => ""
        }
      } catch {
        case e: java.util.NoSuchElementException => {
          val msg = "No exemplar defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
      }
    }

    /** Create a new CtsUrn identifying this URN's text group.
    */
    def toTextGroup: CtsUrn = {
      CtsUrn(s"urn:cts:${namespace}:${textGroup}:")
    }

    /** Create a new CtsUrn identifying this URN's work.
    */
    def toWork: CtsUrn = {
      workOption match {
        case None => toTextGroup
        case _ => CtsUrn(s"urn:cts:${namespace}:${textGroup}.${work}:")
      }
    }

    /** Create a new CtsUrn identifying this URN's version.
    */
    def toVersion: CtsUrn = {
      versionOption match {
        case None => toWork
        case _ => CtsUrn(s"urn:cts:${namespace}:${textGroup}.${work}.${versionOption.get}:")
      }
    }

    /** Create a new CtsUrn identifying this URN's exemplar.
    */
    def toExemplar: CtsUrn = {
      exemplarOption match {
        case None => toVersion
        case _ => CtsUrn(s"urn:cts:${namespace}:${textGroup}.${work}.${versionOption.get}.${exemplar}:")
      }
    }


    /** Create a new [[CtsUrn]] by collapsing the passage hierarchy by `i` levels.
    *
    * @param i Number of levels to drop from passage hierarchy.
    */
    def collapsePassageBy(i: Int) : CtsUrn = {
      if ( this.isRange ) {
        val uv: Vector[CtsUrn] = this.rangeToUrnVector
        val first = uv.head.collapsePassageBy(i)
        val last = uv.last.collapsePassageBy(i)
        val psg = {
          if (first == last) {
            s"${first.passageComponent}"
          } else {
            s"${first.passageComponent}-${last.passageComponent}"
          }
        }
        this.addPassage(psg)
      } else {
        if (passageNodeParts.size == 0) {
          this.dropPassage
        } else {
          val citationLevels = passageNodeParts(0).split("\\.")
          if (citationLevels.size > i) {
            CtsUrn(this.dropPassage.toString + citationLevels.dropRight(i).mkString("."))
          } else {
            this.dropPassage
          }
        }
      }
    }

    /** Create a new [[CtsUrn]] by collapsing the passage hierarchy to a specified level.
    *
    * @param i Number of levels to include in the passage hierarchy.
    */
    def collapsePassageTo(i: Int) : CtsUrn = {
      /*
      if (passageNodeParts.size == 0) {
        val msg = "Two few levels in " + urnString + " - cannot collapse to " +  i + " levels."
        warn(msg)
        throw CiteException(msg)

      } else {
        */
      if ( this.isRange) {
        val uv: Vector[CtsUrn] = this.rangeToUrnVector
        val first = uv.head.collapsePassageTo(i)
        val last = uv.last.collapsePassageTo(i)
        val psg = {
          if (first == last) {
            s"${first.passageComponent}"
          } else {
            s"${first.passageComponent}-${last.passageComponent}"
          }
        }
        this.addPassage(psg)

      } else {
        if (passageNodeParts.size == 0) {
          val msg = "Two few levels in " + urnString + " - cannot collapse to " +  i + " levels."
          warn(msg)
          throw CiteException(msg)

        } else {
          val citationLevels = passageNodeParts(0).split("\\.")
          if (citationLevels.size >= i) {
            CtsUrn(this.dropPassage.toString + citationLevels.take(i).mkString("."))

          } else {
            val msg = "Two few levels in " + urnString + " - cannot collapse to " +  i + " levels."
            warn(msg)
            throw CiteException(msg)
          }
        }
      }
    }

    /** Return a Vector of integers representing the citation-depth of the passage component.
    * May be empty if there is no passage component. Will contain one integer if the URN
    * is not a range; two if it is.
    */
    def citationDepth:Vector[Int] = {
      this.isRange match {
        case true => {
          Vector(
              this.rangeBeginRef.split('.').size,
              this.rangeEndRef.split('.').size
          )
        }
        case false => {
          if (passageComponent.nonEmpty) {
              Vector(this.dropSubref.passageComponent.split('.').size)
          } else {
              Vector.empty

          }
        }
      }
    }


    /** Return a Vector of CtsUrns representing the ends of a range-URN.
    * If the URN is not a range, will return a one-element vector.
    */
    def rangeToUrnVector:Vector[CtsUrn] = {
      this.isRange match {
        case true => {
          val uBase:CtsUrn = this.dropPassage
          val passage1:String = this.rangeBegin
          val passage2:String = this.rangeEnd
          val u1:CtsUrn = CtsUrn(s"${uBase}${passage1}")
          val u2:CtsUrn = CtsUrn(s"${uBase}${passage2}")
          Vector(u1,u2)
        }
        case false => {
          Vector(this)
        }
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
    }*/

    /** String value of optional passage component of the URN.

    def passageComponent = {
      try {
        passageComponentOption.get
      } catch {
          case e: java.util.NoSuchElementException => throw CiteException("No passage component defined in " + urnString)
        case otherEx : Throwable => throw( otherEx)
      }
    }*/

    /** Array of hyphen-separated parts of the passageComponent.
    *
    * The Array will contain 0 elements if passageComponent is empty,
    * 1 element if the passageComponent is a node reference, and
    * 2 elements if the passageComponent is a range reference.
    */
    def passageParts: Vector[String] ={
      passageComponent.split("-").toVector.filter(_.nonEmpty)
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
        case e: java.util.NoSuchElementException => {
          val msg = "No individual node defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }

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
        case e: java.util.NoSuchElementException => {
          val msg = "No passage component defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No individual node subref defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No individual node subreference defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No individual node subreference index defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** Integer value of 0ptional index of the optional passage node.*/
    def passageNodeSubrefIndex: Int= {
      try {
        passageNodeSubrefIndexOption.get
      } catch {
        case e: java.util.NoSuchElementException => {
          val msg = "No individual node subreference index defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
        case otherEx : Throwable => throw( otherEx)
      }
    }

    /** First part of an optional range expression in optional passage component.
    */
    def rangeBeginOption: Option[String] = {
      if (passageParts.size > 1) {
        if (passageParts(0).last == '.') {
          val msg = "Invalid URN: trailing period on range beginning of " + urnString
          warn(msg)
          throw CiteException(msg)
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
        case e: java.util.NoSuchElementException => {
          val msg = "rangeBegin function: invalid range syntax in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "rangeBeginRef function: invalid range syntax in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No subreference on range beginning in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No range beginning subreference text defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No range beginning subreference index defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** Second part of an optional range expression in optional passage component.*/
    def rangeEndOption: Option[String] = {
      if (passageParts.size > 1) {
        if (passageParts(1).last == '.') {
          val msg = "Invalid URN: trailing period on range ending reference of " + urnString
          warn(msg)
          throw CiteException(msg)

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
        case e: java.util.NoSuchElementException => {
          val msg = "No range ending defined in " + urnString + " from passage parts " + passageParts.toVector
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No range ending reference defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No subreference on range ending in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No range ending subreference text defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
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
        case e: java.util.NoSuchElementException => {
          val msg = "No range ending subreference index defined in " + urnString
          warn(msg)
          throw CiteException(msg)
        }
        case otherEx : Throwable => throw( otherEx)
      }
    }


    /** True if the passage component refers to a range.*/
    def isRange = {
       passageComponent contains "-"
    }
    /** True if the URN refers to a point (leaf node or containing node).*/
    def isPoint = {
      ((!isRange) && passageComponent.nonEmpty)
    }

    /** True if URN's syntax for required components is valid.*/
    def componentSyntaxOk = {
      true
      /*
      components.size match {
        case 5 => true
        case 4 => if (urnString.takeRight(1) == ":") true else false
        case _ => false
      }*/
    }

    /** True if URN's syntax for optional passage component is valid.*/
    def passageSyntaxOk = {
      val repeatedPeriods = ".*\\.{2}.*"
      val illegalRepeatedPeriods = passageComponent.matches(repeatedPeriods)
      require(illegalRepeatedPeriods == false, "Invalid URN syntax: repeated periods in passage component of " + urnString)

      val leadingDot = "^\\..*"
      val illegalLeadingDot = passageComponent.matches(leadingDot)
      require(illegalLeadingDot == false, "Invalid URN syntax: leading period in passage component of " + urnString)


      val trailingDot = ".+\\.$"
      if (isRange) {
        val rangeBeginTrailingDot = (rangeBegin.matches(trailingDot))
        require(rangeBeginTrailingDot == false, "Invalid URN syntax: trailing period in range beginning of " + urnString)

        val rangeBeginLeadingDot = (rangeBegin.matches(leadingDot))
        require(rangeBeginLeadingDot == false, "Invalid URN syntax: leading period in rage beginning of " + urnString)

        val rangeEndTrailingDot = (rangeEnd.matches(trailingDot))
        require(rangeEndTrailingDot == false, "Invalid URN syntax:  trailing period in range ending of " + urnString)

        val rangeEndLeadingDot = (rangeEnd.matches(leadingDot))
        require(rangeEndLeadingDot == false, "Invalid URN syntax: leading period in range ending of " + urnString)

      } else {
        passageNodeRefOption match {
          case None => {}
          case _ => {
            val illegalTrailingDot = (passageNodeRef.matches(trailingDot))
            require(illegalTrailingDot == false, "Invalid URN syntax in passage component of " + urnString)
          }
        }

      }

      //val noSubref = dropSubref.passageComponent
  /*  if (isRange) {
      if (rangeBeginRef == rangeEndRef) {
        CtsUrn(baseString + rangeBeginRef)
      } else {
        CtsUrn(baseString + rangeBeginRef + "-" + rangeEndRef)
      }

    } else {*/
      //CtsUrn(baseString + passageNodeRef)
    //}
      //if (isRange) {
        // test each part
      //} else {
        //
        //debug("CHECK TRAIL FOR " + noSubref)
        // (noSubref.last == '.')
        //require(illegalTrailingDot == false, "Invalid URN syntax in passage component of " + urnString)
      //}

      passageParts.size match {
        case 0 => true
        case 1 => if (passageComponent.contains("-")) false else true
        case 2 => ((rangeBegin.nonEmpty) && (rangeEnd.nonEmpty))
        case _ => {
          val msg = "invalid URN string: more than two elements in range " + passageComponent
          warn(msg)
          throw CiteException(msg)
        }
      }
    }


    /** Create a new [[CtsUrn]] by dropping the passage component from
    * this URN.
    */
    def dropPassage: CtsUrn = {
      CtsUrn("urn:cts:" + namespace + ":" + workComponent + ":")
    }

    /** Create a new [[CtsUrn]] by adding or replacing the
    * passage component with a given value.
    *
    * @param psg String value of new passage reference.
    */
    def addPassage(psg: String): CtsUrn = {
      CtsUrn("urn:cts:" + namespace + ":" + workComponent + ":" + psg)
    }

    /** Create a new [[CtsUrn]] by dropping the version part  from
    * the work component.
    */
    def dropVersion: CtsUrn = {
      workLevel match {
        case  WorkLevel.TextGroup => this
        case WorkLevel.Work => this
        case WorkLevel.Version =>   CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + ":" + passageComponent)
        case WorkLevel.Exemplar =>   CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + ":" + passageComponent)
      }
    }


    /** Create a new [[CtsUrn]] by adding or replacing the version part
    * of the work hierarchy with a given value.
    *
    * @param v Version identifier for new URN.
    */
    def addVersion(v: String): CtsUrn = {
      workLevel match {

        case  WorkLevel.TextGroup => {
          val msg = "Cannot add version to group-level URN"
          warn(msg)
          throw (CiteException(msg))
        }
        case WorkLevel.Work =>  CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + v + ":" + passageComponent)
        case WorkLevel.Version =>    CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + v + ":" + passageComponent)
        case WorkLevel.Exemplar =>   CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + v + ":" + passageComponent)
      }
    }


    /** Create a new [[CtsUrn]] by dropping the exemplar
    * part of the work component, if any.
    */
    def dropExemplar: CtsUrn = {
      workLevel match {
        case  WorkLevel.TextGroup => this
        case WorkLevel.Work => this
        case WorkLevel.Version => this
        case WorkLevel.Exemplar =>   CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + versionOption.get + ":" + passageComponent)
      }
    }

  /** Create a new [[CtsUrn]] by adding or replacing the exemplar part
  * of the passage component with a given value.
  *
  * @param v Exemplar identifier for new URN.
  */
  def addExemplar(ex: String): CtsUrn = {
    workLevel match {

      case  WorkLevel.TextGroup => {
        val msg = "Cannot add version to group-level URN"
        warn(msg)
        throw (CiteException(msg))
      }
      case  WorkLevel.Work => {
        val msg = "Cannot add version to work-level URN"
        warn(msg)
        throw (CiteException(msg))
      }
      case WorkLevel.Version =>  CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + versionOption.get + "." + ex + ":" + passageComponent)
      case WorkLevel.Exemplar =>    CtsUrn("urn:cts:" + namespace + ":" + textGroup + "." +  work + "." + versionOption.get + "." + ex + ":" + passageComponent)
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
    * by the work reference of this CtsUrn.
    *
    * @param urn CtsUrn to compare to this one.
    */
    def workContains(urn: CtsUrn): Boolean = {
      //debug("Compare workcomponent " + workComponent + " with " + urn.workComponent)
      if (workComponent == urn.workComponent) {
        false

      } else {

        workLevel match {
          case WorkLevel.TextGroup => (textGroup == urn.textGroup)
          case WorkLevel.Work => {
            try {
              (textGroup == urn.textGroup) && (workOption != None) && (workOption == urn.workOption)
            } catch {
              case e: java.lang.Exception => false
            }

          }
          case WorkLevel.Version => {
            try {
              (textGroup == urn.textGroup) && (work == urn.work) && (urn.versionOption != None) && (versionOption == urn.versionOption)
            } catch {
              case e: java.lang.Exception => false
            }

          }
          case WorkLevel.Exemplar => {
            false
          }
        }

        /*
        val str = "(^" + wrk + """\.)|(^""" + wrk + "$)"
        val pttrn = str.r

        val res = pttrn.findFirstIn(wrk)
        debug("Result of matching  " + str + " in " + urn.toString + " == " + res)
        res match {
          case None => false
          case _ => {
            debug("THIS " +  urn.workComponent + " contains " + urn.workComponent)
            true
          }
        }*/
      }
    }

    /** True if passage reference in `urn` is contained
    * in or equal to the passage reference of this CtsUrn.
    *
    * @param urn CtsUrn to compare to this one.
    */
    def passageContains(urn: CtsUrn): Boolean = {
      //debug(s"LOOKING AT ${passageParts.size} PASSAGE PARTS: " + passageParts)
      //debug("AND COMPARING " + urn.passageParts.size)
      //debug("Passage empty?" + passageParts.isEmpty)

      if (urn.passageParts.isEmpty || passageParts.isEmpty ){
        false


      } else {
        val psg = dropSubref.passageComponent.replaceAll("\\.","\\\\.")
        val str = "(^" + psg + "\\.)|(^" + psg + "$)"
        val pttrn = str.r
        //debug("Use pattern " + pttrn )
        //debug("against " + dropSubref.passageComponent.toString)
        val res = pttrn.findFirstIn(urn.dropSubref.passageComponent.toString)

        res match {
          case None =>  false
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
      passageContains(urn) || urn.passageContains(this) || passageComponent.isEmpty || urn.passageComponent.isEmpty
    }
    /** true if the passage reference of either `urn`
    * of this CtsUrn is contained by the other.
    *
    * @param urn CtsUrn to compare to this one
    */
    def workMatch(urn: CtsUrn): Boolean = {
      (
        workContains(urn) ||
        urn.workContains(this) ||
        (this.workComponent == urn.workComponent)
      )
    }

    /** True if this [[CtsUrn]] contains a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def >(urn: CtsUrn): Boolean = {
      val unlikelyThis: CtsUrn = {
        if (this.isRange) {
          val uv = this.rangeToUrnVector
          if (uv.head == uv.last) uv.head else this
        } else this
      }
      val unlikelyUrn: CtsUrn = {
        if (urn.isRange) {
          val uv = urn.rangeToUrnVector
          if (uv.head == uv.last) uv.head else urn
        } else urn
      }
      ((unlikelyThis >= unlikelyUrn) && (unlikelyUrn != unlikelyThis))
    }

    /** True if this [[CtsUrn]] contains or is equal to a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def >=(urn: CtsUrn): Boolean = {
      if (urn == this) {true} else {
        if (passageComponent.nonEmpty) {

          if (this.isRange) {
            val urnV: Vector[CtsUrn] = this.rangeToUrnVector
            val checkFirst = urnV.head >= urn
            val checkLast = urnV.last >= urn
            ( checkFirst && checkLast )
          } else if ( urn.isRange ) {
            val urnV: Vector[CtsUrn] = urn.rangeToUrnVector
            val checkFirst = this >= urnV.head
            val checkLast = this >= urnV.last
            ( checkFirst && checkLast )
          } else if (this.isRange && urn.isRange) {
            val urnV: Vector[CtsUrn] = urn.rangeToUrnVector
            val thisV: Vector[CtsUrn] = this.rangeToUrnVector
            val checkFirstFirst = thisV.head >= urnV.head
            val checkFirstLast = thisV.head >= urnV.last
            val checkLastFirst = thisV.last >= urnV.head
            val checkLastLast = thisV.last >= urnV.last
            ( checkFirstFirst && checkFirstLast && checkLastFirst && checkLastLast )
          } else {
            //debug(s"got here with ${this} and ${urn}")
            val bottomLine = (
              (workContains(urn)) || (this.workComponent == urn.workComponent )
            ) &&  (
              (passageContains(urn)) || (urn.passageComponent == this.passageComponent)
            )
            bottomLine
          }
        } else {
          //debug("Same workcomponent? " + (this.workComponent == urn.workComponent))
          //debug("Other urn has passge? " + urn.passageComponent.nonEmpty)
          val bottomLine = (
            ((this.workComponent == urn.workComponent) && urn.passageComponent.nonEmpty) ||
            workContains(urn)
          )
         //debug("Bottom line for urn with no passage is " + bottomLine + "\n\n")
         bottomLine
        }
      }
    }


    /** True if this [[CtsUrn]] is contained by or equal to a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def <=(urn: CtsUrn): Boolean = {
      urn >= this
    }

    /** True if this [[CtsUrn]] is contained by a given [[CtsUrn]].
    *
    * @param urn CtsUrn to compare with this one.
    */
    def < (urn: CtsUrn): Boolean = {
        ((this <= urn) && (urn != this))
    }

    /** True if this [[CtsUrn]] is URN-similar a given [[CtsUrn]].
    *
    * @param u URN to compare.
    */
    def ~~(urn: CtsUrn): Boolean = {
      //debug("Twiddleing with " + this +  " against " + urn)
      //debug("NS == ? "+ namespace == urn.namespace )
      //debug("workMatch? " + workMatch(urn))
      //debug("passgeMatch? " + passageMatch(urn))
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
        case _ => {
          val msg = "Can only match CtsUrn against a second CtsUrn"
          warn(msg)
          throw CiteException(msg)
        }
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
        case _ => {
          val msg = "Can only match CtsUrn against a second CtsUrn"
          warn(msg)
          throw CiteException(msg)
        }
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
      require((workParts.size < 5), "invalid URN syntax. Too many parts in work component " + workComponent )

      require(passageSyntaxOk, "Invalid URN syntax.  Error in passage component " + passageComponent)

      val repeatedPeriods = ".*\\.{2}.*"
      val badWorkSyntax = workComponent.matches(repeatedPeriods)
      //debug(s"MATCH REPEATED PERIODS on ${urnString} ? " + badWorkSyntax)
      require(badWorkSyntax ==  false, "Invalid URN syntax in work component of " + urnString)

      for (p <- rangeBeginParts) {
        require(p.nonEmpty,"invalid passage syntax in range beginning" + urnString)
      }
      for (p <- rangeEndParts) {
        require(p.nonEmpty,"invalid passage syntax in range ending" + urnString)
      }

/*
      if (passageComponent.nonEmpty) {
        if (isRange) {
          val r1DotParts = rangeBegin.split("""\.""")
          for (p <- r1DotParts) {
            require(p.nonEmpty,"invalid passage syntax in range beginning of " + urnString)
          }

          val r2DotParts = rangeEnd.split("""\.""")
          for (p <- r2DotParts) {
            require(p.nonEmpty,"invalid passage syntax in range ending of " + urnString)
          }
          true

        }  else {
          val nodeDotParts = passageNode.split("""\.""")
          for (p <- nodeDotParts) {
            require(p.nonEmpty,"invalid passage syntax in " + urnString)
          }
          true
        }
        true
      }
*/

      /*
      if (components.size == 5) {
        urnString.last match {
          case ':' => {
            val msg = "Invalid URN syntax: trailing colon in " + urnString
            warn(msg)
            throw CiteException(msg)
          }
          case _ =>  true
        }
      } else {
        true
      }*/
      true
    }


    /** True if URN is an exemplar.*/
    def isExemplar: Boolean = {
      workLevel match {
        case  WorkLevel.Exemplar => true
        case _ => false
      }
    }

    /** True if URN is a version.*/
    def isVersion: Boolean = {
      workLevel match {
        case  WorkLevel.Version => true
        case _ => false
      }
    }

    /** True if URN is a notional work.*/
    def isNotional: Boolean = {
      workLevel match {
        case  WorkLevel.Work => true
        case _ => false
      }
    }


    /** True if URN is a notional work.*/
    def isTextGroup: Boolean = {
      workLevel match {
        case  WorkLevel.TextGroup => true
        case _ => false
      }
    }

    /** True if URN is a version or exemplar.
    */
    def concrete: Boolean = {
      isVersion || isExemplar
    }

    // Require fully valid syntax:
    require(fullyValid)

  }


  /** Enumeration of levels of the CTS work hierarchy. */
  @JSExportTopLevel("WorkLevel")
  object WorkLevel extends Enumeration {
    val TextGroup, Work, Version, Exemplar = Value
  }



}
