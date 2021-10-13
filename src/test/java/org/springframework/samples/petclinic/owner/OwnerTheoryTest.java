package org.springframework.samples.petclinic.owner;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

@RunWith(Theories.class)
public class OwnerTheoryTest {

	private Owner owner;

	@Before
	public void setup() throws NoSuchFieldException, IllegalAccessException {
		this.owner = new Owner();
		List<Pet> petsList = new ArrayList<>();
		for (String pName : petNames) {
			Pet pet = new Pet();
			pet.setName(pName);
			petsList.add(pet);
		}
		Set<Pet> pets = new HashSet<>(petsList);
		Field petsField = this.owner.getClass().getDeclaredField("pets");
		petsField.setAccessible(true);
		petsField.set(this.owner, pets);
	}

	@DataPoints
	public static String[] petNames = {"Leo", "Basil", "JewelRosy", "Iggy", "George"};

	@Theory
	public void testGetPet(String petName) {
		assumeTrue(petName != null);

		Pet gottenPet = this.owner.getPet(petName);

		assertEquals(petName, gottenPet.getName());

	}

	@After
	public void teardown() {
		this.owner = null;
	}

}

