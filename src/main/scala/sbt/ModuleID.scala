package sbt 

case class ModuleID(
  organization: String,
  name: String,
  revision: String,
  crossVersion: CrossVersion = CrossVersion.Disabled
) {
  def cross(v: CrossVersion): ModuleID = copy(crossVersion = v)
}