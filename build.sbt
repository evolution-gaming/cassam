name := "cassam"

organization := "com.evolutiongaming"

homepage := Some(new URL("http://github.com/evolution-gaming/cassam"))

organizationName := "Evolution Gaming"

organizationHomepage := Some(url("http://evolutiongaming.com"))

bintrayOrganization := Some("evolutiongaming")

scalaVersion := "2.12.3"

crossScalaVersions := Seq("2.12.3", "2.13.2")

externalResolvers ++= Seq(
  Resolver.url(
    "Evolution Gaming (ivy)",
    url("http://rms.evolutiongaming.com/pub-ivy/")
  )(Resolver.ivyStylePatterns),
  "Evolution Gaming repository" at "http://rms.evolutiongaming.com/public/"
)

val crossVersionScalacOptions =
  scalacOptions --=
    (scalaVersion.value match {
      case "2.13.2" =>
        Seq(
          "-Ywarn-adapted-args",
          "-Ywarn-unused-import",
          "-Ypartial-unification",
          "-Yno-adapted-args",
          "-Xfatal-warnings",
          "-Xdeprecation"
        )
      case _ => Seq.empty
    })

lazy val root = (project in file("."))
  .settings(
    libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.3.0",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    libraryDependencies += "com.evolutiongaming" %% "pillar" % "5.0.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test",
    libraryDependencies += "org.scalamock" %% "scalamock" % "4.4.0" % "test",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Xfuture"
    ),
    crossVersionScalacOptions
  )

licenses := Seq(
  ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
)

releaseCrossBuild := true

bintrayRepository := "maven"
