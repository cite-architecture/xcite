

## Previous README

The `edu.holycross.shot.cite` package is a library written in Scala for working with citable objects.

## Why ?

It duplicates much of the functionality of the existing jvm library `edu.harvard.chs.cite` package:  why bother?  Here are a few reasons.

**More idiomatic syntax when used in Scala code**.  A few examples: constructor without `new`:

    val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

function or property values accessible with simple member notation:

    urn.namespace == "greekLit" // expression is true


**Type-safe data handling**.  E.g., since `CtsUrn` and `CiteUrn` are case classes  extending the `Urn` superclass, you can pattern match on them:

    def idString (urn: Urn) = {
      urn match {
        case u: CtsUrn => "It's a CTS URN! " + u
        case _ => "Beats me."
      }
    }


**New features**.  For example, the `Citable` trait:  guarantee that any class you write extending this trait can be identified by a URN value -- potentially, a CitableGraph, an Orca object, or any object model you want to be citable (a Codex?).

**Cross-compilation for use in javascript VMs**.  Unlike the JVM library, this scala source could be compiled either for the JVM or (using scalajs) to javascript.  (NB: I have not yet added scalajs compilation to `build.sbt`.)

## Notes on the current state of the library

Version 3.x of the library includes very full implementations with extensive unit tests of the `CtsUrn` class and the newly defined `Cite2Urn` class.

It uses `sbt` for its build system, and `scalatest` for unit testing.  Run all tests with `sbt test`; build scaladocs with `sbt doc`.


## Demo scripts

Two included scripts, `CtsUrnDemo.sc` and `CtsUrnOpsDemo.sc`, provide an overview of the library.  Start a console session with `sbt console`, and load the scripts in your REPL with `:load CtsUrnDemo.sc` and `:load CtsUrnOpsDemo.sc`. Try these two functions;


- `urn` takes a string value for a URN and runs through a thorough analysis of it
- `ops` takes either one or two URNs as string values, and manipulates them; if two URNs are given, it also compares them
