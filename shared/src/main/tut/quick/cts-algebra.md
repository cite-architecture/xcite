---
layout: page
title: CTS URN algebra  
---

For CTS URNs, the logic of URN comparison is determined by the comparison of the two independent hierarchical components identifying work and passage.  The comparison of each component involves comparing each hierarchical element in the component for the first URN to the corresponding component in the second URN.

## Containment (`>`, `>=`, `<` and `<=`)

For two CTS URNs, `u1` and `u2`:


1. `u1.passageComponent` *contains* `u2.passageComponent` IFF every element of `u2.passageComponent` has the same value as the corresponding  element of `u1.passageComponent`.
2. `u1.workComponent` *contains* `u2.workComponent` IFF every element of `u2.workComponent` has the same value as the corresponding  element of `u1.workComponent`.
3.  `u1 > u2` IFF `u1.passageComponent` *contains* `u2.passageComponent` AND `u1.workComponent` *contains* `u2.workComponent`.
4.  `u1 >= u2` if `u1.passageComponent` *contains* `u2.passageComponent` AND `u1.workComponent` *contains* `u2.workComponent` OR `u1` == `u2`
3.  `u1 < u2` IFF `u2.passageComponent` *contains* `u1.passageComponent` AND `u2.workComponent` *contains* `u1.workComponent`.
4.  `u1 <= u2` if `u2.passageComponent` *contains* `u1.passageComponent` AND `u2.workComponent` *contains* `u1.workComponent` OR `u1` == `u2`


## Similarity (the `~~` function)

`u1.workComponent ~~ u2.workComponent` if:
- `u1.workComponent` *contains*  `u2.workComponent` OR
- `u2.workComponent` *contains*  `u1.workComponent`


`u1.passageComponent ~~ u2.passageComponent` if:
- `u1.passageComponent` *contains*  `u2.passageComponent` OR
- `u2.passageComponent` *contains*  `u1.passageComponent`


`u1 ~~ u2` IFF `u1.passageComponent ~~ u2.passageComponent`  AND `u1.workComponent ~~ u2.workComponent`

Note that the similarity operation is commutative:  `(u1 ~~ u2) == (u2 ~~ u1)`.



## Exclusion (non-similarity, or non-containment) (`><`)

The exclusion boolean function `><` is the complement of the two containment functions:  it is true when both `>` and `<` are false.

The exclusion function is true when:

1.  at least one corresponding element of `u1.passageComponent` and `u2.passageComponent` has different values for `u1` and `u2` OR
2.  at least one corresponding element of `u1.workComponent` and `u2.workComponent` has different values for `u1` and `u2`



## Truth tables

**Containment relations** for two CTS URNs `u1` and `u2` may be summarized as follows:

| Passage component                           | Work component                        | `~~` | `><`  | `>`   | `<`   |
|:--------------------------------------------|:--------------------------------------|:-----|:------|:------|:------|
| `u1.passageComponent > u2.passageComponent` | `u1.workComponent > u2.workComponent` | true | false | true  | false |
| `u1.passageComponent > u2.passageComponent` | `u1.workComponent < u2.workComponent` | true | false | false | false |
| `u1.passageComponent < u2.passageComponent` | `u1.workComponent > u2.workComponent` | true | false | false | false |
| `u1.passageComponent < u2.passageComponent` | `u1.workComponent < u2.workComponent` | true | false | false | true  |


**Similarity relations** for two CTS URNs `u1` and `u2` may be summarized as follows:

| Passage component                            | Work component                         | `~~`  | `><`  |
|:---------------------------------------------|:---------------------------------------|:------|:------|
| `u1.passageComponent ~~ u2.passageComponent` | `u1.workComponent ~~ u2.workComponent` | true  | false |
| `u1.passageComponent ~~ u2.passageComponent` | `u1.workComponent >< u2.workComponent` | false | true  |
| `u1.passageComponent >< u2.passageComponent` | `u1.workComponent ~~ u2.workComponent` | false | true  |
| `u1.passageComponent >< u2.passageComponent` | `u1.workComponent >< u2.workComponent` | false | true  |
