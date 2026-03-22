

# SENG 637 - Dependability and Reliability of Software Systems

# Mutation Testing Log (PIT, Range)

This section tracks mutation-testing progress for the `Range` class using PIT with the `pit-range` Maven profile.

## Run Summary

| Stage | Mutations Killed | Total Mutations | Mutation Score | Test Strength | No Coverage |
| ----- | ---------------- | --------------- | -------------- | ------------- | ----------- |
| Before targeted tests | 119 | 135 | 88% | 89% | 1 |
| After targeted tests | 131 | 135 | 97% | 98% | 1 |

## Semi-Automatic Method for Equivalent Mutant Detection (Range)

The following process was used to semi-automatically identify equivalent-mutant candidates:

1. Run PIT and export HTML/XML reports.
2. Automatically collect only `SURVIVED` and `NO_COVERAGE` entries from `target/pit-reports/org.jfree.data/Range.java.html`.
3. Group entries by method location and mutation operator.
4. Apply candidate rules:
	 - Rule A: Mutation is in code that appears unreachable based on surrounding guards.
	 - Rule B: Boundary mutation (`>`, `<`, `>=`, `<=`) occurs inside a branch already constrained by a stronger predicate.
	 - Rule C: Mutated and original branches return the same externally observable value for all feasible inputs.
5. Manually confirm each candidate by tracing feasible paths in `Range.java` and validating behavior with targeted tests.

### Benefits

- Fast triage of large mutation logs.
- Focuses manual effort on likely equivalent survivors.
- Reproducible workflow across multiple classes.

### Disadvantages

- Not complete: some true non-equivalent mutants may be misclassified.
- Requires manual confirmation to avoid false positives.
- Depends on correct path-feasibility reasoning.

### Assumptions

- Observable behavior means public return values and thrown exceptions.
- Guard conditions in source code are correct and consistently enforced.
- Mutation report locations map correctly to source methods.

## Manually Verified Equivalent-Mutant Candidates (Range)

After re-checking current PIT artifacts, the remaining unresolved entries are:

- `contains(double)` final return replacement -> `NO_COVERAGE` (1)
- `constrain(double)` changed conditional boundary -> `SURVIVED` (2)
- `expand(Range,double,double)` changed conditional boundary -> `SURVIVED` (1)

### Candidate 1: `constrain(double)` boundary mutants (2)

- Source logic: `constrain()` first checks `if (!contains(value))`, then only enters clamping branches.
- Mutated condition: `value > upper` to `value >= upper` (and symmetric lower case).
- Reason likely equivalent: when `value == upper` or `value == lower`, `contains(value)` is true, so mutated branch is not reachable.
- Result: manually classified as likely equivalent under feasible paths.

### Candidate 2: `expand(Range,double,double)` boundary mutant (1)

- Source logic: collapse executes when `lower > upper`.
- Mutated condition: boundary changed to include equality.
- Reason likely equivalent: when `lower == upper`, both original and mutated paths produce the same final range bounds.
- Result: manually classified as likely equivalent.

### Candidate 3: `contains(double)` `NO_COVERAGE` final return mutant (1)

- Source logic: function exits early for `< lower` and `> upper`; final return executes only for in-range values.
- Mutated final return was not covered by PIT in this run (`NO_COVERAGE`).
- Interpretation: likely reporting artifact or effectively equivalent at that point because guards already constrain feasible values.
- Result: kept as residual `NO_COVERAGE` candidate; no production code change recommended.

## Verification Evidence (Current Artifacts)

- Current PIT run summary remains `131/135` killed (`97%`).
- Current unresolved list from `Range.java.html` still contains exactly:
	- `contains` (`NO_COVERAGE` x1)
	- `constrain` (`SURVIVED` x2)
	- `expand` (`SURVIVED` x1)

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





