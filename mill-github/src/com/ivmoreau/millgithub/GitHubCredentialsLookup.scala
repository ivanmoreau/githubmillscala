package com.ivmoreau.millgithub

import scala.util.Try

trait GitHubCredentialsLookup {
  import GitHubCredentialsLookup._

  // Default search places for token
  def personalToken: String = "GITHUB_TOKEN"
  def personalUsername: String = "GITHUB_USERNAME"
  def ciToken: String = "CI_GITHUB_TOKEN"

  // Default token search order. Implementation picks first found and does not look for the rest.
  def tokenSearchOrder: Seq[GitHubToken] = Seq(
    Personal(Env(personalUsername), Env(personalToken)),
    CI(Env(ciToken))
  )

  // Finds gitlab token from this environment. Overriding this is not generally necessary.
  def resolveGitHubToken(
      env: Map[String, String]
  ): Either[String, GitHubAuthHTTP] = {

    val token = LazyList
      .from(tokenSearchOrder)
      .map(token => buildHeaders(token, env))
      .find(_.isRight)
      .flatMap(_.toOption)

    token match {
      case None =>
        Left(s"Unable to find token from $tokenSearchOrder")
      case Some(headers) => Right(headers)
    }
  }

  // Converts GitlabToken to GitlabAuthHeaders. Overriding this is not generally necessary.
  def buildHeaders(
      token: GitHubToken,
      env: Map[String, String]
  ): Either[String, GitHubAuthHTTP] = {

    def readSource(source: TokenSource): Either[String, String] =
      source match {
        case Env(name) =>
          env.get(name).toRight(s"Could not read environment variable $name")
        case Custom(f) => f()
      }

    token match {
      case Personal(name, token) =>
        readSource(name).flatMap(n =>
          readSource(token).map(GitHubAuthHTTP(n, _))
        )
      case CI(token) =>
        readSource(token).map(GitHubAuthHTTP.ciToken)
      case CustomT(name, token) =>
        readSource(name).flatMap(n =>
          readSource(token).map(GitHubAuthHTTP(n, _))
        )
    }
  }

}

object GitHubCredentialsLookup {

  /** Possible types of a Gitlab authentication header.
    *   - Personal = "Private-Token" ->
    *   - Deploy = "Deploy-Token"->
    *   - CIJob = "Job-Token" ->
    *   - CustomHeader = Use with TokenSource/Custom to produce anything you
    *     like
    *
    * Currently only one custom header is supported. If you need multiple
    * override gitlabToken from GitlabPublishModule directly
    */
  sealed trait GitHubToken {}
  case class Personal(name: TokenSource, token: TokenSource) extends GitHubToken
  case class CI(token: TokenSource) extends GitHubToken
  case class CustomT(name: TokenSource, token: TokenSource) extends GitHubToken

  /** Possible source of token value. Either an
    *   - Env = Environment variable
    *   - Property = Javas system property
    *   - File =Contents of a file on local disk.
    *   - Custom = Own function
    *
    * Possible additions, that can now be supported with Custom: KeyVault, Yaml,
    * etc..
    */
  sealed trait TokenSource
  case class Env(name: String) extends TokenSource
  case class Custom(f: () => Either[String, String]) extends TokenSource
}
