name := "iex-cloud-client"

version := "0.1"

scalaVersion := "2.13.1"

val akkaV = "10.1.10"
val akkaStreamV = "2.5.26"
val circeV = "0.12.3"
val circeGenericExtrasV = "0.12.2"
val akkaHttpCirceV = "1.29.1"
val scalaTestV = "3.0.8"
val configV = "1.4.0"
val logbackV = "1.2.3"
val scalaLoggingV = "3.9.2"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % configV,

  "ch.qos.logback" % "logback-classic" % logbackV,

  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV,

  "com.typesafe.akka" %% "akka-http" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaStreamV,

  "io.circe" %% "circe-core" % circeV,
  "io.circe" %% "circe-generic" % circeV,
  "io.circe" %% "circe-parser" % circeV,
  "io.circe" %% "circe-generic-extras" % circeGenericExtrasV,
  "de.heikoseeberger" % "akka-http-circe_2.13" % "1.29.1",

  "org.scalactic" %% "scalactic" % scalaTestV,
  "org.scalatest" %% "scalatest" % scalaTestV % "test"
)
