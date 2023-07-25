import coursierapi.MavenRepository


interp.repositories.update(
  interp.repositories() ++ Seq(MavenRepository.of(
    "https://jitpack.io"
  )))

@

import $ivy.`com.ivmoreau.githubmillscala::mill-github::cd1c30e186`
import $ivy.`de.tototec::de.tobiasroeser.mill.vcs.version::0.4.0`

import mill._
import scalalib._
import mill.scalalib.api.ZincWorkerUtil.scalaNativeBinaryVersion
import mill.scalalib.publish._

import de.tobiasroeser.mill.vcs.version.VcsVersion
import com.ivmoreau.millgithub.GitHubPublishModule
import com.ivmoreau.millgithub.ProjectRepository

val millVersions                           = Seq("0.10.12", "0.11.1")
def millBinaryVersion(millVersion: String) = scalaNativeBinaryVersion(millVersion)

object `mill-github` extends Cross[MillGitHubCross](millVersions: _*)
class MillGitHubCross(millVersion: String) extends CrossModuleBase
with GitHubPublishModule {
  override def crossScalaVersion = "2.13.10"
  override def artifactSuffix    = s"_mill${millBinaryVersion(millVersion)}" + super.artifactSuffix()

  override def compileIvyDeps = super.compileIvyDeps() ++ Agg(
    ivy"com.lihaoyi::mill-main:$millVersion",
    ivy"com.lihaoyi::mill-scalalib:$millVersion"
  )

  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"org.scala-lang.modules::scala-collection-compat:2.11.0",
    ivy"org.scala-lang.modules::scala-java8-compat:1.0.2"
  )

  override def publishRepository: ProjectRepository = ProjectRepository("ivanmoreau", "githubmillscala")

  override def publishVersion = VcsVersion.vcsState().format()
  def pomSettings = PomSettings(
    description = "A Github plugin for the Mill build tool",
    organization = "com.ivmoreau",
    url = "https://github.com/ivanmoreau",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("ivanmoreau", "github.mill.scala"),
    developers = Seq(Developer("ivanmoreau", "Ivan Molina Rebolledo", "https://github.com/ivanmoreau"))
  )
}
