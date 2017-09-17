# xcite: release notes

**3.2.1**  Fixes a bug in the URN containment function `>=`.


**3.2.0**: Add boolean function determining if a CTS URN is concrete or notional.

**3.1.0**: Added boolean functions on work level value for more convenient filtering of collections.

**3.0.1**: Nicer exception handling when total garbage given as input to create CtsUrn.

**3.0.0**: Technically a breaking change since it renames two functions primarily intended for internal use.  Adds new functions implementing algebra of CtsUrn containment and similarity.


**2.7.1**: improved error message when Cite2Urn syntax is wrong.

**2.7.0**: update ScalaJS depenencies, publish binaries for Scala 2.12.3.

**2.6.0**: add `addVersion` function on `CtsUrn`

**2.5.1**: fix error message to include offending value.

**2.5.0**: adds `dropVersion` function on `CtsUrn`.


**2.4.0**: adds `rangeBeginUrn` and `rangeBeginUrn` functions on `Cite2Urn`.

**2.3.2**: fixes a bug in adding property part to collection component of a Cite2Urn.

**2.3.1**: bug fixes.  Enforce requirement that CITE2 URN strings define five colon-separated components, even if object selector component is empty.  Correctly preserve collection component when extensions dropped from object selector.

**2.3.0**: new functions to add versions, properties or object selectors to `Cite2Urn`s.

**2.2.3**: properly protects periods in passage component when using regular expression for URN pattern matching.

**2.2.2**: fixes a bug in URN pattern matching.

**2.2.1**: fixes a couple of bugs with edge cases of bad input to Cite2Urn constructor.

**2.2.0**: adds `dropProperty` function

**2.1.0**: adds `dropSelector` function

**2.0.1**: fixes a bug in matching `Cite2Urn`s with no object identifier.

**2.0**: remove `urnMatch` function name in favor of `~~` throughout.

**1.4**: adds `collapseTo` and `collapseBy` function on CtsUrns.

**1.3**: adds support for property-level references in CITE2URNs.

**1.2**: introduces the twiddle operator `~~` for URN matching; addresses numerous issues testing exotically malformed URN strings.

**1.1**: correctly exports the `CtsUrn` and `Cite2Urn` classes for use in javascript applications compiled from ScalaJS.

**1.0**:  initial release implementing the code library from <https://github.com/cite-architecture/cite_scala> in a cross-compiling build.
