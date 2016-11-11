package sbt 

import CrossVersion._

case class ModuleID(
  organization: String,
  name: String,
  revision: String,
  crossVersion: CrossVersion = Disabled
) {
  def cross(v: CrossVersion): ModuleID = copy(crossVersion = v)
  override def toString = {
    val artifact = 
      crossVersion match {
        case Disabled  => name
        case Binary(f) => f(name)
        case Full(f)   => f(name)
      }
    s"$organization $artifact $revision"
  }
}