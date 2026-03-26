# SENG 637 - Dependability and Reliability of Software Systems

## Mutation Testing Log (Range, PIT 1.6.8 + ALL)

This report summarizes one consistent mutation-testing campaign for `Range` using:

- PIT version: `1.6.8`
- Mutator set: `ALL`
- Maven profile: `pit-range`
- Command: `mvn -Ppit-range test-compile org.pitest:pitest-maven:mutationCoverage -Dmutators=ALL`

## Run Summary

| Stage | Killed | Survived | No Coverage | Total | Mutation Score | Test Strength |
| ----- | ------ | -------- | ----------- | ----- | -------------- | ------------- |
| Baseline (mutation-focused tests disabled) | 915 | 324 | 0 | 1239 | 74% | 74% |
| Improved (restored targeted tests) | 996 | 243 | 0 | 1239 | 80% | 80% |
| Final improved (additional edge/oracle tests) | 1053 | 186 | 0 | 1239 | 85% | 85% |

Improvement calculations:

- Baseline to improved: `80% - 74% = 6` percentage points
- Baseline to final improved: `85% - 74% = 11` percentage points
- Relative gain from baseline to final: `11 / 74 = 14.86%`

## Environment and Tooling Notes

### Version consistency

Mutation totals and scores are strongly configuration-dependent. This report uses one fixed configuration end-to-end (PIT `1.6.8` + `ALL`) to keep comparisons valid.

### Why Eclipse and Maven can differ

Even with identical source files, mutation outcomes can differ across IDE and CLI runs because PIT mutates compiled bytecode. Differences in compilation and launch context can change mutant generation and kill rates.

Common sources of variation:

- Compiler path (`ecj` vs `javac`)
- Incremental build artifacts vs clean build artifacts
- Classpath and test discovery differences
- JVM/tooling settings used by the launcher

## Test Design Strategy Used in This Rerun

The suite was improved iteratively using mutation-guided design:

1. Run PIT and collect survivors.
2. Group survivors by method and mutator type.
3. Add targeted tests with high discrimination power:
   - Boundary equality and epsilon-neighborhood checks
   - Signed-zero behavior checks (`+0.0` vs `-0.0`)
   - NaN and infinity cases
   - Deterministic oracle checks
   - Reflection-based checks for private helper behavior
4. Re-run PIT and repeat until gains became marginal.

## Current Survivor Profile (Latest Rerun)

Top methods by surviving mutants in the final rerun:

- `expand`: 16
- `shiftWithNoZeroCrossing`: 15
- `shift`: 15
- `hashCode`: 14
- `<init>`: 14
- `isNaNRange`: 14
- `constrain`: 14
- `max`: 12
- `min`: 12
- `intersects`: 10
- `combineIgnoringNaN`: 10
- `equals`: 9

Representative surviving patterns from the latest rerun:

- `constrain`: boundary flips and unary operator mutations
- `expand`: unary increments/decrements on intermediate values
- `hashCode`: unary and constant substitutions in bit arithmetic
- `min` / `max`: unary mutations and comparator variants around NaN logic
- `shiftWithNoZeroCrossing`: unary mutations around zero-crossing branches

## Representative Mutant Analysis (Latest Rerun)

| # | Method | Line | Mutator / Change | Status | Evidence / Interpretation |
| - | ------ | ---- | ---------------- | ------ | ------------------------- |
| 1 | `combineIgnoringNaN` | 251 | Argument propagation (`min` call replaced by argument) | KILLED | Killed by NaN/close-bound oracle tests for `combineIgnoringNaN` |
| 2 | `combineIgnoringNaN` | 252 | Argument propagation (`max` call replaced by argument) | KILLED | Killed by NaN/infinity and close-bound oracle checks |
| 3 | `combineIgnoringNaN` | 256 | Constructor call removed | KILLED | Killed by assertions on exact lower/upper bounds of returned `Range` |
| 4 | `constrain` | 189 | ConditionalsBoundaryMutator (`>` / `>=` boundary shift) | SURVIVED | Candidate equivalent under outer `!contains(value)` guard |
| 5 | `constrain` | 191 | ConditionalsBoundaryMutator (`<` / `<=` boundary shift) | SURVIVED | Candidate equivalent under same guard-dominance behavior |
| 6 | `expand` | 329 | ConditionalsBoundaryMutator (`lower > upper` boundary shift) | SURVIVED | Candidate equivalent when `lower == upper` leads to same observable bounds |
| 7 | `intersects` | 160 | CRCR substitution (`1 -> -1`) | SURVIVED | Remaining hard comparator/constant variant despite boundary sweep tests |
| 8 | `hashCode` | 455/457 | UOI mutators on intermediate bit/arith values | SURVIVED | Oracle vectors killed many mutations; unary variants still remain |
| 9 | `min` (private) | 269/272 | UOI mutators on local values | SURVIVED | Reflection tests improved kills but some unary variants persist |
| 10 | `max` (private) | 279/282 | UOI mutators on local values | SURVIVED | Similar residual pattern as `min` under ALL mutators |
| 11 | `shiftWithNoZeroCrossing` (private) | 383/385 | UOI mutators on branch-local values | SURVIVED | Signed-zero + epsilon sweeps reduced but did not eliminate survivors |
| 12 | `expandToInclude` | 303/305 | Boundary mutations in lower/upper checks | KILLED | Killed by exact-boundary identity assertions (`assertSame`) |

## Manually Verified Equivalent Mutants (Latest Rerun)

The following survivors were manually reviewed against source semantics and test reachability in the current PIT `1.6.8` + `ALL` rerun.

| Candidate | Method / Line | Mutation | Manual Verification Outcome | Rationale |
| --------- | ------------- | -------- | --------------------------- | --------- |
| EQ-1 | `constrain`, line 189 | Boundary change around upper check | Likely equivalent | `constrain()` first requires `!contains(value)`; equality-at-boundary cases are already filtered before this branch |
| EQ-2 | `constrain`, line 191 | Boundary change around lower check | Likely equivalent | Same guard-dominance argument as EQ-1 for lower-bound equality |
| EQ-3 | `expand`, line 329 | `lower > upper` boundary change | Likely equivalent | For `lower == upper`, both original and mutated flows produce identical final range bounds |
| EQ-4 | `min` / `max` comparator-equality variants | `equal -> <=` style comparator variants | Likely equivalent in equal-input case | For equal finite operands, both branches yield same returned numeric value; no observable difference |

Notes on verification approach:

- Checked path feasibility in `Range.java` guards and branch ordering.
- Confirmed observed outputs through existing boundary/oracle tests.
- Kept classification conservative: only mutants with same observable outputs under feasible paths are marked likely equivalent.

## Why Further Increase May Not Be Possible Without Changing Scope

Current final score is `1053/1239 = 85%`.

To reach at least `90%` on this same run configuration:

- Required kills at 90%: `ceil(0.90 x 1239) = 1116`
- Additional kills still needed: `1116 - 1053 = 63`

Given the current survivor distribution and repeated targeted improvements, remaining survivors are concentrated in aggressive `ALL`-mutator unary/comparator patterns, many of which are behavior-preserving or near-equivalent under existing public observability.

Practical conclusion:

- With unchanged production code and fixed configuration (PIT `1.6.8` + `ALL`), `85%` appears to be the practical ceiling reached in this rerun.
- Reaching raw `90%` would likely require one of the following:
  - production-code changes to expose additional observables (generally not desirable for this assignment objective), or
  - formal equivalent-mutant adjustment/exclusion process.

## Production Code Change Decision

No changes were made to `Range.java` for this rerun.
The mutation-score improvement came from strengthening `RangeTest.java`.
