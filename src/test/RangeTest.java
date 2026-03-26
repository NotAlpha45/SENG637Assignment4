import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jfree.data.Range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.jfree.data.Range class.
 * Testing methodology: White-Box testing targeting
 * 90% Statement Coverage, 70% Branch Coverage, and 60% Condition Coverage.
 * Techniques applied: Branch Coverage Analysis, Condition Coverage Analysis,
 * Boundary Value Analysis, and Equivalence Class Testing.
 *
 * @author Maheen and Dipu
 */
public class RangeTest {

    private Range exampleRange;

    @Before
    public void setUp() {
        exampleRange = new Range(-1.0, 1.0);
    }

    // #region Range(double, double) constructor tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: Range(double lower, double upper)
     *
     * #####################################################
     */

    /**
     * Test constructor with a valid positive range.
     * Equivalence Class: lower &lt; upper (valid input)
     * Expected: Range is created with correct lower and upper bounds
     */
    @Test
    public void testConstructorValidBounds() {
        Range r = new Range(1.0, 5.0);
        assertEquals(1.0, r.getLowerBound(), 0.0);
        assertEquals(5.0, r.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with equal lower and upper bounds.
     * Boundary Value: lower == upper (point range)
     * Expected: Range is created successfully with a single-point range
     */
    @Test
    public void testConstructorEqualBounds() {
        Range r = new Range(3.0, 3.0);
        assertEquals(3.0, r.getLowerBound(), 0.0);
        assertEquals(3.0, r.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with both bounds being negative.
     * Equivalence Class: Negative ranges (lower &lt; upper, both negative)
     * Expected: Range is created with correct negative bounds
     */
    @Test
    public void testConstructorNegativeBounds() {
        Range r = new Range(-5.0, -1.0);
        assertEquals(-5.0, r.getLowerBound(), 0.0);
        assertEquals(-1.0, r.getUpperBound(), 0.0);
    }

    /**
     * Test constructor when lower bound is greater than upper bound.
     * Equivalence Class: Invalid input — lower &gt; upper
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorLowerGreaterThanUpper() {
        new Range(5.0, 1.0);
    }
    // #endregion

    // #region getLowerBound() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: getLowerBound()
     *
     * #####################################################
     */

    /**
     * Test getLowerBound() with a positive range.
     * Equivalence Class: Positive ranges
     * Expected: Returns 2.0 (the lower bound)
     */
    @Test
    public void testGetLowerBoundPositiveRange() {
        Range r = new Range(2.0, 8.0);
        assertEquals(2.0, r.getLowerBound(), 0.0);
    }

    /**
     * Test getLowerBound() with a fully negative range.
     * Equivalence Class: Negative ranges
     * Expected: Returns -10.0 (the lower bound)
     */
    @Test
    public void testGetLowerBoundNegativeRange() {
        Range r = new Range(-10.0, -1.0);
        assertEquals(-10.0, r.getLowerBound(), 0.0);
    }

    /**
     * Test getLowerBound() using the shared setUp range [-1.0, 1.0].
     * Equivalence Class: Range spanning zero
     * Expected: Returns -1.0 (the lower bound)
     */
    @Test
    public void testGetLowerBoundFromSetUp() {
        assertEquals(-1.0, exampleRange.getLowerBound(), 0.0);
    }
    // #endregion

    // #region getUpperBound() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: getUpperBound()
     *
     * #####################################################
     */

    /**
     * Test getUpperBound() with a positive range.
     * Equivalence Class: Positive ranges
     * Expected: Returns 8.0 (the upper bound)
     */
    @Test
    public void testGetUpperBoundPositiveRange() {
        Range r = new Range(2.0, 8.0);
        assertEquals(8.0, r.getUpperBound(), 0.0);
    }

    /**
     * Test getUpperBound() with a fully negative range.
     * Equivalence Class: Negative ranges
     * Expected: Returns -1.0 (the upper bound)
     */
    @Test
    public void testGetUpperBoundNegativeRange() {
        Range r = new Range(-10.0, -1.0);
        assertEquals(-1.0, r.getUpperBound(), 0.0);
    }

    /**
     * Test getUpperBound() using the shared setUp range [-1.0, 1.0].
     * Equivalence Class: Range spanning zero
     * Expected: Returns 1.0 (the upper bound)
     */
    @Test
    public void testGetUpperBoundFromSetUp() {
        assertEquals(1.0, exampleRange.getUpperBound(), 0.0);
    }
    // #endregion

    // #region getLength() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: getLength()
     *
     * #####################################################
     */

    /**
     * Test getLength() with a positive range.
     * Equivalence Class: Positive ranges
     * Expected: Returns 4.0 (upper - lower)
     */
    @Test
    public void testGetLengthPositiveRange() {
        Range r = new Range(1.0, 5.0);
        assertEquals(4.0, r.getLength(), 0.0);
    }

    /**
     * Test getLength() with equal lower and upper bounds.
     * Boundary Value: lower == upper (zero-length range)
     * Expected: Returns 0.0
     */
    @Test
    public void testGetLengthZeroLength() {
        Range r = new Range(3.0, 3.0);
        assertEquals(0.0, r.getLength(), 0.0);
    }

    /**
     * Test getLength() with a range that crosses zero.
     * Equivalence Class: Ranges spanning positive and negative values
     * Expected: Returns 4.0 (total span from -2 to 2)
     */
    @Test
    public void testGetLengthCrossZero() {
        Range r = new Range(-2.0, 2.0);
        assertEquals(4.0, r.getLength(), 0.0);
    }
    // #endregion

    // #region getCentralValue() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: getCentralValue()
     *
     * #####################################################
     */

    /**
     * Test getCentralValue() with a symmetric range spanning zero.
     * Equivalence Class: Range spanning positive and negative values
     * Expected: Returns 0.0 (midpoint of [-1.0, 1.0])
     */
    @Test
    public void testGetCentralValueSymmetricRange() {
        assertEquals(0.0, exampleRange.getCentralValue(), 0.0);
    }

    /**
     * Test getCentralValue() with a positive range.
     * Equivalence Class: Positive ranges
     * Expected: Returns 3.0 (midpoint of [1.0, 5.0])
     */
    @Test
    public void testGetCentralValuePositiveRange() {
        Range r = new Range(1.0, 5.0);
        assertEquals(3.0, r.getCentralValue(), 0.0);
    }

    /**
     * Test getCentralValue() with a fully negative range.
     * Equivalence Class: Negative ranges
     * Expected: Returns -4.0 (midpoint of [-6.0, -2.0])
     */
    @Test
    public void testGetCentralValueNegativeRange() {
        Range r = new Range(-6.0, -2.0);
        assertEquals(-4.0, r.getCentralValue(), 0.0);
    }

    /**
     * Test getCentralValue() with equal lower and upper bounds.
     * Boundary Value: lower == upper (point range)
     * Expected: Returns 4.0 (the single value is its own midpoint)
     */
    @Test
    public void testGetCentralValueZeroLength() {
        Range r = new Range(4.0, 4.0);
        assertEquals(4.0, r.getCentralValue(), 0.0);
    }
    // #endregion

    // #region contains() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: contains(double value)
     *
     * #####################################################
     */

    /**
     * Test contains() with a value strictly below the lower bound.
     * Boundary Value: BLB (below lower bound)
     * Branch: value &lt; lower → returns false immediately
     * Expected: Returns false
     */
    @Test
    public void testContainsValueBelowLower() {
        assertFalse(exampleRange.contains(-2.0));
    }

    /**
     * Test contains() with a value strictly above the upper bound.
     * Boundary Value: AUB (above upper bound)
     * Branch: value &gt; upper → returns false
     * Expected: Returns false
     */
    @Test
    public void testContainsValueAboveUpper() {
        assertFalse(exampleRange.contains(2.0));
    }

    /**
     * Test contains() with a value exactly at the lower bound.
     * Boundary Value: LB (lower bound)
     * Equivalence Class: value == lower (boundary inclusion)
     * Expected: Returns true
     */
    @Test
    public void testContainsValueAtLowerBound() {
        assertTrue(exampleRange.contains(-1.0));
    }

    /**
     * Test contains() with a value exactly at the upper bound.
     * Boundary Value: UB (upper bound)
     * Equivalence Class: value == upper (boundary inclusion)
     * Expected: Returns true
     */
    @Test
    public void testContainsValueAtUpperBound() {
        assertTrue(exampleRange.contains(1.0));
    }

    /**
     * Test contains() with the midpoint value of the range.
     * Boundary Value: NOM (nominal midpoint)
     * Equivalence Class: lower &lt; value &lt; upper (strictly within range)
     * Expected: Returns true
     */
    @Test
    public void testContainsValueInMiddle() {
        assertTrue(exampleRange.contains(0.0));
    }

    /**
     * Test contains() with NaN input.
     * Expected: Returns false because NaN fails all ordered comparisons.
     */
    @Test
    public void testContainsNaN() {
        assertFalse(exampleRange.contains(Double.NaN));
    }

    /**
     * Test contains() with positive infinity outside a finite range.
     * Expected: Returns false.
     */
    @Test
    public void testContainsPositiveInfinityOutsideFiniteRange() {
        assertFalse(exampleRange.contains(Double.POSITIVE_INFINITY));
    }

    /**
     * Test contains() with negative infinity outside a finite range.
     * Expected: Returns false.
     */
    @Test
    public void testContainsNegativeInfinityOutsideFiniteRange() {
        assertFalse(exampleRange.contains(Double.NEGATIVE_INFINITY));
    }
    // #endregion

    // #region intersects(double, double) tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: intersects(double b0, double b1)
     *
     * #####################################################
     */

    /**
     * Test intersects() when b0 is below the lower bound and b1 falls inside the
     * range.
     * Branch: b0 &lt;= lower is true, b1 &gt; lower is true → returns true
     * Expected: Returns true
     */
    @Test
    public void testIntersectsB0BelowLowerB1InsideRange() {
        Range r = new Range(2.0, 8.0);
        assertTrue(r.intersects(0.0, 5.0));
    }

    /**
     * Test intersects() when b0 is below the lower bound and b1 is exactly at the
     * lower bound.
     * Boundary Value: b1 == lower
     * Branch: b0 &lt;= lower is true, b1 &gt; lower is false → returns false
     * Expected: Returns false
     */
    @Test
    public void testIntersectsB0BelowLowerB1AtLower() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(0.0, 2.0));
    }

    /**
     * Mutation-focused test for boundary behavior when b0 equals lower bound.
     * Expected: touching exactly at lower endpoint is not considered intersection.
     */
    @Test
    public void testIntersectsB0AtLowerB1AtLower() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(2.0, 2.0));
    }

    /**
     * Test intersects() when both b0 and b1 are below the lower bound.
     * Equivalence Class: Disjoint range entirely to the left
     * Branch: b0 &lt;= lower is true, b1 &gt; lower is false → returns false
     * Expected: Returns false
     */
    @Test
    public void testIntersectsB0BelowLowerB1BelowLower() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(0.0, 1.0));
    }

    /**
     * Test intersects() when both b0 and b1 are strictly inside the range.
     * Equivalence Class: Contained range (b0 and b1 within bounds)
     * Branch: b0 &gt; lower, b0 &lt; upper, b1 &gt;= b0 → returns true
     * Expected: Returns true
     */
    @Test
    public void testIntersectsB0InsideRangeB1InsideRange() {
        Range r = new Range(2.0, 8.0);
        assertTrue(r.intersects(3.0, 6.0));
    }

    /**
     * Mutation-focused test for inclusive b1 >= b0 condition in second branch.
     * Expected: a single-point interval inside the range intersects.
     */
    @Test
    public void testIntersectsSinglePointInsideRange() {
        Range r = new Range(2.0, 8.0);
        assertTrue(r.intersects(3.0, 3.0));
    }

    /**
     * Test intersects() when b0 is inside the range and b1 exceeds the upper bound.
     * Equivalence Class: Overlapping range extending beyond upper
     * Branch: b0 &gt; lower, b0 &lt; upper, b1 &gt; upper → returns true
     * Expected: Returns true
     */
    @Test
    public void testIntersectsB0InsideRangeB1AboveUpper() {
        Range r = new Range(2.0, 8.0);
        assertTrue(r.intersects(5.0, 12.0));
    }

    /**
     * Test intersects() when b0 is exactly at the upper bound.
     * Boundary Value: b0 == upper
     * Branch: b0 &gt; lower, b0 &lt; upper is false → returns false
     * Expected: Returns false
     */
    @Test
    public void testIntersectsB0AtUpper() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(8.0, 10.0));
    }

    /**
     * Test intersects() when b0 is strictly above the upper bound.
     * Equivalence Class: Disjoint range entirely to the right
     * Branch: b0 &gt; lower, b0 &lt; upper is false → returns false
     * Expected: Returns false
     */
    @Test
    public void testIntersectsB0AboveUpper() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(9.0, 12.0));
    }

    /**
     * Test intersects() with NaN lower argument.
     * Expected: Returns false.
     */
    @Test
    public void testIntersectsWithNaNLowerArgument() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(Double.NaN, 5.0));
    }

    /**
     * Test intersects() with NaN upper argument.
     * Expected: Returns false.
     */
    @Test
    public void testIntersectsWithNaNUpperArgument() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(3.0, Double.NaN));
    }

    /**
     * Test intersects() with invalid interval where b1 < b0.
     * Expected: Returns false in second branch due b1 >= b0 check.
     */
    @Test
    public void testIntersectsInvalidIntervalOrder() {
        Range r = new Range(2.0, 8.0);
        assertFalse(r.intersects(5.0, 4.0));
    }

    /**
     * Epsilon-boundary test around lower bound for intersects().
     */
    @Test
    public void testIntersectsEpsilonAroundLowerBound() {
        Range r = new Range(2.0, 8.0);
        double eps = 1e-12;
        assertFalse(r.intersects(2.0 - eps, 2.0));
        assertTrue(r.intersects(2.0 - eps, 2.0 + eps));
    }

    /**
     * Epsilon-boundary test around upper bound for intersects().
     */
    @Test
    public void testIntersectsEpsilonAroundUpperBound() {
        Range r = new Range(2.0, 8.0);
        double eps = 1e-12;
        assertTrue(r.intersects(8.0 - eps, 8.0 - eps));
        assertFalse(r.intersects(8.0, 8.0 + eps));
    }
    // #endregion

    // #region intersects(Range) tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: intersects(Range range)
     *
     * #####################################################
     */

    /**
     * Test intersects(Range) when two ranges overlap.
     * Equivalence Class: Overlapping ranges
     * Expected: Returns true
     */
    @Test
    public void testIntersectsRangeOverlapping() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(3.0, 7.0);
        assertTrue(r1.intersects(r2));
    }

    /**
     * Test intersects(Range) when two ranges are disjoint.
     * Equivalence Class: Non-overlapping ranges
     * Expected: Returns false
     */
    @Test
    public void testIntersectsRangeNonOverlapping() {
        Range r1 = new Range(1.0, 3.0);
        Range r2 = new Range(5.0, 9.0);
        assertFalse(r1.intersects(r2));
    }
    // #endregion

    // #region constrain() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: constrain(double value)
     *
     * #####################################################
     */

    /**
     * Test constrain() with a value strictly within the range.
     * Equivalence Class: Value contained within range
     * Branch: contains() returns true → returns value unchanged
     * Expected: Returns 5.0
     */
    @Test
    public void testConstrainValueWithinRange() {
        Range r = new Range(0.0, 10.0);
        assertEquals(5.0, r.constrain(5.0), 0.0);
    }

    /**
     * Test constrain() with a value exactly at the lower bound.
     * Boundary Value: LB (lower bound)
     * Branch: contains() returns true → returns value unchanged
     * Expected: Returns 0.0
     */
    @Test
    public void testConstrainValueAtLowerBound() {
        Range r = new Range(0.0, 10.0);
        assertEquals(0.0, r.constrain(0.0), 0.0);
    }

    /**
     * Test constrain() with a value exactly at the upper bound.
     * Boundary Value: UB (upper bound)
     * Branch: contains() returns true → returns value unchanged
     * Expected: Returns 10.0
     */
    @Test
    public void testConstrainValueAtUpperBound() {
        Range r = new Range(0.0, 10.0);
        assertEquals(10.0, r.constrain(10.0), 0.0);
    }

    /**
     * Mutation-focused test for upper-bound equality using signed zero.
     * Expected: method returns the original input value (+0.0), not upper bound (-0.0).
     */
    @Test
    public void testConstrainUpperBoundaryKeepsInputSignedZero() {
        Range r = new Range(-1.0, -0.0);
        double constrained = r.constrain(+0.0);
        assertEquals(0x0000000000000000L, Double.doubleToLongBits(constrained));
    }

    /**
     * Mutation-focused test for lower-bound equality using signed zero.
     * Expected: method returns original input value (-0.0), not lower bound (+0.0).
     */
    @Test
    public void testConstrainLowerBoundaryKeepsInputSignedZero() {
        Range r = new Range(+0.0, 1.0);
        double constrained = r.constrain(-0.0);
        assertEquals(0x8000000000000000L, Double.doubleToLongBits(constrained));
    }

    /**
     * Test constrain() with a value above the upper bound.
     * Boundary Value: AUB (above upper bound)
     * Branch: !contains → value &gt; upper → result = upper
     * Expected: Returns 10.0 (clamped to upper bound)
     */
    @Test
    public void testConstrainValueAboveUpper() {
        Range r = new Range(0.0, 10.0);
        assertEquals(10.0, r.constrain(15.0), 0.0);
    }

    /**
     * Test constrain() with a value below the lower bound.
     * Boundary Value: BLB (below lower bound)
     * Branch: !contains → value &lt; lower → result = lower
     * Expected: Returns 0.0 (clamped to lower bound)
     */
    @Test
    public void testConstrainValueBelowLower() {
        Range r = new Range(0.0, 10.0);
        assertEquals(0.0, r.constrain(-5.0), 0.0);
    }

    /**
     * Test constrain() with NaN input.
     * Expected: Returns NaN unchanged.
     */
    @Test
    public void testConstrainNaNValue() {
        Range r = new Range(0.0, 10.0);
        assertTrue(Double.isNaN(r.constrain(Double.NaN)));
    }

    /**
     * Test constrain() with positive infinity.
     * Expected: Clamps to upper bound.
     */
    @Test
    public void testConstrainPositiveInfinity() {
        Range r = new Range(0.0, 10.0);
        assertEquals(10.0, r.constrain(Double.POSITIVE_INFINITY), 0.0);
    }

    /**
     * Test constrain() with negative infinity.
     * Expected: Clamps to lower bound.
     */
    @Test
    public void testConstrainNegativeInfinity() {
        Range r = new Range(0.0, 10.0);
        assertEquals(0.0, r.constrain(Double.NEGATIVE_INFINITY), 0.0);
    }

    /**
     * Epsilon-boundary test around constrain() limits.
     */
    @Test
    public void testConstrainEpsilonAroundBounds() {
        Range r = new Range(0.0, 10.0);
        double eps = 1e-12;
        assertEquals(0.0, r.constrain(-eps), 0.0);
        assertEquals(10.0, r.constrain(10.0 + eps), 0.0);
        assertEquals(eps, r.constrain(eps), 0.0);
        assertEquals(10.0 - eps, r.constrain(10.0 - eps), 0.0);
    }

    /**
     * Constrain test with very narrow interval.
     */
    @Test
    public void testConstrainVeryNarrowRange() {
        Range r = new Range(1.0, 1.0 + 1e-12);
        assertEquals(1.0, r.constrain(0.5), 0.0);
        assertEquals(1.0 + 1e-12, r.constrain(2.0), 0.0);
        assertEquals(1.0 + 5e-13, r.constrain(1.0 + 5e-13), 0.0);
    }
    // #endregion

    // #region combine() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: combine(Range range1, Range range2)
     *
     * #####################################################
     */

    /**
     * Test combine() when range1 is null.
     * Branch: range1 == null → returns range2
     * Expected: Returns range2
     */
    @Test
    public void testCombineRange1Null() {
        Range r = new Range(1.0, 5.0);
        assertEquals(r, Range.combine(null, r));
    }

    /**
     * Test combine() when range2 is null.
     * Branch: range2 == null → returns range1
     * Expected: Returns range1
     */
    @Test
    public void testCombineRange2Null() {
        Range r = new Range(1.0, 5.0);
        assertEquals(r, Range.combine(r, null));
    }

    /**
     * Test combine() when both ranges are null.
     * Branch: range1 == null → returns range2 (which is also null)
     * Expected: Returns null
     */
    @Test
    public void testCombineBothNull() {
        assertNull(Range.combine(null, null));
    }

    /**
     * Test combine() when both ranges are non-null.
     * Equivalence Class: Both ranges valid — returns spanning range
     * Expected: Returns new Range(1.0, 9.0) spanning both inputs
     */
    @Test
    public void testCombineBothNonNull() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(3.0, 9.0);
        assertEquals(new Range(1.0, 9.0), Range.combine(r1, r2));
    }
    // #endregion

    // #region combineIgnoringNaN() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: combineIgnoringNaN(Range range1, Range range2)
     *
     * #####################################################
     */

    /**
     * Test combineIgnoringNaN() when both ranges are null.
     * Branch: range1 == null, range2 == null → range2 != null is false → returns
     * null
     * Expected: Returns null
     */
    @Test
    public void testCombineIgnoringNaNBothNull() {
        assertNull(Range.combineIgnoringNaN(null, null));
    }

    /**
     * Test combineIgnoringNaN() when range1 is null and range2 is a valid range.
     * Branch: range1 == null, range2 is not a NaN range → returns range2
     * Expected: Returns range2
     */
    @Test
    public void testCombineIgnoringNaNRange1NullRange2Valid() {
        Range r = new Range(1.0, 5.0);
        assertEquals(r, Range.combineIgnoringNaN(null, r));
    }

    /**
     * Test combineIgnoringNaN() when range1 is null and range2 is a NaN range.
     * Branch: range1 == null, range2.isNaNRange() is true → returns null
     * Expected: Returns null
     */
    @Test
    public void testCombineIgnoringNaNRange1NullRange2NaN() {
        Range nanRange = new Range(Double.NaN, Double.NaN);
        assertNull(Range.combineIgnoringNaN(null, nanRange));
    }

    /**
     * Test combineIgnoringNaN() when range2 is null and range1 is a valid range.
     * Branch: range2 == null, range1.isNaNRange() is false → returns range1
     * Expected: Returns range1
     */
    @Test
    public void testCombineIgnoringNaNRange2NullRange1Valid() {
        Range r = new Range(1.0, 5.0);
        assertEquals(r, Range.combineIgnoringNaN(r, null));
    }

    /**
     * Test combineIgnoringNaN() when range2 is null and range1 is a NaN range.
     * Branch: range2 == null, range1.isNaNRange() is true → returns null
     * Expected: Returns null
     */
    @Test
    public void testCombineIgnoringNaNRange2NullRange1NaN() {
        Range nanRange = new Range(Double.NaN, Double.NaN);
        assertNull(Range.combineIgnoringNaN(nanRange, null));
    }

    /**
     * Test combineIgnoringNaN() when both ranges are valid (no NaN values).
     * Equivalence Class: Both ranges non-null, no NaN bounds
     * Branch: private min/max methods take the non-NaN path
     * Expected: Returns new Range(1.0, 9.0) spanning both inputs
     */
    @Test
    public void testCombineIgnoringNaNBothNonNullValid() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(3.0, 9.0);
        assertEquals(new Range(1.0, 9.0), Range.combineIgnoringNaN(r1, r2));
    }

    /**
     * Test combineIgnoringNaN() when both ranges are NaN ranges.
     * Branch: Both NaN → private min(NaN,NaN)=NaN, max(NaN,NaN)=NaN →
     * Double.isNaN(l) &amp;&amp; Double.isNaN(u) is true → returns null
     * Expected: Returns null
     */
    @Test
    public void testCombineIgnoringNaNBothNaN() {
        Range nan1 = new Range(Double.NaN, Double.NaN);
        Range nan2 = new Range(Double.NaN, Double.NaN);
        assertNull(Range.combineIgnoringNaN(nan1, nan2));
    }

    /**
     * Test combineIgnoringNaN() when range1 has NaN bounds and range2 is valid.
     * Branch: private min(d1=NaN, d2) → d1 is NaN → returns d2;
     * private max(d1=NaN, d2) → d1 is NaN → returns d2
     * Expected: Returns new Range(2.0, 6.0) using range2's bounds
     */
    @Test
    public void testCombineIgnoringNaNRange1NaNBoundRange2Valid() {
        Range r1 = new Range(Double.NaN, Double.NaN);
        Range r2 = new Range(2.0, 6.0);
        assertEquals(new Range(2.0, 6.0), Range.combineIgnoringNaN(r1, r2));
    }

    /**
     * Test combineIgnoringNaN() when range2 has NaN bounds and range1 is valid.
     * Branch: private min(d1, d2=NaN) → d2 is NaN → returns d1;
     * private max(d1, d2=NaN) → d2 is NaN → returns d1
     * Expected: Returns new Range(2.0, 6.0) using range1's bounds
     */
    @Test
    public void testCombineIgnoringNaNRange2NaNBoundRange1Valid() {
        Range r1 = new Range(2.0, 6.0);
        Range r2 = new Range(Double.NaN, Double.NaN);
        assertEquals(new Range(2.0, 6.0), Range.combineIgnoringNaN(r1, r2));
    }

    /**
     * Test combineIgnoringNaN() when only one lower bound is NaN.
     * Expected: Lower selected from non-NaN bound and upper from max operation.
     */
    @Test
    public void testCombineIgnoringNaNOneLowerNaN() {
        Range r1 = new Range(Double.NaN, 6.0);
        Range r2 = new Range(2.0, 5.0);
        assertEquals(new Range(2.0, 6.0), Range.combineIgnoringNaN(r1, r2));
    }

    /**
     * Test combineIgnoringNaN() when only one upper bound is NaN.
     * Expected: Upper selected from non-NaN bound and lower from min operation.
     */
    @Test
    public void testCombineIgnoringNaNOneUpperNaN() {
        Range r1 = new Range(1.0, Double.NaN);
        Range r2 = new Range(2.0, 5.0);
        assertEquals(new Range(1.0, 5.0), Range.combineIgnoringNaN(r1, r2));
    }

    /**
     * Test combineIgnoringNaN() with infinities in finite/extended ranges.
     * Expected: Proper min/max selection preserving infinities.
     */
    @Test
    public void testCombineIgnoringNaNWithInfinities() {
        Range r1 = new Range(Double.NEGATIVE_INFINITY, -1.0);
        Range r2 = new Range(2.0, Double.POSITIVE_INFINITY);
        assertEquals(new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
                Range.combineIgnoringNaN(r1, r2));
    }

    /**
     * Close-value test to stress min/max selection in combineIgnoringNaN().
     */
    @Test
    public void testCombineIgnoringNaNCloseBoundsSelection() {
        Range r1 = new Range(1.000000000001, 5.000000000001);
        Range r2 = new Range(1.000000000002, 5.000000000002);
        Range out = Range.combineIgnoringNaN(r1, r2);
        assertEquals(1.000000000001, out.getLowerBound(), 0.0);
        assertEquals(5.000000000002, out.getUpperBound(), 0.0);
    }

    /**
     * Reverse close-value order to stress alternate min/max branch decisions.
     */
    @Test
    public void testCombineIgnoringNaNCloseBoundsSelectionReversed() {
        Range r1 = new Range(1.000000000002, 5.000000000002);
        Range r2 = new Range(1.000000000001, 5.000000000001);
        Range out = Range.combineIgnoringNaN(r1, r2);
        assertEquals(1.000000000001, out.getLowerBound(), 0.0);
        assertEquals(5.000000000002, out.getUpperBound(), 0.0);
    }
    // #endregion

    // #region expandToInclude() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: expandToInclude(Range range, double value)
     *
     * #####################################################
     */

    /**
     * Test expandToInclude() when the range is null.
     * Branch: range == null → returns new Range(value, value)
     * Expected: Returns Range(5.0, 5.0)
     */
    @Test
    public void testExpandToIncludeNullRange() {
        Range r = Range.expandToInclude(null, 5.0);
        assertEquals(new Range(5.0, 5.0), r);
    }

    /**
     * Test expandToInclude() when the value is below the lower bound.
     * Boundary Value: value &lt; lower
     * Branch: value &lt; range.getLowerBound() → returns new Range(value, upper)
     * Expected: Returns Range(0.0, 8.0)
     */
    @Test
    public void testExpandToIncludeValueBelowLower() {
        Range r = new Range(2.0, 8.0);
        assertEquals(new Range(0.0, 8.0), Range.expandToInclude(r, 0.0));
    }

    /**
     * Test expandToInclude() when the value is above the upper bound.
     * Boundary Value: value &gt; upper
     * Branch: value &gt; range.getUpperBound() → returns new Range(lower, value)
     * Expected: Returns Range(2.0, 10.0)
     */
    @Test
    public void testExpandToIncludeValueAboveUpper() {
        Range r = new Range(2.0, 8.0);
        assertEquals(new Range(2.0, 10.0), Range.expandToInclude(r, 10.0));
    }

    /**
     * Test expandToInclude() when the value is within the range.
     * Equivalence Class: Value already contained in range
     * Branch: value within bounds → returns the original range object unchanged
     * Expected: Returns the same Range object (no expansion needed)
     */
    @Test
    public void testExpandToIncludeValueWithinRange() {
        Range r = new Range(2.0, 8.0);
        assertSame(r, Range.expandToInclude(r, 5.0));
    }

    /**
     * Mutation-focused test for lower-bound boundary in expandToInclude().
     * Expected: when value equals lower bound, method returns the same object.
     */
    @Test
    public void testExpandToIncludeValueAtLowerReturnsSameInstance() {
        Range r = new Range(2.0, 8.0);
        assertSame(r, Range.expandToInclude(r, 2.0));
    }

    /**
     * Mutation-focused test for upper-bound boundary in expandToInclude().
     * Expected: when value equals upper bound, method returns the same object.
     */
    @Test
    public void testExpandToIncludeValueAtUpperReturnsSameInstance() {
        Range r = new Range(2.0, 8.0);
        assertSame(r, Range.expandToInclude(r, 8.0));
    }
    // #endregion

    // #region expand() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: expand(Range range, double lowerMargin, double upperMargin)
     *
     * #####################################################
     */

    /**
     * Test expand() with valid positive margins.
     * Equivalence Class: Normal expansion with positive margins
     * Calculation: range [2,6], length=4; lower=2−4×0.25=1, upper=6+4×0.5=8
     * Expected: Returns Range(1.0, 8.0)
     */
    @Test
    public void testExpandNormal() {
        Range r = new Range(2.0, 6.0);
        assertEquals(new Range(1.0, 8.0), Range.expand(r, 0.25, 0.5));
    }

    /**
     * Test expand() when the range argument is null.
     * Branch: ParamChecks.nullNotPermitted throws IllegalArgumentException
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExpandNullRange() {
        Range.expand(null, 0.25, 0.5);
    }

    /**
     * Test expand() when negative margins cause the computed lower to exceed upper.
     * Branch: lower &gt; upper after expansion → both are collapsed to their
     * midpoint
     * Calculation: range [0,1], length=1; lower=1.5, upper=−0.5 → mid=0.5
     * Expected: Returns Range(0.5, 0.5)
     */
    @Test
    public void testExpandLowerExceedsUpperAfterExpansion() {
        Range result = Range.expand(new Range(0.0, 1.0), -1.5, -1.5);
        assertEquals(new Range(0.5, 0.5), result);
    }

    /**
     * Additional expand() oracle check with mixed margins.
     */
    @Test
    public void testExpandMixedMarginsOracle() {
        Range r = new Range(-4.0, 10.0);
        Range out = Range.expand(r, 0.125, -0.25);
        // length = 14, lower = -4 - 1.75 = -5.75, upper = 10 - 3.5 = 6.5
        assertEquals(new Range(-5.75, 6.5), out);
    }

    /**
     * Additional expand() oracle check where margins increase asymmetrically.
     */
    @Test
    public void testExpandAsymmetricPositiveMarginsOracle() {
        Range r = new Range(1.0, 9.0);
        Range out = Range.expand(r, 0.375, 0.125);
        // length = 8, lower = -2.0, upper = 10.0
        assertEquals(new Range(-2.0, 10.0), out);
    }
    // #endregion

    // #region shift(Range, double) tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: shift(Range base, double delta)
     *
     * #####################################################
     */

    /**
     * Test shift() with a positive delta (delegates to 3-arg form,
     * allowZeroCrossing=false).
     * Equivalence Class: Positive delta, range stays positive
     * Expected: Returns Range(3.0, 5.0)
     */
    @Test
    public void testShiftPositiveDelta() {
        Range r = new Range(1.0, 3.0);
        assertEquals(new Range(3.0, 5.0), Range.shift(r, 2.0));
    }

    /**
     * Test shift() with a negative delta.
     * Equivalence Class: Negative delta, range stays positive
     * Expected: Returns Range(1.0, 3.0)
     */
    @Test
    public void testShiftNegativeDelta() {
        Range r = new Range(2.0, 4.0);
        assertEquals(new Range(1.0, 3.0), Range.shift(r, -1.0));
    }

    /**
     * Test shift() when the base range is null.
     * Branch: ParamChecks.nullNotPermitted throws IllegalArgumentException
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShiftNullBase() {
        Range.shift(null, 1.0);
    }
    // #endregion

    // #region shift(Range, double, boolean) tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: shift(Range base, double delta, boolean allowZeroCrossing)
     *
     * #####################################################
     */

    /**
     * Test shift() with allowZeroCrossing=true.
     * Branch: allowZeroCrossing is true → adds delta directly without clamping
     * Expected: Returns Range(2.0, 4.0)
     */
    @Test
    public void testShiftAllowZeroCrossingTrue() {
        Range r = new Range(-3.0, -1.0);
        assertEquals(new Range(2.0, 4.0), Range.shift(r, 5.0, true));
    }

    /**
     * Test shift() with allowZeroCrossing=false when values are positive and do not
     * cross zero.
     * Branch: value &gt; 0, delta &gt; 0 → Math.max(v+delta, 0) = v+delta
     * Expected: Returns Range(3.0, 5.0)
     */
    @Test
    public void testShiftNoZeroCrossingPositiveValueNoCross() {
        Range r = new Range(1.0, 3.0);
        assertEquals(new Range(3.0, 5.0), Range.shift(r, 2.0, false));
    }

    /**
     * Test shift() with allowZeroCrossing=false when a positive value would cross
     * zero.
     * Branch: value &gt; 0, v+delta &lt; 0 → Math.max(v+delta, 0) = 0
     * Expected: Returns Range(0.0, 0.0) (clamped at zero)
     */
    @Test
    public void testShiftNoZeroCrossingPositiveValueCrossesZero() {
        Range r = new Range(1.0, 3.0);
        assertEquals(new Range(0.0, 0.0), Range.shift(r, -5.0, false));
    }

    /**
     * Test shift() with allowZeroCrossing=false when values are negative and do not
     * cross zero.
     * Branch: value &lt; 0, delta &lt; 0 → Math.min(v+delta, 0) = v+delta
     * Expected: Returns Range(-5.0, -3.0)
     */
    @Test
    public void testShiftNoZeroCrossingNegativeValueNoCross() {
        Range r = new Range(-4.0, -2.0);
        assertEquals(new Range(-5.0, -3.0), Range.shift(r, -1.0, false));
    }

    /**
     * Test shift() with allowZeroCrossing=false when a negative value would cross
     * zero.
     * Branch: value &lt; 0, v+delta &gt; 0 → Math.min(v+delta, 0) = 0
     * Expected: Returns Range(0.0, 0.0) (clamped at zero)
     */
    @Test
    public void testShiftNoZeroCrossingNegativeValueCrossesZero() {
        Range r = new Range(-2.0, -1.0);
        assertEquals(new Range(0.0, 0.0), Range.shift(r, 5.0, false));
    }

    /**
     * Test shift() with allowZeroCrossing=false when initial value is exactly zero.
     * Boundary Value: value == 0
     * Branch: value == 0 → returns value + delta (no clamping)
     * Expected: Returns Range(3.0, 3.0)
     */
    @Test
    public void testShiftNoZeroCrossingZeroValue() {
        Range r = new Range(0.0, 0.0);
        assertEquals(new Range(3.0, 3.0), Range.shift(r, 3.0, false));
    }

    /**
     * Test shift() with allowZeroCrossing=false when lower is +0.0 and delta is
     * negative.
     * Expected: lower moves negative through zero because exact zero uses value+delta
     * branch.
     */
    @Test
    public void testShiftNoZeroCrossingPositiveZeroMovesNegative() {
        Range r = new Range(+0.0, 2.0);
        assertEquals(new Range(-1.0, 1.0), Range.shift(r, -1.0, false));
    }

    /**
     * Test shift() with allowZeroCrossing=false when upper is -0.0 and delta is
     * positive.
     * Expected: upper moves positive through zero because exact zero uses value+delta
     * branch.
     */
    @Test
    public void testShiftNoZeroCrossingNegativeZeroMovesPositive() {
        Range r = new Range(-2.0, -0.0);
        assertEquals(new Range(-1.0, 1.0), Range.shift(r, 1.0, false));
    }

    /**
     * Epsilon crossing behavior when allowZeroCrossing is false.
     */
    @Test
    public void testShiftNoZeroCrossingEpsilonCrossing() {
        Range r = new Range(1e-12, 2e-12);
        assertEquals(new Range(0.0, 1e-12), Range.shift(r, -1e-12, false));
    }

    /**
     * Epsilon non-crossing behavior when allowZeroCrossing is false.
     */
    @Test
    public void testShiftNoZeroCrossingEpsilonNoCrossing() {
        Range r = new Range(-2e-12, -1e-12);
        assertEquals(new Range(-3e-12, -2e-12), Range.shift(r, -1e-12, false));
    }

    /**
     * Mutation-focused test for value == 0.0 path in shiftWithNoZeroCrossing().
     * Expected: zero lower bound with negative delta can become negative.
     */
    @Test
    public void testShiftNoZeroCrossingZeroLowerWithNegativeDelta() {
        Range r = new Range(0.0, 1.0);
        assertEquals(new Range(-1.0, 0.0), Range.shift(r, -1.0, false));
    }
    // #endregion

    // #region scale() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: scale(Range base, double factor)
     *
     * #####################################################
     */

    /**
     * Test scale() with a valid positive factor.
     * Equivalence Class: Positive factor
     * Expected: Returns Range(2.0, 6.0) (each bound multiplied by 2)
     */
    @Test
    public void testScaleNormal() {
        Range r = new Range(1.0, 3.0);
        assertEquals(new Range(2.0, 6.0), Range.scale(r, 2.0));
    }

    /**
     * Test scale() with a factor of zero.
     * Boundary Value: factor == 0
     * Expected: Returns Range(0.0, 0.0) (all bounds collapse to zero)
     */
    @Test
    public void testScaleByZero() {
        Range r = new Range(1.0, 5.0);
        assertEquals(new Range(0.0, 0.0), Range.scale(r, 0.0));
    }

    /**
     * Test scale() with a negative factor.
     * Branch: factor &lt; 0 → throws IllegalArgumentException
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testScaleNegativeFactor() {
        Range.scale(new Range(1.0, 3.0), -1.0);
    }

    /**
     * Test scale() when the base range is null.
     * Branch: ParamChecks.nullNotPermitted throws IllegalArgumentException
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testScaleNullBase() {
        Range.scale(null, 2.0);
    }
    // #endregion

    // #region equals() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: equals(Object obj)
     *
     * #####################################################
     */

    /**
     * Test equals() with two ranges that have identical bounds.
     * Equivalence Class: Equal ranges
     * Branch: instanceof Range → lower equal → upper equal → returns true
     * Expected: Returns true
     */
    @Test
    public void testEqualsIdenticalRanges() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(1.0, 5.0);
        assertTrue(r1.equals(r2));
    }

    /**
     * Test equals() when lower bounds differ.
     * Branch: instanceof Range, lower not equal → returns false
     * Expected: Returns false
     */
    @Test
    public void testEqualsDifferentLower() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(2.0, 5.0);
        assertFalse(r1.equals(r2));
    }

    /**
     * Test equals() when upper bounds differ.
     * Branch: instanceof Range, lower equal, upper not equal → returns false
     * Expected: Returns false
     */
    @Test
    public void testEqualsDifferentUpper() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(1.0, 6.0);
        assertFalse(r1.equals(r2));
    }

    /**
     * Test equals() when compared against a non-Range object.
     * Branch: !(obj instanceof Range) → returns false
     * Expected: Returns false
     */
    @Test
    public void testEqualsNonRangeObject() {
        assertFalse(exampleRange.equals("not a range"));
    }

    /**
     * Test equals() when compared against null.
     * Branch: !(obj instanceof Range) since null is not an instance → returns false
     * Expected: Returns false
     */
    @Test
    public void testEqualsNull() {
        assertFalse(exampleRange.equals(null));
    }

    /**
     * Test equals() with NaN lower bound values.
     * Expected: false because NaN == NaN is false for primitive doubles.
     */
    @Test
    public void testEqualsNaNLowerBounds() {
        Range r1 = new Range(Double.NaN, 1.0);
        Range r2 = new Range(Double.NaN, 1.0);
        assertFalse(r1.equals(r2));
    }

    /**
     * Test equals() with NaN upper bound values.
     * Expected: false because NaN == NaN is false for primitive doubles.
     */
    @Test
    public void testEqualsNaNUpperBounds() {
        Range r1 = new Range(1.0, Double.NaN);
        Range r2 = new Range(1.0, Double.NaN);
        assertFalse(r1.equals(r2));
    }
    // #endregion

    // #region isNaNRange() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: isNaNRange()
     *
     * #####################################################
     */

    /**
     * Test isNaNRange() when both bounds are Double.NaN.
     * Condition: Double.isNaN(lower) &amp;&amp; Double.isNaN(upper) — both true
     * Expected: Returns true
     */
    @Test
    public void testIsNaNRangeBothNaN() {
        Range r = new Range(Double.NaN, Double.NaN);
        assertTrue(r.isNaNRange());
    }

    /**
     * Test isNaNRange() when both bounds are ordinary numbers.
     * Condition: Double.isNaN(lower) is false → overall condition is false
     * Expected: Returns false
     */
    @Test
    public void testIsNaNRangeNeitherNaN() {
        assertFalse(exampleRange.isNaNRange());
    }

    /**
     * Test isNaNRange() when only lower is NaN.
     * Expected: false.
     */
    @Test
    public void testIsNaNRangeOnlyLowerNaN() {
        Range r = new Range(Double.NaN, 1.0);
        assertFalse(r.isNaNRange());
    }

    /**
     * Test isNaNRange() when only upper is NaN.
     * Expected: false.
     */
    @Test
    public void testIsNaNRangeOnlyUpperNaN() {
        Range r = new Range(1.0, Double.NaN);
        assertFalse(r.isNaNRange());
    }
    // #endregion

    // #region hashCode() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: hashCode()
     *
     * #####################################################
     */

    /**
     * Test hashCode() contract: equal ranges must produce equal hash codes.
     * Equivalence Class: Equal ranges (same bounds)
     * Expected: Both hash codes are equal
     */
    @Test
    public void testHashCodeEqualRangesHaveEqualHashCodes() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(1.0, 5.0);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    /**
     * Test hashCode() contract: repeated calls return the same value.
     * Equivalence Class: Same object, multiple invocations
     * Expected: Both invocations return identical hash code
     */
    @Test
    public void testHashCodeConsistency() {
        int hash1 = exampleRange.hashCode();
        int hash2 = exampleRange.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Mutation-focused test with a fixed oracle for hashCode implementation.
     */
    @Test
    public void testHashCodeMatchesExpectedComputation() {
        Range r = new Range(-9.87654321, 1.23456789);

        long tempLower = Double.doubleToLongBits(-9.87654321);
        int expected = (int) (tempLower ^ (tempLower >>> 32));
        long tempUpper = Double.doubleToLongBits(1.23456789);
        expected = 29 * expected + (int) (tempUpper ^ (tempUpper >>> 32));

        assertEquals(expected, r.hashCode());
    }

    /**
     * Mutation-focused test to ensure non-equal ranges do not collapse to same hash.
     */
    @Test
    public void testHashCodeDifferentRangesUsuallyDiffer() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(1.0, 6.0);
        assertFalse(r1.hashCode() == r2.hashCode());
    }

    /**
     * Multi-vector hashCode oracle checks to kill arithmetic/unary mutants.
     */
    @Test
    public void testHashCodeOracleMultipleVectors() {
        double[][] vectors = new double[][] {
                { -0.0, +0.0 },
                { Double.NEGATIVE_INFINITY, -1.0 },
                { -12345.6789, 98765.4321 },
                { 1.0, Double.POSITIVE_INFINITY }
        };

        for (double[] v : vectors) {
            Range r = new Range(v[0], v[1]);
            assertEquals(expectedHash(v[0], v[1]), r.hashCode());
        }
    }
    // #endregion

    // #region toString() tests
    /**
     * #####################################################
     *
     * Test suite for Range class
     * Method: toString()
     *
     * #####################################################
     */

    /**
     * Test toString() with a positive range.
     * Equivalence Class: Positive bounds
     * Expected: Returns "Range[1.0,5.0]"
     */
    @Test
    public void testToString() {
        Range r = new Range(1.0, 5.0);
        assertEquals("Range[1.0,5.0]", r.toString());
    }

    /**
     * Test toString() with a negative range.
     * Equivalence Class: Negative bounds
     * Expected: Returns "Range[-3.0,-1.0]"
     */
    @Test
    public void testToStringNegativeBounds() {
        Range r = new Range(-3.0, -1.0);
        assertEquals("Range[-3.0,-1.0]", r.toString());
    }

    /**
     * Oracle sweep for contains() and constrain() on a finite range.
     */
    @Test
    public void testContainsAndConstrainOracleSweep() {
        Range r = new Range(-3.5, 7.25);
        double[] specials = new double[] {
                Double.NEGATIVE_INFINITY, -100.0, -3.5, -3.5 + 1e-12, -0.0, +0.0,
                7.25 - 1e-12, 7.25, 100.0, Double.POSITIVE_INFINITY, Double.NaN
        };

        for (double v : specials) {
            assertEquals(oracleContains(-3.5, 7.25, v), r.contains(v));
            assertDoubleBitsEqual(oracleConstrain(-3.5, 7.25, v), r.constrain(v));
        }

        for (int i = -200; i <= 200; i++) {
            double v = i / 8.0;
            assertEquals(oracleContains(-3.5, 7.25, v), r.contains(v));
            assertDoubleBitsEqual(oracleConstrain(-3.5, 7.25, v), r.constrain(v));
        }
    }

    /**
     * Oracle sweep for intersects(double,double) across boundary-heavy pairs.
     */
    @Test
    public void testIntersectsOracleSweep() {
        Range r = new Range(-2.0, 3.0);
        double[] values = new double[] {
                Double.NEGATIVE_INFINITY, -3.0, -2.0, -2.0 + 1e-12,
                -1.0, -0.0, +0.0, 2.999999999999, 3.0, 4.0,
                Double.POSITIVE_INFINITY, Double.NaN
        };

        for (double b0 : values) {
            for (double b1 : values) {
                boolean expected = oracleIntersects(-2.0, 3.0, b0, b1);
                assertEquals(expected, r.intersects(b0, b1));
            }
        }
    }

    /**
     * Oracle sweep for combineIgnoringNaN() with mixed NaN/finite/infinite bounds.
     */
    @Test
    public void testCombineIgnoringNaNOracleSweep() {
        Range[] cases = new Range[] {
                new Range(Double.NaN, Double.NaN),
                new Range(Double.NaN, 4.0),
                new Range(-5.0, Double.NaN),
                new Range(-5.0, 4.0),
                new Range(Double.NEGATIVE_INFINITY, -1.0),
                new Range(2.0, Double.POSITIVE_INFINITY)
        };

        for (Range a : cases) {
            for (Range b : cases) {
                Range expected = oracleCombineIgnoringNaN(a, b);
                Range actual = Range.combineIgnoringNaN(a, b);
                if (expected == null) {
                    assertNull(actual);
                } else {
                    assertEquals(expected.getLowerBound(), actual.getLowerBound(), 0.0);
                    assertEquals(expected.getUpperBound(), actual.getUpperBound(), 0.0);
                }
            }
        }
    }

    /**
     * Oracle sweep for shift(base, delta, false) including zero-crossing edges.
     */
    @Test
    public void testShiftNoZeroCrossingOracleSweep() {
        Range[] bases = new Range[] {
                new Range(-3.0, -1.0),
                new Range(-1.0, 1.0),
                new Range(0.0, 0.0),
                new Range(1e-12, 2e-12),
                new Range(1.0, 3.0)
        };
        double[] deltas = new double[] { -5.0, -1.0, -1e-12, 0.0, 1e-12, 1.0, 5.0 };

        for (Range base : bases) {
            for (double delta : deltas) {
                Range expected = oracleShiftNoZeroCrossing(base, delta);
                Range actual = Range.shift(base, delta, false);
                assertEquals(expected.getLowerBound(), actual.getLowerBound(), 0.0);
                assertEquals(expected.getUpperBound(), actual.getUpperBound(), 0.0);
            }
        }
    }

    /**
     * Direct tests for private min(double,double) via reflection.
     */
    @Test
    public void testPrivateMinOracleViaReflection() throws Exception {
        assertDoubleBitsEqual(2.0, invokePrivateMin(Double.NaN, 2.0));
        assertDoubleBitsEqual(1.0, invokePrivateMin(1.0, Double.NaN));
        assertDoubleBitsEqual(-5.0, invokePrivateMin(-5.0, 3.0));
        assertDoubleBitsEqual(-7.0, invokePrivateMin(-6.0, -7.0));
        assertDoubleBitsEqual(Double.NEGATIVE_INFINITY,
                invokePrivateMin(Double.NEGATIVE_INFINITY, 3.0));
    }

    /**
     * Direct tests for private max(double,double) via reflection.
     */
    @Test
    public void testPrivateMaxOracleViaReflection() throws Exception {
        assertDoubleBitsEqual(2.0, invokePrivateMax(Double.NaN, 2.0));
        assertDoubleBitsEqual(1.0, invokePrivateMax(1.0, Double.NaN));
        assertDoubleBitsEqual(3.0, invokePrivateMax(-5.0, 3.0));
        assertDoubleBitsEqual(-6.0, invokePrivateMax(-6.0, -7.0));
        assertDoubleBitsEqual(Double.POSITIVE_INFINITY,
                invokePrivateMax(Double.POSITIVE_INFINITY, 3.0));
    }

    /**
     * Direct tests for private shiftWithNoZeroCrossing(double,double) via
     * reflection.
     */
    @Test
    public void testPrivateShiftWithNoZeroCrossingOracleViaReflection() throws Exception {
        assertDoubleBitsEqual(5.0, invokePrivateShiftWithNoZeroCrossing(3.0, 2.0));
        assertDoubleBitsEqual(0.0, invokePrivateShiftWithNoZeroCrossing(1.0, -2.0));
        assertDoubleBitsEqual(-5.0, invokePrivateShiftWithNoZeroCrossing(-3.0, -2.0));
        assertDoubleBitsEqual(0.0, invokePrivateShiftWithNoZeroCrossing(-1.0, 2.0));
        assertDoubleBitsEqual(-1.0, invokePrivateShiftWithNoZeroCrossing(0.0, -1.0));
        assertDoubleBitsEqual(1.0, invokePrivateShiftWithNoZeroCrossing(-0.0, 1.0));
    }

    /**
     * Sweep for private shiftWithNoZeroCrossing to stress unary/constant mutants.
     */
    @Test
    public void testPrivateShiftWithNoZeroCrossingSweepViaReflection() throws Exception {
        double[] values = new double[] { -5.0, -1.0, -1e-12, -0.0, +0.0, 1e-12, 1.0, 5.0 };
        double[] deltas = new double[] { -3.0, -1.0, -1e-12, 0.0, 1e-12, 1.0, 3.0 };

        for (double value : values) {
            for (double delta : deltas) {
                assertDoubleBitsEqual(
                        oracleShiftWithNoZeroCrossing(value, delta),
                        invokePrivateShiftWithNoZeroCrossing(value, delta));
            }
        }
    }
    // #endregion

    private static boolean oracleContains(double lower, double upper, double value) {
        return value >= lower && value <= upper;
    }

    private static double oracleConstrain(double lower, double upper, double value) {
        double result = value;
        if (!(value >= lower && value <= upper)) {
            if (value > upper) {
                result = upper;
            } else if (value < lower) {
                result = lower;
            }
        }
        return result;
    }

    private static boolean oracleIntersects(double lower, double upper, double b0, double b1) {
        if (b0 <= lower) {
            return b1 > lower;
        }
        return b0 < upper && b1 >= b0;
    }

    private static Range oracleCombineIgnoringNaN(Range r1, Range r2) {
        if (r1 == null) {
            if (r2 != null && r2.isNaNRange()) {
                return null;
            }
            return r2;
        }
        if (r2 == null) {
            if (r1.isNaNRange()) {
                return null;
            }
            return r1;
        }

        double l = oracleMinNaNIgnoring(r1.getLowerBound(), r2.getLowerBound());
        double u = oracleMaxNaNIgnoring(r1.getUpperBound(), r2.getUpperBound());
        if (Double.isNaN(l) && Double.isNaN(u)) {
            return null;
        }
        return new Range(l, u);
    }

    private static double oracleMinNaNIgnoring(double d1, double d2) {
        if (Double.isNaN(d1)) {
            return d2;
        }
        if (Double.isNaN(d2)) {
            return d1;
        }
        return Math.min(d1, d2);
    }

    private static double oracleMaxNaNIgnoring(double d1, double d2) {
        if (Double.isNaN(d1)) {
            return d2;
        }
        if (Double.isNaN(d2)) {
            return d1;
        }
        return Math.max(d1, d2);
    }

    private static Range oracleShiftNoZeroCrossing(Range base, double delta) {
        return new Range(
                oracleShiftWithNoZeroCrossing(base.getLowerBound(), delta),
                oracleShiftWithNoZeroCrossing(base.getUpperBound(), delta));
    }

    private static double oracleShiftWithNoZeroCrossing(double value, double delta) {
        if (value > 0.0) {
            return Math.max(value + delta, 0.0);
        }
        if (value < 0.0) {
            return Math.min(value + delta, 0.0);
        }
        return value + delta;
    }

    private static void assertDoubleBitsEqual(double expected, double actual) {
        assertEquals(Double.doubleToLongBits(expected), Double.doubleToLongBits(actual));
    }

    private static int expectedHash(double lower, double upper) {
        long tempLower = Double.doubleToLongBits(lower);
        int result = (int) (tempLower ^ (tempLower >>> 32));
        long tempUpper = Double.doubleToLongBits(upper);
        return 29 * result + (int) (tempUpper ^ (tempUpper >>> 32));
    }

    private static double invokePrivateMin(double d1, double d2)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = Range.class.getDeclaredMethod("min", double.class, double.class);
        m.setAccessible(true);
        return ((Double) m.invoke(null, d1, d2)).doubleValue();
    }

    private static double invokePrivateMax(double d1, double d2)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = Range.class.getDeclaredMethod("max", double.class, double.class);
        m.setAccessible(true);
        return ((Double) m.invoke(null, d1, d2)).doubleValue();
    }

    private static double invokePrivateShiftWithNoZeroCrossing(double value, double delta)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = Range.class.getDeclaredMethod("shiftWithNoZeroCrossing", double.class, double.class);
        m.setAccessible(true);
        return ((Double) m.invoke(null, value, delta)).doubleValue();
    }
}
