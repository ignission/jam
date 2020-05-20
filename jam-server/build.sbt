name := "jam-server"

addCommandAlias("fix", "all compile:scalafix; openviduClient/scalafix") // TODO: test:scalafix
addCommandAlias(
  "fixCheck",
  "; compile:scalafix --check; openviduClient/scalafix --check"
) // TODO:  ; test:scalafix --check

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.3.1-RC1"

lazy val commonSettings = Seq(
  version := "0.2.0-SNAPSHOT",
  scalaVersion := "2.13.2",
  organization := "tech.ignission",
  test in assembly := {},
  // scalafix
  semanticdbEnabled := true,
  semanticdbVersion := "4.3.10",
  addCompilerPlugin(scalafixSemanticdb),
  scalacOptions ++= List(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Yrangepos",
    "-Ywarn-unused",
    "-Xlint",
    "-Xfatal-warnings"
  )
)

val catsVersion     = "2.1.1"
val monixVersion    = "3.2.0"
val akkaHttpVersion = "10.1.12"
val akkaVersion     = "2.6.4"
val log4j2Version   = "2.13.2"

lazy val openviduClient = (project in file("openvidu-client")).settings(commonSettings)

lazy val domain = (project in file("jam-domain")).settings(commonSettings)

lazy val infra = (project in file("jam-infrastructure")).settings(commonSettings).dependsOn(domain)

lazy val server = (project in file("jam-server"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel"           %% "cats-core"            % catsVersion,
      "io.monix"                %% "monix"                % monixVersion,
      "com.typesafe.akka"       %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka"       %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka"       %% "akka-stream"          % akkaVersion,
      "com.typesafe.akka"       %% "akka-actor"           % akkaVersion,
      "org.apache.logging.log4j" % "log4j-core"           % log4j2Version,
      "org.apache.logging.log4j" % "log4j-api"            % log4j2Version,
      "org.apache.logging.log4j" % "log4j-slf4j-impl"     % log4j2Version,
      "com.typesafe"             % "config"               % "1.4.0",
      "ch.megard"               %% "akka-http-cors"       % "0.4.3",
      "org.scalatest"           %% "scalatest"            % "3.1.2"         % "test",
      "com.typesafe.akka"       %% "akka-stream-testkit"  % akkaVersion     % "test",
      "com.typesafe.akka"       %% "akka-http-testkit"    % akkaHttpVersion % "test"
    ),
    assemblyJarName in assembly := "jam-server.jar"
  )
  .dependsOn(domain, infra, openviduClient)
