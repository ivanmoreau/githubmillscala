# Publish module for GitHub maven repo / Mill

[![Release](https://jitpack.io/v/com.ivmoreau/githubmillscala.svg)](https://jitpack.io/#com.ivmoreau/githubmillscala)

The code was taken from the conntrib/Gitlab. :3

# Does this even work? Owo

suwpwisingwy yes, i-it does!=. the *runs away* onwy thing you nyeed t-to do is t-to impowt the x3 dependecy and set the x3 ENV vawiabwes.

[This is an exampwe of use](https://github.com/ivanmoreau/examplemillgithub):

```scala
import coursierapi.MavenRepository

interp.repositories.update(
  interp.repositories() ++ Seq(MavenRepository.of(
  "https://jitpack.io"
)))

@

import $ivy.`com.ivmoreau.githubmillscala::mill-github::9dcd1b8a48`

import mill._
import scalalib._
import mill.scalalib.publish._

import com.ivmoreau.millgithub.GitHubPublishModule
import com.ivmoreau.millgithub.ProjectRepository

object ex extends ScalaModule with GitHubPublishModule {
  override def scalaVersion = "3.2.2"

  override def publishRepository: ProjectRepository = ProjectRepository("ivanmoreau", "examplemillgithub")

  override def pomSettings: T[PomSettings] = PomSettings(
    organization = "com.ivmoreau",
    description = "Example of mill-github",
    url = "https://github.com/ivanmoreau/examplemillgithub",
    licenses = Seq(
      License.`MPL-2.0`
    ),
    versionControl = VersionControl.github("ivanmoreau","examplemillgithub"),
    developers = Seq(
      Developer(
        "ivanmoreau",
        "Ivan Molina Rebolledo",
        url = "ivmoreau.com"
      )))

  override def publishVersion: T[String] = "0.0.1"
}
```

You have `GITHUB_TOKEN` and `GITHUB_USERNAME` fow wocaw/devewopment

You have `CI_GITHUB_TOKEN` *runs away* fow github *sweats* CI. YOu nyeed t-to set up this vawiabwe in CI tho.

Aftew this, the x3 u-usage is simiwiaw ÚwÚ t-to [Gitwab Contwib Moduwe](https://mill-build.com/mill/contrib/gitlab.html)

You extend :3 GitHubPubwishModuwe in youw buiwd.sc and fiww the x3 missing fiewds.

Then t-to pubwish use somehting wike:

```bash
./mill ex.publishGitHub
```

# But this README is unreadable and unprofessional how you dare!!!???

Looks funny uwu :3. I bet someone is going to use this in production anyways Uwu.

# Contributions

Awways wewcome OwO. Pwease open an issue befowe making a puww wequest, or don't ÙwÚ, it's
not required but it's nice to know what's going on OwO.

# But why this is not in Maven Centwal? UwU

I don't know OwO. I don't know how to do that OwO. Someone pwease hewp me OwO. Ok no, 
i just have nevew done that, put maybe i wiww do it soon OwO. FOr now, you can use
Jitpack which is awways nice OwO.

# License

The one from the mill repo owo
