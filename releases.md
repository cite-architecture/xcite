# xcite: release notes


**2.2.0**: adds `dropProperty` function

**2.1.0**: adds `dropSelector` function

**2.0.1**: fixes a bug in matching `Cite2Urn`s with no object identifier.

**2.0**: remove `urnMatch` function name in favor of `~~` throughout.

**1.4**: adds `collapseTo` and `collapseBy` function on CtsUrns.

**1.3**: adds support for property-level references in CITE2URNs.

**1.2**: introduces the twiddle operator `~~` for URN matching; addresses numerous issues testing exotically malformed URN strings.

**1.1**: correctly exports the `CtsUrn` and `Cite2Urn` classes for use in javascript applications compiled from ScalaJS.

**1.0**:  initial release implementing the code library from <https://github.com/cite-architecture/cite_scala> in a cross-compiling build.
