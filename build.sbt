
lazy val docs = project       // new documentation project
  .in(file("docs-build")) // important: it must not be docs/
  .dependsOn(crossed.jvm)
  .enablePlugins(MdocPlugin)
  .settings(
    mdocIn := file("guide"),
    mdocOut := file("docs"),
    mdocExtraArguments := Seq("--no-link-hygiene"),
    mdocVariables := Map(
     "VERSION" -> "1.4.1"
   )
  )