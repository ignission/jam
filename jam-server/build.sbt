name := "jam-server"

lazy val commonSettings = Seq(
  version := "0.2.0-SNAPSHOT",
  scalaVersion := "2.13.2",
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

lazy val openviduClient = (project in file("openvidu-client"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.spray"          %% "spray-json"     % "1.3.5",
      "org.typelevel"     %% "cats-core"      % catsVersion,
      "io.monix"          %% "monix"          % monixVersion,
      "io.monix"          %% "monix-eval"     % monixVersion,
      "io.monix"          %% "monix-reactive" % monixVersion,
      "com.typesafe.akka" %% "akka-http"      % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"    % akkaVersion,
      "org.slf4j"          % "slf4j-api"      % "1.7.30",
      "org.scalatest"     %% "scalatest"      % "3.1.1" % "test"
    )
  )

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
      "mysql"                    % "mysql-connector-java" % "8.0.17",
      "io.getquill"             %% "quill-jdbc"           % "3.5.1",
      "com.typesafe"             % "config"               % "1.4.0",
      "ch.megard"               %% "akka-http-cors"       % "0.4.3",
      "org.scalatest"           %% "scalatest"            % "3.1.1"         % "test",
      "com.typesafe.akka"       %% "akka-stream-testkit"  % akkaVersion     % "test",
      "com.typesafe.akka"       %% "akka-http-testkit"    % akkaHttpVersion % "test"
    ),
    assemblyJarName in assembly := "jam-server.jar"
  )
  .dependsOn(openviduClient)
