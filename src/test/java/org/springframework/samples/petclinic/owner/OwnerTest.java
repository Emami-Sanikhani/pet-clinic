package org.springframework.samples.petclinic.owner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

@RunWith(Theories.class)
public class OwnerTest {

	@DataPoints
	public static String[] petNames = {"Leo", "Basil", "JewelRosy", "Iggy", "George"};

	private Owner owner;
	private Set<Pet> petsData;


	private void setFieldValue(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
		Field field = this.owner.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(this.owner, value);
	}

	private Object getFieldValue(String fieldName) throws NoSuchFieldException, IllegalAccessException {
		Field field = this.owner.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(this.owner);
	}

	private Set<Pet> buildPetsTestData() {
		Pet pet1 = new Pet();
		pet1.setName("pet1");
		Pet pet2 = new Pet();
		pet2.setName("pet2");
		return new HashSet<>(Arrays.asList(pet1, pet2));
	}

	@Before
	public void setup() throws NoSuchFieldException, IllegalAccessException {
		this.owner = new Owner();
		Set<Pet> pets = new HashSet<>();
		for (String pName : petNames) {
			Pet pet = new Pet();
			pet.setName(pName);
			pets.add(pet);
		}
		this.petsData = pets;
		setFieldValue("pets", new HashSet<>(this.petsData));
	}

	@Test
	public void testGetAddress() throws NoSuchFieldException, IllegalAccessException {
		String addressTestName = "Getter Dummy Address";
		setFieldValue("address", addressTestName);
		assertEquals(
			"conflict in field 'address' by 'getAddress' method",
			addressTestName,
			this.owner.getAddress()    // Method Under Test
		);
	}

	@Test
	public void testSetAddress() throws NoSuchFieldException, IllegalAccessException {
		String addressTestName = "Setter Dummy Address";

		// Method Under Test
		this.owner.setAddress(addressTestName);

		assertEquals(
			"conflict in field 'address' by 'setAddress' method",
			addressTestName,
			getFieldValue("address")
		);
	}

	@Test
	public void testGetCity() throws NoSuchFieldException, IllegalAccessException {
		String cityTestName = "Getter Dummy City";
		setFieldValue("city", cityTestName);
		assertEquals(
			"conflict in field 'city' by 'getCity' method",
			cityTestName,
			this.owner.getCity()    // Method Under Test
		);
	}

	@Test
	public void testSetCity() throws NoSuchFieldException, IllegalAccessException {
		String cityTestName = "Setter Dummy City";

		// Method Under Test
		this.owner.setCity(cityTestName);

		assertEquals(
			"conflict in field 'city' by 'setCity' method",
			cityTestName,
			getFieldValue("city")
		);
	}

	@Test
	public void testGetTelephone() throws NoSuchFieldException, IllegalAccessException {
		String telephoneTestValue = "09123456789";
		setFieldValue("telephone", telephoneTestValue);
		assertEquals(
			"conflict in field 'telephone' by 'getTelephone' method",
			telephoneTestValue,
			this.owner.getTelephone()    // Method Under Test
		);
	}

	@Test
	public void testSetTelephone() throws NoSuchFieldException, IllegalAccessException {
		String telephoneTestValue = "09123456789";

		// Method Under Test
		this.owner.setTelephone(telephoneTestValue);

		assertEquals(
			"conflict in field 'telephone' by 'setTelephone' method",
			telephoneTestValue,
			getFieldValue("telephone")
		);
	}

	@Test
	public void testGetPetsInternal() {
		assertEquals(
			"conflict in field 'pets' by 'getPetsInternal' method",
			this.petsData,
			this.owner.getPetsInternal()    // Method Under Test
		);
	}

	@Test
	public void testSetPetsInternal() throws NoSuchFieldException, IllegalAccessException {
		// Method Under Test
		this.owner.setPetsInternal(this.petsData);

		assertEquals(
			"conflict in field 'pets' by 'setPetsInternal' method",
			this.petsData,
			getFieldValue("pets")
		);
	}

	@Test
	public void testAddPet() throws NoSuchFieldException, IllegalAccessException {
		Pet newPet = new Pet();
		newPet.setName("new pet name");

		// Method Under Test
		this.owner.addPet(newPet);

		Set<Pet> ownerPets = (Set<Pet>) getFieldValue("pets");
		assertEquals(this.petsData.size() + 1, ownerPets.size());
		Set<Pet> expected = new HashSet<>(this.petsData);
		expected.add(newPet);
		assertEquals(expected, ownerPets);
	}

	@Test
	public void testGetPets() {

		// Method Under Test
		List<Pet> gottenPets = this.owner.getPets();

		for (Pet p : gottenPets) {
			assertTrue(this.petsData.contains(p));
		}

		for (Pet p : this.petsData) {
			assertTrue(gottenPets.contains(p));
		}

		assertEquals(this.petsData.size(), gottenPets.size());
	}

	@Test
	public void testRemovePet() throws NoSuchFieldException, IllegalAccessException {
		Pet removedPet = (Pet) this.petsData.toArray()[0];
		String removedPetName = removedPet.getName();

		// Method Under Test
		this.owner.removePet(removedPet);

		for (Pet pet : (Set<Pet>) getFieldValue("pets")) {
			String name = pet.getName();
			assertNotEquals(
				String.format("pet with name %s is not removed by 'removePet' method", removedPetName),
				removedPetName,
				name
			);
		}
	}

	@Test
	public void testGetPet() {

		List<Pet> petList = new ArrayList<>(this.petsData);
		Pet firstPet = petList.get(0);
		Pet secondPet = petList.get(1);

		Pet gottenPet = this.owner.getPet(firstPet.getName());
		assertEquals(firstPet.getName(), gottenPet.getName());
		gottenPet = this.owner.getPet(secondPet.getName());
		assertEquals(secondPet.getName(), gottenPet.getName());
		gottenPet = this.owner.getPet(firstPet.getName(), true);
		assertNull(gottenPet);
		gottenPet = this.owner.getPet("non-existing pet name");
		assertNull(gottenPet);
	}

	@Theory
	public void testTheoryGetPet(String petName) throws NoSuchFieldException, IllegalAccessException {
		// Assumptions
		assumeTrue(petName != null);
		Set<Pet> pets = (Set<Pet>) getFieldValue("pets");
		boolean hasPet = false;
		for (Pet p : pets) {
			if (p.getName().equals(petName)) {
				hasPet = true;
				break;
			}
		}
		assumeTrue(hasPet);

		// Act
		Pet gottenPet = this.owner.getPet(petName);

		// Assertions
		assertEquals(petName, gottenPet.getName());
	}

	@After
	public void teardown() {
		this.owner = null;
		this.petsData = null;
	}
}
