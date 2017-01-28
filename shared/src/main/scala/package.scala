package edu.holycross.shot

/** Library for working with canonical identifers
* used in citing scholarly resources. */
package object cite {


  /** Regular expression matching text part of a subreference on a CTS URN passage node.*/
  val subrefTextRE = """([^\[]+).*""".r
  /** Regular expression matching the index part of a subreference on a CTS URN passage node.*/
  val subrefIndexRE = """[^\[]+\[([^\]]+)\]""".r


  /** Extracts the subref from a passage node value.
  *
  * @param s A passage node value, a reference to
  * a single node or to the beginning or ending node
  * of a range reference.
  */
  def subrefOption(s: String): Option[String] = {
    val psgSplit = s.split("@")
    psgSplit.size match {
      case 2 => Some(psgSplit(1))
      case _ => None
    }
  }


  /** Extracts the cited text of a subref from a passage node value.
  *
  * @param s A passage node value, a reference to
  * a single node or to the beginning or ending node
  * of a range reference.
  */
  def subrefTextOption(s: String): Option[String] = {

    val psgSplit = s.split("@")
    psgSplit.size match {
      case 2 => {

        psgSplit(1) match {
         case subrefTextRE(sr) => Some(sr)
         case default => None
        }
      }
      case _ => None
    }
  }


  /** Extracts the explicitly given index of a subref
  * from a passage node value, or the default value of 1.
  *
  * The index value must be an integer.
  * @param s A passage node value, a reference to
  * a single node or to the beginning or ending node
  * of a range reference.
  */
  def subrefIndexOption(subref: String): Option[Int] = {
    val psgSplit = subref.split("@")
    psgSplit.size match {
      case 2 => {
        psgSplit(1) match {
          case subrefIndexRE(i) => try {
            Some(i.toInt)
          } catch {
            case e: NumberFormatException => throw CiteException("non-integer subreference index in " + subref)
          }
          case s :  String => if (psgSplit(0).nonEmpty) Some(1) else None
        }
      }
      case _ => None
    }
  }

}
