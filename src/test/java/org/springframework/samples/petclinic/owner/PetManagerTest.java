package org.springframework.samples.petclinic.owner;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PetManagerTest {

	private static final String[] petTypeNames = {"dog", "cat", "parrot"};
	private static final String[] petNames = {"Leo", "Basil", "JewelRosy", "Iggy", "George", "Freddy"};
	private static final String[] ownerNames = {"Saman", "Mahan", "Poorya", "Kianoosh"};

	private static List<PetType> petTypeList;
	private static List<Pet> petList;
	private static List<Owner> ownerList;

	@Mock
	private PetTimedCache pets;
	@Mock
	private OwnerRepository owners;
	@Mock
	private Logger log;

	@InjectMocks
	private PetManager petManager;

	public void initializePetTypes() {
		petTypeList = new ArrayList<>();
		for (String s : petTypeNames) {
			PetType pt = new PetType();
			pt.setName(s);
			petTypeList.add(pt);
		}
	}

	public void initializePets() {
		petList = new ArrayList<>();
		int i = 0;
		for (String s : petNames) {
			Pet p = new Pet();
			p.setName(s);
			p.setType(petTypeList.get(i % petTypeList.size()));
			petList.add(p);
			i++;
		}
	}

	public void initializeOwners() {
		ownerList = new ArrayList<>();
		for (String s : ownerNames) {
			Owner o = new Owner();
			o.setFirstName(s);
			ownerList.add(o);
		}
		Owner owner0 = ownerList.get(0);
		owner0.addPet(petList.get(0));
		owner0.addPet(petList.get(1));
		owner0.addPet(petList.get(2));
		Owner owner1 = ownerList.get(1);
		owner1.addPet(petList.get(3));
		owner1.addPet(petList.get(4));
		Owner owner2 = ownerList.get(2);
		owner2.addPet(petList.get(5));
	}


	@Before
	public void setup() {
		initializePetTypes();
		initializePets();
		initializeOwners();
		for (int i = 0; i < petList.size(); i++) {
			Pet p = petList.get(i);
			p.setId(i);
			given(pets.get(i)).willReturn(p);
		}
		for (int i = 0; i < ownerList.size(); i++) {
			Owner o = ownerList.get(i);
			o.setId(i);
			given(owners.findById(i)).willReturn(o);
		}
	}

	// Mock
	// State Verification
	@Test
	public void testFindPet() {
		for (int i = 0; i < petList.size(); i++) {
			Pet expected = petList.get(i);

			// Act
			Pet actual = petManager.findPet(i);

			// Assertion
			assertNotNull(actual);
			assertEquals(expected, actual);
		}
	}

	// Mock
	// State Verification
	@Test
	public void testFindOwner() {
		for (int i = 0; i < ownerList.size(); i++) {
			Owner expected = ownerList.get(i);

			// Act
			Owner actual = petManager.findOwner(i);

			// Assertion
			assertNotNull(actual);
			assertEquals(expected, actual);
		}
	}

	// Mock
	// State Verification
	@Test
	public void testNewPet() {
		for (Owner o : ownerList) {
			List<Pet> petsBefore = o.getPets();

			// Act
			Pet newPet = petManager.newPet(o);

			// Assertion
			assertNotNull(newPet);
			assertNull(newPet.getName());
			assertEquals(o, newPet.getOwner());

			List<Pet> differences = new ArrayList<>(o.getPets());
			differences.removeAll(petsBefore);
			assertEquals(1, differences.size());
			assertEquals(newPet, differences.get(0));
		}
	}

	// Mock
	// State Verification
	@Test
	public void testSaveNewPet() {
		for (Owner o : ownerList) {
			List<Pet> petsBefore = o.getPets();
			Pet newPet = new Pet();

			// Act
			petManager.savePet(newPet, o);

			// Assertion
			List<Pet> differences = new ArrayList<>(o.getPets());
			differences.removeAll(petsBefore);
			assertEquals(1, differences.size());
			assertEquals(newPet, differences.get(0));
		}
	}

	// Mock
	// State Verification
	@Test
	public void testSaveOldPet() {
		for (Owner o : ownerList) {
			Set<Pet> petsBefore = new HashSet<>(o.getPets());
			if (petsBefore.isEmpty()) continue;
			Pet oldPet = petsBefore.stream().findFirst().get();
			oldPet.setOwner(null);

			// Act
			petManager.savePet(oldPet, o);

			// Assertion
			Set<Pet> petsAfter = new HashSet<>(o.getPets());
			assertEquals(petsBefore, petsAfter);
			assertNotNull(oldPet.getOwner());
			assertEquals(o, oldPet.getOwner());
		}
	}

	// Mock
	// State Verification
	@Test
	public void testGetOwnerPets() {
		for (int i = 0; i < ownerList.size(); i++) {
			List<Pet> expected = ownerList.get(i).getPets();
			Set<Pet> expectedSet = new HashSet<>(expected);

			// Act
			List<Pet> actual = petManager.getOwnerPets(i);

			// Assertion
			Set<Pet> actualSet = new HashSet<>(actual);
			assertEquals(expected.size(), actual.size());
			assertEquals(expectedSet, actualSet);
		}
	}

	// Mock
	// State Verification
	@Test
	public void testGetOwnerPetTypes() {
		for (int i = 0; i < ownerList.size(); i++) {
			Owner o = ownerList.get(i);
			Set<PetType> expected = o.getPets().stream().map(Pet::getType).collect(Collectors.toSet());

			// Act
			Set<PetType> actual = petManager.getOwnerPetTypes(i);

			// Assertion
			assertEquals(expected, actual);
		}
	}

	// Mock
	// State Verification
	@Test
	public void testGetVisitsBetween() {
		fail("not implemented test");
	}
}
