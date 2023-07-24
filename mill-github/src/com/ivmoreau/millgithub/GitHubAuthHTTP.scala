package com.ivmoreau.millgithub

import requests.RequestAuth

/** Actual headers to inject to http requests to gitlab.
  *
  * @param headers
  *   header -> value pairs
  */
case class GitHubAuthHTTP(auth: RequestAuth.Basic, user: String, token: String)

object GitHubAuthHTTP {
  def apply(user: String, token: String): GitHubAuthHTTP =
    GitHubAuthHTTP(new RequestAuth.Basic(user, token), user, token)
}
