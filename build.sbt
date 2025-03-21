import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import play.sbt.routes.RoutesKeys
import play.sbt.PlayImport.PlayKeys

lazy val microservice = Project("ioss-returns-stub", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    majorVersion := 0,
    scalaVersion := "3.3.4",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
    // suppress warnings in generated routes files
    scalacOptions += "-Wconf:src=routes/.*:s",
    RoutesKeys.routesImport ++= Seq(
      "java.time.LocalDate",
      "uk.gov.hmrc.iossreturnsstub.models._"
    ),
    PlayKeys.playDefaultPort := 10195
  )
  .settings(PlayKeys.playDefaultPort := 10195)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(CodeCoverageSettings.settings: _*)
  .settings(scalacOptions += "-Wconf:msg=Flag.*repeatedly:s")
