package com.ivmoreau.millgithub

import scala.concurrent.duration._

object GitHubUploader {
  type Upload = (String, Array[Byte]) => requests.Response
}

class GitHubUploader(
    authentication: GitHubAuthHTTP,
    readTimeout: Int = 5000,
    connectTimeout: Int = 5000
) {
  val http = requests.Session(
    readTimeout = readTimeout,
    connectTimeout = connectTimeout,
    maxRedirects = 0,
    check = false
  )

  private val uploadTimeout = 2.minutes.toMillis.toInt

  // https://docs.gitlab.com/ee/user/packages/maven_repository/#publish-a-package
  def upload(uri: String, data: Array[Byte]): requests.Response = {
    http.put(
      uri,
      auth = authentication.auth,
      readTimeout = uploadTimeout,
      data = data
    )
  }
}
