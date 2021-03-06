package scalajs

import sbt._
import scala.language.implicitConversions

final class CrossProject private[scalajs]() {
  def jsSettings[T](v: T): CrossProject = this
  def jvmSettings[T](v: T): CrossProject = this
  def settings[T](v: T): CrossProject = this
}

object AutoImports {

  def crossProject: CrossProject = new CrossProject()

  final implicit def toScalaJSGroupID(groupID: String): ScalaJSGroupID = new ScalaJSGroupID(groupID)
  final class CrossGroupArtifactID(groupID: String, artifactID: String, crossVersion: CrossVersion) {
    def %(revision: String): ModuleID = {
      ModuleID(groupID, artifactID, revision).cross(crossVersion)
    }
  }

  object ScalaJSCrossVersion {
    private val scalaJSVersionUnmapped: String => String =
      _ => s"sjs$currentBinaryVersion"

    private val scalaJSVersionMap: String => String =
      version => s"${version}_sjs${currentBinaryVersion}"

    val currentBinaryVersion = "0.6"
    def scalaJSMapped(cross: CrossVersion): CrossVersion = cross match {
      case CrossVersion.Disabled =>
        CrossVersion.binaryMapped(scalaJSVersionUnmapped)
      case cross: CrossVersion.Binary =>
        CrossVersion.binaryMapped(scalaJSVersionMap andThen cross.remapVersion)
      case cross: CrossVersion.Full =>
        CrossVersion.fullMapped(scalaJSVersionMap andThen cross.remapVersion)
    }

    val binary: CrossVersion = scalaJSMapped(CrossVersion.binary)
    val full: CrossVersion = scalaJSMapped(CrossVersion.full)
  }

  object ScalaJSGroupID {
    def withCross(groupID: ScalaJSGroupID, artifactID: String, cross: CrossVersion): CrossGroupArtifactID = {
      new CrossGroupArtifactID(groupID.groupID, artifactID, cross)
    }
  }

  final class ScalaJSGroupID private[scalajs] (private val groupID: String) {
    def %%%(artifactID: String): CrossGroupArtifactID = {
      val cross =
        if(true) ScalaJSCrossVersion.binary
        else CrossVersion.binary

      ScalaJSGroupID.withCross(groupID, artifactID, cross)
    }
  }
}