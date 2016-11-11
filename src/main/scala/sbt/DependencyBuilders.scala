package sbt

import scala.language.implicitConversions

trait DependencyBuilders {
  final implicit def toGroupID(groupID: String): GroupID = new GroupID(groupID)
  final class GroupID private[sbt] (private[sbt] val groupID: String) {
    def %(artifactID: String) = 
      groupArtifact(artifactID, CrossVersion.Disabled)

    def %%(artifactID: String): GroupArtifactID = 
      groupArtifact(artifactID, CrossVersion.binary)
    
    private def groupArtifact(artifactID: String, cross: CrossVersion) = 
      new GroupArtifactID(groupID, artifactID, cross)
  }
  final class GroupArtifactID private[sbt] (
    private[sbt] val groupID: String,
    private[sbt] val artifactID: String,
    private[sbt] val crossVersion: CrossVersion) {
      def %(revision: String): ModuleID =
      ModuleID(groupID, artifactID, revision).cross(crossVersion)
  }
}