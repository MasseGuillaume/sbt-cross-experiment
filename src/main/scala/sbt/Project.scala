package sbt

case class Project(libraryDependencies: Seq[ModuleID] = Seq()) {
  def apply(deltas: Seq[SettingsDelta]): Project = {
    deltas.foldLeft(this){ case (project, delta) => delta(project) }
  }
}