package org.springframework.samples.petclinic.owner;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PetManagerTest {

	private static final String[] petTypeNames = {"dog", "cat", "parrot"};
	private static final LocalDate[] visitTimes = {
		LocalDate.of(2000, 10, 13),
		LocalDate.of(2012, 5, 5),
		LocalDate.of(2018, 12, 19),
		LocalDate.of(2020, 2, 1),
		LocalDate.now(),
		LocalDate.now().minusWeeks(3),
		LocalDate.of(2021, 10, 18),
		LocalDate.of(2015, 1, 29),
		LocalDate.of(2014, 5, 20),
		LocalDate.of(2010, 9, 11)
	};
	private static final String[] petNames = {"Leo", "Basil", "JewelRosy", "Iggy", "George", "Freddy"};
	private static final String[] ownerNames = {"Saman", "Mahan", "Poorya", "Kianoosh"};

	private static List<PetType> petTypeList;
	private static List<Visit> visitList;
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

	public void initializeVisits() {
		visitList = new ArrayList<>();
		for (LocalDate ld : visitTimes) {
			Visit v = new Visit();
			v.setDate(ld);
			visitList.add(v);
		}
	}

	public void initializePets() {
		petList = new ArrayList<>();
		int i = 0;
		for (String s : petNames) {
			Pet p = spy(new Pet());
			p.setName(s);
			p.setType(petTypeList.get(i % petTypeList.size()));
			petList.add(p);
			i++;
		}
		petList.get(0).addVisit(visitList.get(0));
		petList.get(0).addVisit(visitList.get(1));
		petList.get(1).addVisit(visitList.get(2));
		petList.get(1).addVisit(visitList.get(3));
		petList.get(2).addVisit(visitList.get(4));
		petList.get(3).addVisit(visitList.get(5));
		petList.get(3).addVisit(visitList.get(6));
		petList.get(3).addVisit(visitList.get(7));
		petList.get(4).addVisit(visitList.get(8));
		petList.get(4).addVisit(visitList.get(9));
	}

	public void initializeOwners() {
		ownerList = new ArrayList<>();
		for (String s : ownerNames) {
			Owner o = spy(new Owner());
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
		initializeVisits();
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

	// Mock(Spy)
	// Behavior Verification
	@Test
	public void testBehavioralFindPet() {
		for (Pet p : petList) {
			clearInvocations(pets, log);

			// Act
			petManager.findPet(p.getId());

			// Verification
			verify(pets).get(p.getId());
			verify(log).info(anyString(), anyInt());
		}
	}

	// *Part 3*
	// State Verification
	@Test
	public void testStateFindPet() {
		for (Pet p : petList) {
			clearInvocations(pets, log);

			// Act
			Pet actualPet = petManager.findPet(p.getId());

			// Assertion
			assertEquals(actualPet, p);
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

	// Mock(Spy)
	// Behavior Verification
	@Test
	public void testBehavioralFindOwner() {
		for (Owner o : ownerList) {
			clearInvocations(owners, log);

			// Act
			petManager.findOwner(o.getId());

			// Verification
			verify(owners).findById(o.getId());
			verify(log).info(anyString(), anyInt());
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

	// Mock, Spy
	// Behavior Verification
	@Test
	public void testBehavioralNewPet() {
		for (Owner o : ownerList) {
			clearInvocations(log, o);

			// Act
			petManager.newPet(o);

			// Verification
			verify(o).addPet(any());
			verify(log).info(anyString(), anyInt());
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

	// Mock, Spy
	// Behavior Verification
	@Test
	public void testBehavioralSaveNewPet() {
		for (Owner o : ownerList) {
			clearInvocations(log, pets, o);

			// Act
			Pet p = new Pet();
			petManager.savePet(p, o);

			// Verification
			verify(o).addPet(p);
			verify(pets).save(p);
			verify(log).info(anyString(), nullable(Integer.class));
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

	// Mock, Spy
	// Behavior Verification
	@Test
	public void testBehavioralSaveOldPet() {
		for (Owner o : ownerList) {
			Set<Pet> petsBefore = new HashSet<>(o.getPets());
			if (petsBefore.isEmpty()) continue;
			Pet oldPet = petsBefore.stream().findFirst().get();
			oldPet.setOwner(null);

			clearInvocations(log, pets, o);

			// Act
			petManager.savePet(oldPet, o);

			// Verification
			verify(o).addPet(oldPet);
			verify(pets).save(oldPet);
			verify(log).info(anyString(), anyInt());
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

	// Mock, Spy
	// Behavior Verification
	@Test
	public void testBehavioralGetOwnerPets() {
		for (Owner o : ownerList) {
			clearInvocations(owners, log, o);

			// Act
			petManager.getOwnerPets(o.getId());

			// Verification
			verify(o).getPets();
			verify(owners).findById(o.getId());
			verify(log, atLeast(1)).info(anyString(), anyInt());
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

	// Mock, Spy
	// Behavior Verification
	@Test
	public void testBehavioralGetOwnerPetTypes() {
		for (Owner o : ownerList) {
			for (Pet p : o.getPets())
				clearInvocations(p);
			clearInvocations(owners, log, o);

			// Act
			petManager.getOwnerPetTypes(o.getId());

			// Verification
			verify(owners).findById(o.getId());
			verify(o).getPets();
			for (Pet p : o.getPets()) {
				verify(p).getType();
			}
			verify(log, atLeast(1)).info(anyString(), anyInt());
		}
	}

	// Mock
	// State Verification
	@Test
	public void testGetVisitsBetween() {
		LocalDate start = LocalDate.of(2020, 1, 1);
		LocalDate end = LocalDate.of(2022, 1, 1);
		for (Pet p : petList) {
			// Assume this method works properly for Pet class
			List<Visit> expected = p.getVisitsBetween(start, end);

			// Act
			List<Visit> actual = petManager.getVisitsBetween(p.getId(), start, end);

			// Assertion
			assertEquals(expected.size(), actual.size());
			assertEquals(new HashSet<>(expected), new HashSet<>(actual));
		}
	}

	// Mock, Spy
	// Behavior Verification
	@Test
	public void testBehavioralGetVisitsBetween() {
		LocalDate start = LocalDate.of(2000, 1, 1);
		LocalDate end = LocalDate.of(2022, 1, 1);
		for (Pet p : petList) {
			clearInvocations(pets, log, p);

			// Act
			petManager.getVisitsBetween(p.getId(), start, end);

			// Verification
			verify(pets).get(p.getId());
			verify(p).getVisitsBetween(start, end);
			verify(log).info(anyString(), anyInt(), any(LocalDate.class), any(LocalDate.class));

		}
	}
}
