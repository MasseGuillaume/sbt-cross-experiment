import sbtcross.AutoImports._

import sbt._
import scala.language.implicitConversions

package object sbtcross {
  @deprecated("use %%", "now")
  final implicit def toScalaJSGroupID(groupID: String) = 
    new DeprecatedScalaJSGroupID(groupID)

  @deprecated("crossProject is now crossProject(JSPlatform, JVMPlatform)", "now")
  def crossProject: CrossProject = 
    sys.error("crossProject is now crossProject(JSPlatform, JVMPlatform)")

  def crossProject(platforms: Platform*): CrossProject =
    CrossProject(platforms.toSet)
}