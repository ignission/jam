import play.sbt.PlayImport.PlayKeys.devSettings

name := "jam"

lazy val commonSettings = Seq(
  version := "0.4.1",
  scalaVersion := "2.13.6",
  scalacOptions ++= List(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-language:implicitConversions",
    "-Yrangepos",
    "-Ymacro-annotations",
    "-Ywarn-unused",
    "-Xlint",
    "-Xfatal-warnings"
  ),
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % "1.0.8"
  ),
  // scalafix
  addCompilerPlugin(scalafixSemanticdb),
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision
)

lazy val playCommonSettings = Seq(
  scalacOptions += s"-Wconf:src=${target.value}/.*:s",
  PlayKeys.playRunHooks ++= Seq(
    DockerComposeRunHook(baseDirectory.value.getParentFile),
    FrontendRunHook(baseDirectory.value.getParentFile / "jam-client")
  ),
  PlayKeys.fileWatchService := {
    lazy val isMac = System.getProperties.get("os.name") == "Mac OS X"
    val logger     = play.sbt.run.toLoggerProxy(sLog.value)
    if (System.getProperties.get("os.arch") == "aarch64") {
      // For Apple M1
      play.dev.filewatch.FileWatchService.jdk7(logger)
    } else
      play.dev.filewatch.FileWatchService.default(logger, isMac)
  }
)

lazy val domain = (project in file("jam-domain"))
  .settings(commonSettings)

lazy val application = (project in file("jam-application"))
  .settings(commonSettings)
  .dependsOn(domain)

lazy val server = (project in file("jam-server"))
  .enablePlugins(PlayScala)
  .settings(commonSettings)
  .settings(playCommonSettings)
  .settings(
    libraryDependencies ++= Seq(
      guice,
      jdbc,
      "net.debasishg"          %% "redisclient"        % "3.30",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
    )
  )
  .settings(
    Docker / packageName := s"jam-${name.value}",
    dockerBaseImage := "openjdk:8-slim",
    dockerExposedPorts ++= Seq(9000),
    Universal / javaOptions ++= Seq(
      "-Dpidfile.path=/dev/null",
      "-Dconfig.file=/opt/docker/conf/docker.conf"
    )
  )
  .settings(
    devSettings := Map("play.server.http.port" -> "9000").toSeq
  )
  .dependsOn(domain)
  .dependsOn(application)
  .dependsOn(infrastructure)

lazy val infrastructure = (project in file("jam-infrastructure"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.google.code.gson" % "gson"                 % "2.8.7",
      "org.slf4j"            % "slf4j-api"            % "1.7.30",
      "io.openvidu"          % "openvidu-java-client" % "2.17.0"
    )
  )
  .dependsOn(domain)
  .dependsOn(application)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    Compile / run := (server / Compile / run).evaluated
  )
  .aggregate(domain)
  .aggregate(application)
  .aggregate(infrastructure)
  .aggregate(server)

addCommandAlias("fixAll", "scalafixAll; scalafmtAll; scalafmtSbt")
addCommandAlias("checkAll", "scalafixAll --check; scalafmtCheckAll; scalafmtSbtCheck")

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"

Global / onChangedBuildSource := ReloadOnSourceChanges
