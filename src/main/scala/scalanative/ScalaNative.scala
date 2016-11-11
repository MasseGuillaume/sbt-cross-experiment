package scalanative

import sbt._
import sbtcross._

object AutoImports {
  case object NativePlatform extends Platform
  implicit class NativeExtensions(project: CrossProject) {
    def nativeSettings(xs: SettingsDelta*): CrossProject = {
      require(project.platforms.contains(NativePlatform))
      project
    }
  }
}