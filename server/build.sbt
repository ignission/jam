
name := "emoi-server"

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.13.1",
  organization := "tech.ignission"
)

lazy val root = (project in file("."))
  .settings(commonSettings)

lazy val test = (project in file("test"))
  .settings(commonSettings)
  .dependsOn(root)