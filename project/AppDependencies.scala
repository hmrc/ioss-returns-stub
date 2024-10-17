import play.core.PlayVersion
import sbt.*

object AppDependencies {

  private val bootstrapVersion = "9.5.0"


  val compile = Seq(
    "uk.gov.hmrc"                 %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "com.github.java-json-tools"  % "json-schema-validator"       % "2.2.14"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30" % bootstrapVersion      % "test, it",
    "org.scalatest"           %% "scalatest"              % "3.2.15"              % Test,
    "org.playframework"       %% "play-test"              % PlayVersion.current   % Test,
    "com.vladsch.flexmark"    % "flexmark-all"            % "0.64.6"              % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"     % "5.1.0"               % "test, it"
  )
}
