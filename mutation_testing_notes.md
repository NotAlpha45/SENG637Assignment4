

# SENG 637 - Dependability and Reliability of Software Systems

# Mutation Testing Log (PIT, Range)

This section tracks mutation-testing progress for the `Range` class using PIT with the `pit-range` Maven profile.

## Run Summary

| Stage | Mutations Killed | Total Mutations | Mutation Score | Test Strength | No Coverage |
| ----- | ---------------- | --------------- | -------------- | ------------- | ----------- |
| Before targeted tests | 119 | 135 | 88% | 89% | 1 |
| After targeted tests | 131 | 135 | 97% | 98% | 1 |

## Targeted Surviving Mutants and Actions

| Mutated Location | Mutation | Why It Survived | Detection Strategy Added | Need Range.java Change? |
| ---------------- | -------- | --------------- | ------------------------ | ----------------------- |
| `intersects(double,double)` | changed conditional boundary | Missing exact-equality edge cases (`b0 == lower`, `b1 == b0`) | Added tests for single-point and lower-edge touch behavior | No |
| `expandToInclude(Range,double)` | changed conditional boundary | Existing tests checked value inside/outside bounds, but not exact lower/upper identity behavior | Added `assertSame` tests for `value == lower` and `value == upper` | No |
| `shiftWithNoZeroCrossing` (via `shift`) | changed conditional boundary | Existing zero test only used positive delta, so boundary mutation around `value == 0` with negative delta survived | Added test for lower bound `0.0` with negative delta and `allowZeroCrossing=false` | No |
| `hashCode()` | multiple arithmetic/bit-operation replacements survived | Existing tests only checked consistency/equal objects, not exact hash formula | Added oracle test computing expected hash from `doubleToLongBits` and equality checks | No |

## Remaining Unresolved Mutants

| Mutated Location | PIT Status | Interpretation | Suggested Action |
| ---------------- | ---------- | -------------- | ---------------- |
| `contains(double)` final boolean return replacement | `NO_COVERAGE` | Likely equivalent/reporting artifact because final expression is logically true when reached after guard checks | Keep as known residual; no source change required |
| `constrain(double)` boundary changes (`>`/`<` to inclusive) | `SURVIVED` (2) | Likely equivalent under outer `!contains(value)` guard; boundary-equality cases do not alter observable return value | Document as likely equivalent mutant; no production change needed |
| `expand(Range,double,double)` boundary change (`lower > upper`) | `SURVIVED` (1) | Likely equivalent for `lower == upper` case since both branches return same numeric bounds | Document as likely equivalent mutant; no production change needed |

## Decision on Production Code

No changes were made to `Range.java`. The score increase came from stronger tests in `RangeTest.java`, and the remaining survivors appear to be equivalent or non-actionable mutants rather than confirmed functional defects.





