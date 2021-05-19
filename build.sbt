name := "jam"

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.13.5",
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
  // scalafix
  addCompilerPlugin(scalafixSemanticdb),
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision
)

lazy val messaging = (project in file("jam-messaging"))
  .settings(commonSettings)
  .enablePlugins(PlayScala)
  .settings(
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

lazy val root = (project in file("."))
  .settings(commonSettings)
  .aggregate(messaging)

addCommandAlias("fixAll", "scalafixAll; scalafmtAll; scalafmtSbt")
addCommandAlias("checkAll", "scalafixAll --check; scalafmtCheckAll; scalafmtSbtCheck")

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"

Global / onChangedBuildSource := ReloadOnSourceChanges
