name := """pdf-watermark-service"""
organization := "org.dic"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.13"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test

libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.27"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.dic.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.dic.binders._"
