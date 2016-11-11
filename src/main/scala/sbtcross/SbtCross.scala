package sbtcross

import sbt._

object AutoImports {   
  trait Platform
  case object JVMPlatform extends Platform
  object CrossProject {
    def apply(platforms: Set[Platform]): CrossProject = CrossProject(platforms)
  }
  case class CrossProject private [sbtcross](
    val platforms: Set[Platform],
    private val crossSettings: Seq[SettingsDelta],
    private val platformsSettings: Map[Platform, Seq[SettingsDelta]]) {

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

  @deprecated("[3] migration instructions via deprecation", "always")
  def crossProject: CrossProject = sys.error("[1] migration instructions via runtime error")

  def crossProject(platforms: Platform*): CrossProject = {
    if(platforms.isEmpty) sys.error("[2] migration instructions via runtime error")
    else CrossProject(platforms.toSet)
  }
  
  implicit class JvmExtensions(project: CrossProject) {
    def jvmSettings(settings: SettingsDelta*): CrossProject = {
      require(project.platforms.contains(JVMPlatform))
      project
    }
  }
}