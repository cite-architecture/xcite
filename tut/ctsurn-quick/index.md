---
layout: page
title: "CTS URNs: quick start"
---



## Creating URNs

Create `CtsUrn`s directly from strings:

```tut:invisible
import edu.holycross.shot.cite._
```


```tut:silent

val venetusA_1_1 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

```

## Manipulating URNs

The library includes functions for getting access to all parts of the URN, such as the top-level components:

```tut
venetusA_1_1.workComponent
venetusA_1_1.passageComponent
```

or individual parts of each component:

```tut
venetusA_1_1.textGroup
```

(See the [API docs](http://cite-architecture.org/libs/api-docs/xcite/edu/holycross/shot/cite/index.html) for more information.)


## Creating derivative URNs

`CtsUrn`s are immutable objects:  you cannot modify them, but you can use them to create new `CtsUrn`s.  You can drop particular parts of the URN:

```tut
venetusA_1_1.dropPassage
```

You can add particular parts (substituting for the value of that part, if any, in the source URN)

```tut
venetusA_1_1.addVersion("msB")
```



## Comparing URNs


One of the most powerful features of the `CtsUrn` class is  comparison of two URNs following the semantics of the URN notation.

```tut:silent
val venetusA_1_2 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
val iliad_1_1_notional = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
val iliad_bk1_notional = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1")

```

### URN equality

The equality operator `==` tests whether the URNs have identical values:

```tut
venetusA_1_1 == venetusA_1_2
```


### URN containment
The containment operator '>' tests whether one URN fully contains the other ('>=' tests "contains or is equal to").

```tut
iliad_1_1_notional > venetusA_1_1
venetusA_1_1 > iliad_1_1_notional
```


### URN similarity

The URN similarity operator `~~` tests whether either URN's work hierarchy contains the other's, *and* whether either URN's passage hierarchy contains the other's.

```tut
iliad_bk1_notional ~~ iliad_1_1_notional
iliad_bk1_notional ~~ venetusA_1_1
venetusA_1_1 ~~ venetusA_1_2
```
