package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(Theories.class)
public class PetTest {

	private Pet pet;

	@DataPoints
	public static Visit[] visitSet1() {
		Visit v1 = new Visit();
		Visit v2 = new Visit();
		Visit v3 = new Visit();
		v1.setDescription("first visit from visitSet1");
		v2.setDescription("second visit from visitSet1");
		v3.setDescription("third visit from visitSet1");
		return new Visit[]{
			v1.setDate(LocalDate.of(2000, 6, 14)),
			v2.setDate(LocalDate.of(2001, 4, 29)),
			v3.setDate(LocalDate.of(1999, 1, 1))
		};
	}

	@DataPoints
	public static Visit[] visitSet2() {
		Visit v1 = new Visit();
		Visit v2 = new Visit();
		Visit v3 = new Visit();
		v1.setDescription("first visit from visitSet2");
		v2.setDescription("second visit from visitSet2");
		v3.setDescription("third visit from visitSet2");
		return new Visit[]{
			v1.setDate(LocalDate.of(2010, 7, 18)),
			v2.setDate(LocalDate.of(2013, 8, 8)),
			v3.setDate(LocalDate.of(2015, 2, 28))
		};
	}

	@DataPoints
	public static Visit[] visitSet3() {
		Visit v1 = new Visit();
		Visit v2 = new Visit();
		Visit v3 = new Visit();
		v1.setDescription("first visit from visitSet3");
		v2.setDescription("second visit from visitSet3");
		v3.setDescription("third visit from visitSet3");
		return new Visit[]{
			v1.setDate(LocalDate.of(2020, 10, 8)),
			v2.setDate(LocalDate.of(2022, 1, 16)),
			v3.setDate(LocalDate.of(2021, 6, 6)),
			null
		};
	}

	@Before
	public void setup() {
		this.pet = new Pet();
	}

	@Theory
	public void testGetVisits(Visit v1, Visit v2, Visit v3) throws NoSuchFieldException, IllegalAccessException {
		Set<Visit> visits = new HashSet<>(Arrays.asList(v1, v2, v3));

		// Assumptions
		assumeFalse(visits.contains(null));
		assumeTrue(
			!v1.getDescription().equals(v2.getDescription())
				&& !v2.getDescription().equals(v3.getDescription())
				&& !v1.getDescription().equals(v3.getDescription())
		);

		Field visitsField = this.pet.getClass().getDeclaredField("visits");
		visitsField.setAccessible(true);
		visitsField.set(this.pet, visits);

		// Act
		List<Visit> gottenVisits = this.pet.getVisits();

		// Assertions
		assertEquals(visits.size(), gottenVisits.size());

		for (Visit v : visits) {
			assertTrue(gottenVisits.contains(v));
		}

		for (Visit v : gottenVisits) {
			assertTrue(visits.contains(v));
		}

	}
}
