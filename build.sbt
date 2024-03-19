name := """pdf-watermark-service"""
organization := "org.dic"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.13"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.27"
libraryDependencies += "org.apache.jclouds" % "jclouds-allblobstore" % "2.2.1"

libraryDependencies ++= Seq(
  "javax.xml.bind" % "jaxb-api" % "2.3.1",
  "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.3"
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.dic.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.dic.binders._"
