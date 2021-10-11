package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

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

	@BeforeEach
	void setup() {
		this.owner = new Owner();
	}

	@Test
	void testGetAddress() throws NoSuchFieldException, IllegalAccessException {
		String addressTestName = "Getter Dummy Address";
		setFieldValue("address", addressTestName);
		assertEquals(
			addressTestName,
			this.owner.getAddress(),    // Method Under Test
			"conflict in field 'address' by 'getAddress' method"
		);
	}

	@Test
	void testSetAddress() throws NoSuchFieldException, IllegalAccessException {
		String addressTestName = "Setter Dummy Address";

		// Method Under Test
		this.owner.setAddress(addressTestName);

		assertEquals(
			addressTestName,
			getFieldValue("address"),
			"conflict in field 'address' by 'setAddress' method"
		);
	}

	@Test
	void testGetCity() throws NoSuchFieldException, IllegalAccessException {
		String cityTestName = "Getter Dummy City";
		setFieldValue("city", cityTestName);
		assertEquals(
			cityTestName,
			// Method Under Test
			this.owner.getCity(),    // Method Under Test
			"conflict in field 'city' by 'getCity' method"
		);
	}

	@Test
	void testSetCity() throws NoSuchFieldException, IllegalAccessException {
		String cityTestName = "Setter Dummy City";

		// Method Under Test
		this.owner.setCity(cityTestName);

		assertEquals(
			cityTestName,
			getFieldValue("city"),
			"conflict in field 'city' by 'setCity' method"
		);
	}

	@Test
	void testGetTelephone() throws NoSuchFieldException, IllegalAccessException {
		String telephoneTestValue = "09123456789";
		setFieldValue("telephone", telephoneTestValue);
		assertEquals(
			telephoneTestValue,
			this.owner.getTelephone(),    // Method Under Test
			"conflict in field 'telephone' by 'getTelephone' method"
		);
	}

	@Test
	void testSetTelephone() throws NoSuchFieldException, IllegalAccessException {
		String telephoneTestValue = "09123456789";

		// Method Under Test
		this.owner.setTelephone(telephoneTestValue);

		assertEquals(
			telephoneTestValue,
			getFieldValue("telephone"),
			"conflict in field 'telephone' by 'setTelephone' method"
		);
	}

	@Test
	void testGetPetsInternal() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> petsInternalTestValue = buildPetsTestData();
		setFieldValue("pets", petsInternalTestValue);
		assertEquals(
			petsInternalTestValue,
			this.owner.getPetsInternal(),    // Method Under Test
			"conflict in field 'pets' by 'getPetsInternal' method"
		);
	}

	@Test
	void testSetPetsInternal() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> petsInternalTestValue = buildPetsTestData();

		// Method Under Test
		this.owner.setPetsInternal(petsInternalTestValue);

		assertEquals(
			petsInternalTestValue,
			getFieldValue("pets"),
			"conflict in field 'pets' by 'setPetsInternal' method"
		);
	}

	@Test
	void testRemovePet() throws NoSuchFieldException, IllegalAccessException {
		Set<Pet> removePetTestValue = buildPetsTestData();
		setFieldValue("pets", removePetTestValue);
		Pet removedPet = (Pet) removePetTestValue.toArray()[0];
		String removedPetName = removedPet.getName();

		// Method Under Test
		this.owner.removePet(removedPet);

		for (Pet pet : (Set<Pet>) getFieldValue("pets")) {
			String name = pet.getName();
			assertNotEquals(
				removedPetName,
				name,
				String.format("pet with name %s is not removed by 'removePet' method", removedPetName)
			);
		}
	}

	@Test
	void testGetPet() throws NoSuchFieldException, IllegalAccessException {
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

	@AfterEach
	void teardown() {
		this.owner = null;
	}
}
