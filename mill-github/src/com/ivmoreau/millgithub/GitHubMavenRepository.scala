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
  def githubRepository: GitHubPackageRepository // For package discovery

  def mavenRepository: Task[MavenRepository] = T.task {

    val githubAuth = tokenLookup
      .resolveGitHubToken(T.env)
      .map(auth => Authentication(user = auth.user, password = auth.token))
      .map(auth => MavenRepository(githubRepository.url(), Some(auth)))

    githubAuth match {
      case Left(msg) =>
        Failure(
          s"UnU token wookup fow package wepositowy (${githubRepository.url()}) faiwed with $msg OwO"
        ): Result[MavenRepository]
      case Right(value) => Success(value)
    }
  }
}
