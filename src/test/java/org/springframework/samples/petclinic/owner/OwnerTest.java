package org.springframework.samples.petclinic.owner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class OwnerTest {

	private Owner owner = null;


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

//	private static Field getField(Class<?> type, String name) throws NoSuchFieldException {
//		try {
//			return type.getDeclaredField(name);
//		} catch (NoSuchFieldException e) {
//			if (type.getSuperclass() != null) {
//				return getField(type.getSuperclass(), name);
//			}
//		}
//		throw new NoSuchFieldException(name);
//	}

	@Before
	public void setup() {
		this.owner = new Owner();
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
	public void testGetPetsInternal() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> petsInternalTestValue = buildPetsTestData();
		setFieldValue("pets", petsInternalTestValue);
		assertEquals(
			"conflict in field 'pets' by 'getPetsInternal' method",
			petsInternalTestValue,
			this.owner.getPetsInternal()    // Method Under Test
		);
	}

	@Test
	public void testSetPetsInternal() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> petsInternalTestValue = buildPetsTestData();

		// Method Under Test
		this.owner.setPetsInternal(petsInternalTestValue);

		assertEquals(
			"conflict in field 'pets' by 'setPetsInternal' method",
			petsInternalTestValue,
			getFieldValue("pets")
		);
	}

	@Test
	public void testAddPet() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> addPetTestValue = buildPetsTestData();
		setFieldValue("pets", addPetTestValue);
		Pet newPet = new Pet();
		newPet.setName("pet3");

		// Method Under Test
		this.owner.addPet(newPet);

		Set<Pet> ownerPets = (Set<Pet>) getFieldValue("pets");
		assertEquals(3, ownerPets.size());
		addPetTestValue.add(newPet);
		assertEquals(addPetTestValue, ownerPets);
	}

	@Test
	public void testGetPets() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> getPetsTestValue = buildPetsTestData();
		setFieldValue("pets", getPetsTestValue);

		// Method Under Test
		List<Pet> gottenPets = this.owner.getPets();

		for (Pet p : gottenPets) {
			assertTrue(getPetsTestValue.contains(p));
		}

		for (Pet p : getPetsTestValue) {
			assertTrue(gottenPets.contains(p));
		}

		assertEquals(getPetsTestValue.size(), gottenPets.size());
	}

	@Test
	public void testRemovePet() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> removePetTestValue = buildPetsTestData();
		setFieldValue("pets", removePetTestValue);
		Pet removedPet = (Pet) removePetTestValue.toArray()[0];
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
	public void testGetPet() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> initialPets = buildPetsTestData();
		setFieldValue("pets", initialPets);

		List<Pet> petList = new ArrayList<>(initialPets);
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

	@After
	public void teardown() {
		this.owner = null;
	}
}
