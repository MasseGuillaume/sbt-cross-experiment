package sbt

sealed trait SettingsDelta {
  def apply(project: Project): Project
}
case class LibraryDependenciesSettingsDelta(libraryDependencies: Seq[ModuleID] = Seq()) extends SettingsDelta {
  def +=(module: ModuleID) = copy(libraryDependencies = module +: libraryDependencies)
  def apply(project: Project): Project = 
    project.copy(
      libraryDependencies = libraryDependencies ++ project.libraryDependencies
    )
}

object Keys {
  val libraryDependencies = LibraryDependenciesSettingsDelta()
}