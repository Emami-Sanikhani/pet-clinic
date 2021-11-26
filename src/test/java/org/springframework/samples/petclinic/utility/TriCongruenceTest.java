package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
@ClauseDefinition(clause = 'a', def = "t1arr[0] != t2arr[0]")
@ClauseDefinition(clause = 'b', def = "t1arr[1] != t2arr[1]")
@ClauseDefinition(clause = 'c', def = "t1arr[2] != t2arr[2]")
@ClauseDefinition(clause = 'd', def = "t1arr[0] < 0")
@ClauseDefinition(clause = 'e', def = "t1arr[0] + t1arr[1] < t1arr[2]")
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	@Test
	public void sampleTest() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(7, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	// Predicate in line 14 CUTPNFP: {TFF, FTF, FFT, FFF}

	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "a",
		clause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void Line14PredicateTestFFF1() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(2, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "b",
		clause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void Line14PredicateTestFFF2() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(2, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "c",
		clause = 'c',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void Line14PredicateTestFFF3() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(2, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "a",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void Line14PredicateTestTFF() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(3, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void Line14PredicateTestFTF() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(2, 2, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "c",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void Line14PredicateTestFFT() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(2, 3, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	// Line 15 Predicate CC: {TT, FF}
	// Line 15 Predicate CACC: {TF, FF, FT   OR    TT, FF}
	// So with {TT, FF} we can satisfy CC and CACC

	@ClauseCoverage(
		predicate = "d + e",
		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@Test
	public void Line15PredicateTestTT1() {
		Triangle t1 = new Triangle(-2, 3, 7);
		Triangle t2 = new Triangle(-2, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@CACC(
		predicate = "d + e",
		majorClause = 'd',
		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true)
		},
		predicateValue = true
	)
	@Test
	public void Line15PredicateTestTT2() {
		Triangle t1 = new Triangle(-2, 3, 7);
		Triangle t2 = new Triangle(-2, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@CACC(
		predicate = "d + e",
		majorClause = 'e',
		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true)
		},
		predicateValue = true
	)
	@Test
	public void Line15PredicateTestTT3() {
		Triangle t1 = new Triangle(-2, 3, 7);
		Triangle t2 = new Triangle(-2, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertFalse(areCongruent);
	}

	@ClauseCoverage(
		predicate = "d + e",
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@Test
	public void Line15PredicateTestFF1() {
		Triangle t1 = new Triangle(2, 6, 7);
		Triangle t2 = new Triangle(2, 6, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertTrue(areCongruent);
	}

	@CACC(
		predicate = "d + e",
		majorClause = 'd',
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		},
		predicateValue = true
	)
	@Test
	public void Line15PredicateTestFF2() {
		Triangle t1 = new Triangle(2, 6, 7);
		Triangle t2 = new Triangle(2, 6, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertTrue(areCongruent);
	}

	@CACC(
		predicate = "d + e",
		majorClause = 'e',
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		},
		predicateValue = true
	)
	@Test
	public void Line15PredicateTestFF3() {
		Triangle t1 = new Triangle(2, 6, 7);
		Triangle t2 = new Triangle(2, 6, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		Assertions.assertTrue(areCongruent);
	}

	/**
	 * TODO
	 * explain your answer here
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
//		predicate = a predicate with any number of clauses
		return predicate;
	}
}
