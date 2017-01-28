name := "Cross-compiled CITE library"

scalaVersion in ThisBuild := "2.12.1"

crossScalaVersions := Seq("2.11.8", "2.12.1")





lazy val root = project.in(file(".")).
  aggregate(xciteJS, xciteJVM).
  settings(
    licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),
    publish := {    },
    publishLocal := {}
  )

lazy val xcite = crossProject.in(file(".")).
  settings(
    name := "xcite",
    organization := "edu.holycross.shot.cite",
    version := "1.0.0",
    licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))
  ).
  jvmSettings(
    // Add JVM-specific settings here
  ).
  jsSettings(
    // Add JS-specific settings here
  )

lazy val xciteJVM = xcite.jvm
lazy val xciteJS = xcite.js
