# `xcite`

## What it is

`xcite` is a cross-platform library for semantic manipulation of scholarly references expressed in URN notation.

## Current version: 1.2

[Release notes](releases.md)


## License

[GPL 3.0](http://www.opensource.org/licenses/gpl-3.0.html)

## Using, building, testing

`xcite` is compiled for both the JVM and ScalaJS using scala versions 2.10, 2.11 and 2.12.  Binaries for all platforms are available from jcenter.  If you are using sbt, include `Resolver.jcenterRepo` in your build file, and add this to your library dependencies:


    "edu.holycross.shot.cite" %% "xcite" % "1.2.0"

For maven, ivy or gradle equivalents, refer to <https://bintray.com/neelsmith/maven/xcite>.

To build from source and test, use normal sbt commands (`compile`, `test` ...).
