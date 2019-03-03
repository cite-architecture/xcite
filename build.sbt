
lazy val supportedScalaVersions = List("2.10.6", "2.11.8", "2.12.4")

lazy val root = project.in(file(".")).
    aggregate(crossedJVM, crossedJS).
    settings(
      crossScalaVersions := Nil,
      publish / skip := true
    )

lazy val crossed = crossProject.in(file(".")).
    settings(
      name := "xcite",
      organization := "edu.holycross.shot.cite",
      version := "4.0.0",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),
      libraryDependencies ++= Seq(
        "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
        "org.scalatest" %%% "scalatest" % "3.0.5" % "test"
      )
    ).
    jvmSettings(
      tutTargetDirectory := file("docs"),
      tutSourceDirectory := file("tut"),
      crossScalaVersions := supportedScalaVersions
    ).
    jsSettings(
      skip in packageJSDependencies := false,
      scalaJSUseMainModuleInitializer in Compile := true,
      crossScalaVersions := supportedScalaVersions      
    )

lazy val crossedJVM = crossed.jvm.enablePlugins(TutPlugin)
lazy val crossedJS = crossed.js.enablePlugins(ScalaJSPlugin)
