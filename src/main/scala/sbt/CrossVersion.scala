package sbt

sealed trait CrossVersion
object CrossVersion {
  object Disabled extends CrossVersion { 
    override def toString = "Disabled" 
  }
  final case class Binary(val remapVersion: String => String) extends CrossVersion {
    override def toString = "Binary" 
  }
  final case class Full(val remapVersion: String => String) extends CrossVersion { 
    override def toString = "Full" 
  }

  private[this] def idFun[T]: T => T = x => x
  def full: CrossVersion = new Full(idFun)
  def fullMapped(remapVersion: String => String): CrossVersion = new Full(remapVersion)
  def binary: CrossVersion = new Binary(idFun)
  def binaryMapped(remapVersion: String => String): CrossVersion = new Binary(remapVersion)
}
