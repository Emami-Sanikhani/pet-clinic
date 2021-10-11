package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

	@BeforeEach
	void setup() {
		this.owner = new Owner();
	}

	@Test
	void testGetAddress() throws NoSuchFieldException, IllegalAccessException{
		String addressTestName = "Getter Dummy Address";
		setFieldValue("address", addressTestName);
		assertEquals(
			addressTestName,
			this.owner.getAddress(),
			"conflict in field 'address' with 'getAddress' method"
		);
	}

	@Test
	void testSetAddress() throws NoSuchFieldException, IllegalAccessException {
		String addressTestName = "Setter Dummy Address";
		this.owner.setAddress(addressTestName);
		assertEquals(
			addressTestName,
			getFieldValue("address"),
			"conflict in field 'address' with 'setAddress' method"
		);
	}

	@Test
	void testGetCity() throws NoSuchFieldException, IllegalAccessException {
		String cityTestName = "Getter Dummy City";
		setFieldValue("city", cityTestName);
		assertEquals(
			cityTestName,
			this.owner.getCity(),
			"conflict in field 'city' with 'getCity' method"
		);
	}

	@Test
	void testSetCity() throws NoSuchFieldException, IllegalAccessException {
		String cityTestName = "Setter Dummy City";
		this.owner.setCity(cityTestName);
		assertEquals(
			cityTestName,
			getFieldValue("city"),
			"conflict in field 'city' with 'setCity' method"
		);
	}

	@Test
	void testGetTelephone() throws NoSuchFieldException, IllegalAccessException {
		String telephoneTestValue = "09123456789";
		setFieldValue("telephone", telephoneTestValue);
		assertEquals(
			telephoneTestValue,
			this.owner.getTelephone(),
			"conflict in field 'telephone' with 'getTelephone' method"
		);
	}

	@Test
	void testSetTelephone() throws NoSuchFieldException, IllegalAccessException {
		String telephoneTestValue = "09123456789";
		this.owner.setTelephone(telephoneTestValue);
		assertEquals(
			telephoneTestValue,
			getFieldValue("telephone"),
			"conflict in field 'telephone' with 'setTelephone' method"
		);
	}

	@Test
	void testGetPetsInternal() throws NoSuchFieldException, IllegalAccessException {
		Pet pet1 = new Pet();
		pet1.setId(1);
		pet1.setName("pet1");
		Pet pet2 = new Pet();
		pet2.setId(2);
		pet2.setName("pet2");
		Set<Pet> petInternalTestValue = new HashSet<>(Arrays.asList(pet1, pet2));
		setFieldValue("pets", petInternalTestValue);
		assertEquals(
			petInternalTestValue,
			this.owner.getPetsInternal(),
			"conflict in field 'pets' with 'getPetsInternal' method"
		);
	}

	@Test
	void testSetPetsInternal() throws NoSuchFieldException, IllegalAccessException {
		Pet pet1 = new Pet();
		pet1.setId(1);
		pet1.setName("pet1");
		Pet pet2 = new Pet();
		pet2.setId(2);
		pet2.setName("pet2");
		Set<Pet> petInternalTestValue = new HashSet<>(Arrays.asList(pet1, pet2));
		this.owner.setPetsInternal(petInternalTestValue);
		assertEquals(
			petInternalTestValue,
			getFieldValue("pets"),
			"conflict in field 'pets' with 'setPetsInternal' method"
		);
	}

	@AfterEach
	void teardown() {
		this.owner = null;
	}
}
