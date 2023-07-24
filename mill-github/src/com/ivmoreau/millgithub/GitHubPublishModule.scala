package com.ivmoreau.millgithub

import mill._
import mill.api.Result.{Failure, Success}
import mill.api.Result
import mill.define.{Command, ExternalModule, Task}
import mill.scalalib.publish.Artifact
import scalalib._

trait GitHubPublishModule extends PublishModule { outer =>

  def publishRepository: ProjectRepository

  def skipPublish: Boolean = false

  def tokenLookup: GitHubCredentialsLookup = new GitHubCredentialsLookup {}

  def githubHeaders(
      systemProps: Map[String, String] = sys.props.toMap
  ): Task[GitHubAuthHTTP] = T.task {
    val auth = tokenLookup.resolveGitHubToken(T.env)
    auth match {
      case Left(msg) =>
        Failure(
          s"Token lookup for PUBLISH repository ($publishRepository) failed with $msg"
        ): Result[GitHubAuthHTTP]
      case Right(value) => Success(value)
    }
  }

  def publishGitHub(
      readTimeout: Int = 60000,
      connectTimeout: Int = 5000
  ): define.Command[Unit] = T.command {

    val githubRepo = publishRepository

    val PublishModule.PublishData(artifactInfo, artifacts) = publishArtifacts()
    if (skipPublish) {
      T.log.info(s"SkipPublish = true, skipping publishing of $artifactInfo")
    } else {
      val uploader =
        new GitHubUploader(githubHeaders()(), readTimeout, connectTimeout)
      new GitHubPublisher(
        uploader.upload,
        githubRepo,
        T.log
      ).publish(artifacts.map { case (a, b) => (a.path, b) }, artifactInfo)
    }

  }
}

object GitHubPublishModule extends ExternalModule {

  lazy val millDiscover: mill.define.Discover[this.type] =
    mill.define.Discover[this.type]
}
