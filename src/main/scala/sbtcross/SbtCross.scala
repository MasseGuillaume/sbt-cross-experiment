package sbtcross 

import sbt._
import scala.language.implicitConversions

final class DeprecatedScalaJSGroupID (private val groupID: String) {
  def %%%(artifactID: String): GroupArtifactID = f(artifactID)
  def %%%!(artifactID: String): GroupArtifactID = f(artifactID)
  private def f(artifactID: String) = new GroupArtifactID(groupID, artifactID, CrossVersion.binary)
}

final class GroupArtifactID private[sbtcross] (
  private[sbtcross] val groupID: String,
  private[sbtcross] val artifactID: String,
  private[sbtcross] val crossVersion: CrossVersion) {
    def %(revision: String): ModuleID =
    ModuleID(groupID, artifactID, revision).cross(crossVersion)
}
trait Platform
case object JVMPlatform extends Platform

private [sbtcross] object CrossProject {
  def apply(platforms: Set[Platform]): CrossProject = new CrossProject(platforms)
}
case class CrossProject private [sbtcross](
  val platforms: Set[Platform],
  private val crossSettings: Seq[SettingsDelta] = Seq(),
  private val platformsSettings: Map[Platform, Seq[SettingsDelta]] = Map()) {

  def settings(settings: SettingsDelta*): CrossProject = copy(crossSettings = settings.toSeq)
  def build: List[Project] = {

    platforms.foldLeft(List.empty[Project]){ case (projects, platform) =>
      val newProject = Project()
      val crossAppliedProject = newProject(crossSettings)
      val platformAppliedProject =
        platformsSettings.get(platform).map{platformSettings =>
          crossAppliedProject(platformSettings)
        }.getOrElse(crossAppliedProject)
      platformAppliedProject :: projects
    }
  }
}

object AutoImports {
  val JVMPlatform = sbtcross.JVMPlatform
  implicit class JvmExtensions(project: CrossProject) {
    def jvmSettings(settings: SettingsDelta*): CrossProject = {
      require(project.platforms.contains(JVMPlatform))
      project
    }
  }

  // final implicit def toCrossGroupID(groupID: String): CrossGroupID = 
  //   new CrossGroupID(groupID)

  // final class CrossModuleID(
  //   organization: String,
  //   name: String,
  //   revision: String,
  //   crossVersion: CrossVersion = Disabled) {

  //   def cross(v: CrossVersion): CrossModuleID = copy(crossVersion = v)
  //   def apply(platform: Platform): ModuleID = ???
  // }


  // final class CrossGroupArtifactID(groupID: String, artifactID: String, crossVersion: CrossVersion) {
  //   def %(revision: String): ModuleID = {
  //     ModuleID(groupID, artifactID, revision).cross(crossVersion)
  //   }
  // }

  // final class CrossGroupID private[scalajs] (private val groupID: String) {
  //   def %%(artifactID: String): CrossGroupArtifactID = {
  //     // val cross =
  //     //   if(true) CrossCrossVersion.binary
  //     //   else CrossVersion.binary

  //     CrossGroupID.withCross(groupID, artifactID, cross)
  //   }
  // }

  // object CrossGroupID {
  //   def withCross(groupID: CrossGroupID, artifactID: String, cross: CrossVersion): CrossGroupArtifactID = {
  //     new CrossGroupArtifactID(groupID.groupID, artifactID, cross)
  //   }
  // }
}