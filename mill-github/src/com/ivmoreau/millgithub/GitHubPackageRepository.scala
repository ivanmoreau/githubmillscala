package mill.contrib.gitlab

import mill.scalalib.publish.Artifact

sealed trait GitHubPackageRepository {
  def url(): String
}

// Could also support project name (https://docs.gitlab.com/ee/user/packages/maven_repository/index.html#project-level-maven-endpoint)
// though only ID can be used for publishing.
case class ProjectRepository(owner: String, repository: String)
    extends GitHubPackageRepository {
  override def url(): String =
    s"https://maven.pkg.github.com/$owner/$repository"

  // https://docs.gitlab.com/ee/api/packages/maven.html#upload-a-package-file
  def uploadUrl(artifact: Artifact): String = {
    val repoUrl = url()
    val group = artifact.group.replace(".", "/")
    val id = artifact.id
    val version = artifact.version
    s"$repoUrl/$group/$id/$version"
  }
}
