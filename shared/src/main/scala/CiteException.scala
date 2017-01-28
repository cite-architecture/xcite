package edu.holycross.shot

package cite {

  case class CiteException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}
