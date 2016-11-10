package sbtcross

object AutoImports {   
  trait Platform
  case object JVMPlatform extends Platform
  object CrossProject {
    def apply(platforms: Set[Platform]): CrossProject = new CrossProject(platforms)
  }
  class CrossProject private [sbtcross](private val platforms: Set[Platform]) {
    def settings(xs: (String, String)*): CrossProject = this
  }
  
  @deprecated("[3] migration instructions via deprecation", "always")
  def crossProject: CrossProject = sys.error("[1] migration instructions via runtime error")

  def crossProject(platforms: Platform*): CrossProject = {
    if(platforms.isEmpty) sys.error("[2] migration instructions via runtime error")
    else CrossProject(platforms.toSet)
  }
  
  implicit class JvmExtensions(project: CrossProject) {
    def jvmSettings(settings: (String, String)*): CrossProject = project
  }
}