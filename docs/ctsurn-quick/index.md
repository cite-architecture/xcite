---
layout: page
title: "CTS URNs: quick start"
---



## Creating URNs

Create `CtsUrn`s directly from strings:





```scala

val venetusA_1_1 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

```

## Manipulating URNs

The library includes functions for getting access to all parts of the URN, such as the top-level components:

```scala
scala> venetusA_1_1.workComponent
res0: String = tlg0012.tlg001.msA

scala> venetusA_1_1.passageComponent
res1: String = 1.1
```

or individual parts of each component:

```scala
scala> venetusA_1_1.textGroup
res2: String = tlg0012
```

(See the [API docs](http://cite-architecture.org/libs/api-docs/xcite/edu/holycross/shot/cite/index.html) for more information.)


## Creating derivative URNs

`CtsUrn`s are immutable objects:  you cannot modify them, but you can use them to create new `CtsUrn`s.  You can drop particular parts of the URN:

```scala
scala> venetusA_1_1.dropPassage
res3: edu.holycross.shot.cite.CtsUrn = urn:cts:greekLit:tlg0012.tlg001.msA:
```

You can add particular parts (substituting for the value of that part, if any, in the source URN)

```scala
scala> venetusA_1_1.addVersion("msB")
res4: edu.holycross.shot.cite.CtsUrn = urn:cts:greekLit:tlg0012.tlg001.msB:1.1
```



## Comparing URNs


One of the most powerful features of the `CtsUrn` class is  comparison of two URNs following the semantics of the URN notation.

```scala
val venetusA_1_2 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
val iliad_1_1_notional = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
val iliad_bk1_notional = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1")

```

### URN equality

The equality operator `==` tests whether the URNs have identical values:

```scala
scala> venetusA_1_1 == venetusA_1_2
res5: Boolean = false
```


### URN containment
The containment operator '>' tests whether one URN fully contains the other ('>=' tests "contains or is equal to").

```scala
scala> iliad_1_1_notional > venetusA_1_1
res6: Boolean = true

scala> venetusA_1_1 > iliad_1_1_notional
res7: Boolean = false
```


### URN similarity

The URN similarity operator `~~` tests whether either URN's work hierarchy contains the other's, *and* whether either URN's passage hierarchy contains the other's.

```scala
scala> iliad_bk1_notional ~~ iliad_1_1_notional
res8: Boolean = true

scala> iliad_bk1_notional ~~ venetusA_1_1
res9: Boolean = true

scala> venetusA_1_1 ~~ venetusA_1_2
res10: Boolean = false
```
