
name := "emoi-server"

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.13.1",
  organization := "tech.ignission",
  test in assembly := {}
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    assemblyJarName in assembly := "emoi-server.jar"
  )
