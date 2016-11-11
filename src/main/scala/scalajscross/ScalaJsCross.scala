package scalajscross

import sbt._
import sbtcross._

import scala.language.implicitConversions

// scalajs % provided

object AutoImports {
  case object JSPlatform extends Platform
  
  implicit class JsExtensions(project: CrossProject) {
    def jsSettings(xs: SettingsDelta*): CrossProject = {
      require(project.platforms.contains(JSPlatform))
      project
    }
  }
}