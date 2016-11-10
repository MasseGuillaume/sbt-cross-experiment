package scalanative

import sbtcross.AutoImports._
case object Native extends Platform
object AutoImports {
  implicit class NativeExtensions(project: CrossProject) {
    def nativeSettings(settings: (String, String)*): CrossProject = project
  }
}