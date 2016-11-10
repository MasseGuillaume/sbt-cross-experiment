package scalajscross

object AutoImports {
  import sbtcross.AutoImports._
  case object JS extends Platform
  implicit class JsExtensions(project: CrossProject) {
    def foo = 1
    def jsSettings(settings: (String, String)*): CrossProject = project
  }
}