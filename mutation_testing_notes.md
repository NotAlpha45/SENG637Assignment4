

# SENG 637 - Dependability and Reliability of Software Systems

# Mutation Testing Log (PIT, Range)

This section tracks mutation-testing progress for the `Range` class using PIT with the `pit-range` Maven profile.

## Run Summary

| Campaign | Stage | Mutations Killed | Total Mutations | Mutation Score | Test Strength | No Coverage |
| -------- | ----- | ---------------- | --------------- | -------------- | ------------- | ----------- |
| PIT 1.6.8 + `ALL` mutators (report rerun) | Baseline (mutation-focused tests disabled) | 915 | 1239 | 74% | 74% | 0 |
| PIT 1.6.8 + `ALL` mutators (report rerun) | Improved (mutation-focused tests restored) | 996 | 1239 | 80% | 80% | 0 |
| PIT 1.6.8 + `ALL` mutators (after additional edge/oracle tests) | Latest improved | 1053 | 1239 | 85% | 85% | 0 |

## Controlled Re-Run Used for Report (PIT 1.6.8 + ALL)

To align with the report comparison requirement, we executed a controlled before/after run using the same command and environment:

- Command: `mvn -Ppit-range test-compile org.pitest:pitest-maven:mutationCoverage -Dmutators=ALL`
- Baseline run: temporarily disabled the mutation-targeted tests added late in `RangeTest.java`.
- Improved run: re-enabled those tests and reran the same command.

Results used in report:

- Baseline: `915/1239 = 74%`
- Improved: `996/1239 = 80%`
- Absolute gain: `6` percentage points
- Relative gain: `(80 - 74) / 74 = 8.11%`

Latest rerun after adding additional edge/oracle tests:

- Latest improved: `1053/1239 = 85%`
- Absolute gain vs baseline: `85% - 74% = 11` percentage points
- Relative gain vs baseline: `11 / 74 = 14.86%`

## Environment and Tooling Discrepancy Notes

### 1) PIT version discrepancy

Earlier results were produced with a newer PIT line and/or default mutator set, while report-aligned reruns used PIT `1.6.8` with `-Dmutators=ALL`.
Because PIT versions and mutator sets change both the number and type of generated mutants, raw totals are not directly comparable across those settings.

### 2) Why Eclipse can still differ even with same source

Even when source files are identical, Eclipse runs can still produce different mutant totals than Maven/VS Code runs because PIT mutates compiled bytecode, and bytecode can differ by toolchain and build path.

Common causes:

- Compiler differences (`ecj` in Eclipse vs `javac` in Maven)
- Different compiler flags/target settings and incremental vs clean compilation
- Different classpaths/test discovery in IDE launch vs Maven profile execution
- Different stale/clean artifact states when mutation starts

Conclusion:

- For fair comparison, keep all of the following identical: PIT version, mutator set, compiler path, Java version, test selection, and clean-build process.

## Scope Note for Remaining Sections

The sections below document the earlier default-mutator campaign (total mutants around 135), which was used for targeted survivor analysis and equivalent-mutant discussion.
They are intentionally kept for methodology and reasoning, but they should not be numerically compared with the report rerun metrics above (`ALL` mutators, total mutants 1239).

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

In the historical default-mutator artifacts, the remaining unresolved entries were:

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

## Verification Evidence (Historical Default-Mutator Artifacts)

- Historical default-mutator run summary: `131/135` killed (`97%`).
- Historical unresolved list from `Range.java.html` contained exactly:
	- `contains` (`NO_COVERAGE` x1)
	- `constrain` (`SURVIVED` x2)
	- `expand` (`SURVIVED` x1)

## Analysis of Representative PIT Mutants (Range)

The table below analyzes 13 representative mutants (more than the required 10) and compares behavior under the original and updated `RangeTest` suite.

| # | Method | Mutant | Original Suite | Updated Suite | Evidence / Notes |
| - | ------ | ------ | -------------- | ------------- | ---------------- |
| 1 | `intersects(double,double)` | changed conditional boundary (branch around `b0 <= lower`) | SURVIVED | KILLED | Killed by added boundary test for `b0 == lower` touch case |
| 2 | `intersects(double,double)` | changed conditional boundary (branch around `b1 >= b0`) | SURVIVED | KILLED | Killed by added single-point interval test (`b0 == b1`) |
| 3 | `expandToInclude()` | changed conditional boundary (lower-side check) | SURVIVED | KILLED | Killed by `value == lower` identity test (`assertSame`) |
| 4 | `expandToInclude()` | changed conditional boundary (upper-side check) | SURVIVED | KILLED | Killed by `value == upper` identity test (`assertSame`) |
| 5 | `shiftWithNoZeroCrossing()` | changed conditional boundary (`value > 0.0`) | SURVIVED | KILLED | Killed by zero-lower + negative-delta case |
| 6 | `hashCode()` | unsigned shift right replaced with shift left | SURVIVED | KILLED | Killed by fixed-oracle hash test |
| 7 | `hashCode()` | XOR replaced with AND | SURVIVED | KILLED | Killed by fixed-oracle hash test |
| 8 | `hashCode()` | integer addition replaced with subtraction | SURVIVED | KILLED | Killed by fixed-oracle hash test |
| 9 | `hashCode()` | integer multiplication replaced with division | SURVIVED | KILLED | Killed by fixed-oracle hash test |
| 10 | `hashCode()` | return replaced with `0` | SURVIVED | KILLED | Killed by fixed-oracle hash test |
| 11 | `constrain(double)` | changed conditional boundary (`value > upper` to inclusive) | SURVIVED | SURVIVED | Likely equivalent due outer `!contains(value)` guard |
| 12 | `expand()` | changed conditional boundary (`lower > upper` to inclusive) | SURVIVED | SURVIVED | Likely equivalent for `lower == upper` scenario |
| 13 | `contains(double)` | final return replaced with `true` | NO_COVERAGE | NO_COVERAGE | Residual, likely artifact/equivalent-at-point due prior guards |

## Statistics and Mutation Score by Test Suite (Historical Default-Mutator Campaign)

| Range Class + Suite | Killed | Survived | No Coverage | Total | Mutation Score | Test Strength |
| ------------------- | ------ | -------- | ----------- | ----- | -------------- | ------------- |
| Original `RangeTest` | 119 | 15 | 1 | 135 | 88% | 89% |
| Updated `RangeTest` | 131 | 3 | 1 | 135 | 97% | 98% |

Notes:

- Absolute mutation score improvement: `97% - 88% = 9` percentage points.
- Relative improvement: `9 / 88 = 10.23%`.
- Survived mutants reduced from `15` to `3`.

## Statistics and Mutation Score by Test Suite (Report Rerun, PIT 1.6.8 + ALL)

| Range Class + Suite | Killed | Survived | No Coverage | Total | Mutation Score | Test Strength |
| ------------------- | ------ | -------- | ----------- | ----- | -------------- | ------------- |
| Baseline (mutation-focused tests disabled) | 915 | 324 | 0 | 1239 | 74% | 74% |
| Improved (mutation-focused tests restored) | 996 | 243 | 0 | 1239 | 80% | 80% |
| Latest improved (after additional edge/oracle tests) | 1053 | 186 | 0 | 1239 | 85% | 85% |

Notes:

- Absolute mutation score improvement: `80% - 74% = 6` percentage points.
- Relative improvement: `6 / 74 = 8.11%`.
- Survived mutants reduced from `324` to `243`.
- Latest absolute mutation score improvement: `85% - 74% = 11` percentage points.
- Latest relative improvement: `11 / 74 = 14.86%`.
- Latest survived mutants reduced from `324` to `186`.

## Why Further Increase Might Not Be Feasible (ALL Mutators)

Under PIT `1.6.8` with `ALL` mutators enabled, the current score is `1053/1239 = 85%`.

To reach at least `90%`, the suite would need:

- Minimum kills required for `90%`: `ceil(0.90 x 1239) = 1116`
- Additional kills still needed: `1116 - 1053 = 63`

Given repeated targeted expansions (boundary tests, signed-zero tests, NaN/infinity tests, oracle sweeps, and reflection-based checks for private helpers), the remaining survivors are increasingly dominated by aggressive unary/comparator mutations (especially UOI-family variants) that are often behavior-preserving or extremely hard to distinguish via black-box assertions.

Practical implications:

- Marginal returns are now low: each additional test round yields small gains relative to effort.
- Many residual survivors likely fall into equivalent or near-equivalent categories under floating-point and guard-dominance semantics.
- Reaching raw `90%` with `ALL` mutators may require either:
	- production-code changes purely to expose mutation-sensitive observables (not recommended for this assignment objective), or
	- exclusion/adjustment for justified equivalent mutants.

Therefore, `85%` is treated as the current practical ceiling for the unchanged `Range.java` implementation under this specific configuration (PIT `1.6.8` + `ALL`).

## Effect of Equivalent Mutants on Mutation Score Accuracy

Equivalent mutants can make mutation score look lower than the true defect-detection strength of a test suite, because they are counted as not killed even when no test can distinguish them from the original behavior.

For `Range`, the likely equivalent candidates are concentrated in boundary mutations guarded by stronger predicates (`constrain`) and algebraically neutral boundary behavior (`expand`). This creates a conservative bias in raw mutation score.

Impact in the historical default-mutator campaign:

- Raw final score: `131/135 = 97%`.
- If the 3 likely equivalent survivors are excluded, adjusted score is effectively `131/132 = 99.24%`.

Equivalent-mutant detection approach used:

- Semi-automatic triage from PIT (`SURVIVED` + `NO_COVERAGE`).
- Rule-based candidate filtering (guard-dominance, boundary-only mutations, same-observable-output patterns).
- Manual source-path confirmation on each candidate.

## How Mutation Score Was Improved (Design Strategy)

The score improvement strategy was targeted and mutation-driven, not random test expansion.

1. Run PIT on original suite and isolate surviving/no-coverage mutants.
2. Group survivors by method and mutation operator.
3. Prioritize high-yield groups (multiple survivors in same logic region):
	- boundary decisions in `intersects` and `expandToInclude`
	- zero-crossing boundary logic in `shiftWithNoZeroCrossing`
	- arithmetic/bit-level logic in `hashCode`
4. Add minimal, high-discrimination tests:
	- exact-boundary equality cases
	- identity vs new-instance assertions (`assertSame`)
	- signed-zero edge behavior to detect subtle boundary changes
	- deterministic oracle for hash computation
5. Re-run PIT and iterate until non-equivalent survivors were mostly eliminated.

Result:

- Mutation score improved from `88%` to `97%`.
- The remaining unresolved mutants are documented and justified as likely equivalent/non-actionable.

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





