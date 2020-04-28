name := "emoi-server"

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.13.1",
  organization := "tech.ignission",
  test in assembly := {},
  scalacOptions ++= List(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint:unused"
  )
)

val catsVersion     = "2.1.1"
val monixVersion    = "3.2.0"
val akkaHttpVersion = "10.1.11"
val akkaVersion     = "2.6.4"
val log4j2Version   = "2.13.2"

lazy val root = (project in file("."))
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
      "org.scalatest"           %% "scalatest"            % "3.1.1" % "test"
    ),
    assemblyJarName in assembly := "emoi-server.jar"
  )
