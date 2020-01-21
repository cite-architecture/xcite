# `xcite`

## What it is

`xcite` is a cross-platform library for semantic manipulation of scholarly references expressed in URN notation.

## Current version: 4.2.1

Status:  **active development**. [Release notes](releases.md)

[API Docs](https://cite-architecture.github.io/cite-api-docs/xcite/api/edu/holycross/shot/cite/index.html)

## License

[GPL 3.0](http://www.opensource.org/licenses/gpl-3.0.html)

## Documentation

See <https://cite-architecture.github.io/xcite/>.

## Using, building, testing

`xcite` can be compiled for both the JVM and ScalaJS using scala versions 2.10, 2.11 or 2.12.  Binaries for all three versions are available from jcenter.

If you are using sbt, include `Resolver.jcenterRepo` in your list of resolvers

    resolvers += Resolver.jcenterRepo

and add this to your library dependencies:

    "edu.holycross.shot.cite" %%% "xcite" % VERSION


For maven, ivy or gradle equivalents, refer to <https://bintray.com/neelsmith/maven/xcite>.

To build from source and test, use normal sbt commands (`compile`, `test` ...).

## Using as a Javascript Library

In SBT, `fullOptJS` will build a JS library, and save it as `js/target/scala-2.12/xcite-opt.js`.

Include this in an HTML file as you would any other JS library:

~~~html
<script type="text/javascript" src="xcite-opt.js"></script>
~~~

**The trick** with creating URN objects is the JavaScript syntax:

~~~javascript
var citeU = new edu.holycross.shot.cite.Cite2Urn( { urnString: "urn:cite2:hmt:msA.v1:1r2" });
var ctsU = new edu.holycross.shot.cite.CtsUrn( { urnString: "urn:cts:greekLit:tlg0012.tlg001.msA:1.1" });
~~~

After that, the [API Docs](https://cite-architecture.github.io/cite-api-docs/xcite/api/edu/holycross/shot/cite/index.html), and working in a console, should be the best guide.
