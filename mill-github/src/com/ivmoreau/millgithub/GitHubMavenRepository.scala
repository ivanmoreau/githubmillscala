package com.ivmoreau.millgithub

import coursier.core.Authentication
import coursier.maven.MavenRepository
import mill.T
import mill.api.Result
import mill.api.Result.{Failure, Success}
import mill.define.Task

trait GitHubMavenRepository {

  def tokenLookup: GitHubCredentialsLookup =
    new GitHubCredentialsLookup {} // For token discovery
  def gitlabRepository: GitHubPackageRepository // For package discovery

  def mavenRepository: Task[MavenRepository] = T.task {

    val gitlabAuth = tokenLookup
      .resolveGitlabToken(T.env)
      .map(auth => Authentication(user = auth.user, password = auth.token))
      .map(auth => MavenRepository(gitlabRepository.url(), Some(auth)))

    gitlabAuth match {
      case Left(msg) =>
        Failure(
          s"UnU token wookup fow package wepositowy (${gitlabRepository.url()}) faiwed with $msg OwO"
        ): Result[MavenRepository]
      case Right(value) => Success(value)
    }
  }
}
