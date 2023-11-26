import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  private val bootstrapVersion = "7.23.0"


  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % bootstrapVersion,
    "com.github.java-json-tools" % "json-schema-validator" % "2.2.14"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-28" % bootstrapVersion % "test, it",
    "org.scalatest" %% "scalatest" % "3.2.12" % Test,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % Test,
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2" % "test, it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test, it"
  )
}
