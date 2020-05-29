name := "jam-server"

addCommandAlias("fix", "all compile:scalafix; test:scalafix")
addCommandAlias("fixCheck", "; compile:scalafix --check; test:scalafix --check")
addCommandAlias("format", "; scalafmt; test:scalafmt; scalafmtSbt")
addCommandAlias("formatCheck", "; scalafmtCheck; test:scalafmtCheck; scalafmtSbtCheck")

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.3.1-RC1"

lazy val commonSettings = Seq(
  version := "0.2.0-SNAPSHOT",
  scalaVersion := "2.13.2",
  organization := "tech.ignission",
  test in assembly := {},
  scalacOptions ++= List(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Yrangepos",
    "-Ywarn-unused",
    "-Xlint",
    "-Xfatal-warnings",
    "-Ymacro-annotations"
  ),
  libraryDependencies ++= {
    val catsVersion  = "2.1.1"
    val monixVersion = "3.2.0"
    Seq(
      "org.typelevel" %% "cats-core" % catsVersion,
      "io.monix"      %% "monix"     % monixVersion
    )
  },
  // scalafix
  semanticdbEnabled := true,
  semanticdbVersion := "4.3.10",
  addCompilerPlugin(scalafixSemanticdb),
  scalacOptions in Test := { // https://github.com/scalacenter/scalafix/pull/1116
    val initial           = (scalacOptions in Test).value
    val semanticDbOptions = initial.filter(_.contains("-P:semanticdb:"))
    val semanticDbLess    = initial.filterNot(_.contains("-P:semanticdb:"))
    semanticDbLess ++ semanticDbOptions.lastOption.toSeq
  }
)

lazy val openvidu4s = (project in file("openvidu4s")).settings(commonSettings)

lazy val domain = (project in file("jam-domain")).settings(commonSettings)

lazy val infra = (project in file("jam-infrastructure"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= {
      val quillVersion = "3.5.1"
      Seq(
        "mysql"        % "mysql-connector-java" % "8.0.17",
        "io.getquill" %% "quill-jdbc-monix"     % quillVersion
      )
    }
  )
  .dependsOn(domain)

lazy val server = (project in file("jam-server"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= {
      val akkaHttpVersion = "10.1.12"
      val akkaVersion     = "2.6.4"
      val log4j2Version   = "2.13.2"
      Seq(
        "com.typesafe.akka"                  %% "akka-http"            % akkaHttpVersion,
        "com.typesafe.akka"                  %% "akka-http-spray-json" % akkaHttpVersion,
        "com.typesafe.akka"                  %% "akka-stream"          % akkaVersion,
        "com.typesafe.akka"                  %% "akka-actor"           % akkaVersion,
        "org.apache.logging.log4j"            % "log4j-core"           % log4j2Version,
        "org.apache.logging.log4j"            % "log4j-api"            % log4j2Version,
        "org.apache.logging.log4j"            % "log4j-slf4j-impl"     % log4j2Version,
        "com.typesafe"                        % "config"               % "1.4.0",
        "ch.megard"                          %% "akka-http-cors"       % "0.4.3",
        "com.softwaremill.akka-http-session" %% "core"                 % "0.5.11",
        "org.springframework.security"        % "spring-security-web"  % "5.3.2.RELEASE",
        // test
        "org.scalatest"     %% "scalatest"           % "3.1.2"         % "test",
        "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion     % "test",
        "com.typesafe.akka" %% "akka-http-testkit"   % akkaHttpVersion % "test"
      )
    },
    // sbt assembly
    assemblyJarName in assembly := "jam-server.jar",
    // database migration
    flywayUrl := "jdbc:mysql://localhost:33055/jam",
    flywayUser := "jam",
    flywayPassword := "jam",
    flywayLocations += "db/migration",
    flywayUrl in Test := "jdbc:mysql://localhost:33056/jam",
    flywayUser in Test := "jam",
    flywayPassword in Test := "jam"
  )
  .enablePlugins(FlywayPlugin)
  .dependsOn(domain, infra, openvidu4s)
