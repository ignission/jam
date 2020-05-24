name := "openvidu4s"

val catsVersion     = "2.1.1"
val monixVersion    = "3.2.0"
val akkaHttpVersion = "10.1.12"
val akkaVersion     = "2.6.4"

libraryDependencies ++= Seq(
  "io.spray"          %% "spray-json"     % "1.3.5",
  "org.typelevel"     %% "cats-core"      % catsVersion,
  "io.monix"          %% "monix"          % monixVersion,
  "io.monix"          %% "monix-eval"     % monixVersion,
  "io.monix"          %% "monix-reactive" % monixVersion,
  "com.typesafe.akka" %% "akka-http"      % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"    % akkaVersion,
  "org.slf4j"          % "slf4j-api"      % "1.7.30",
  "org.scalatest"     %% "scalatest"      % "3.1.2" % "test"
)
