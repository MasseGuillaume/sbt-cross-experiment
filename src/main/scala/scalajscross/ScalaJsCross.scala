package scalajscross

import sbt._

object AutoImports {
  import sbtcross.AutoImports._
  case object JSPlatform extends Platform
  implicit class JsExtensions(project: CrossProject) {
    def jsSettings(xs: SettingsDelta*): CrossProject = {
      require(project.platforms.contains(JSPlatform))
      project
    }
  }
}