package org.springframework.samples.petclinic.owner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class PetServiceTest {

	private PetService petService;

	private final PetTimedCache petTimedCache = mock(PetTimedCache.class);
	private final OwnerRepository ownerRepository = mock(OwnerRepository.class);
	private final Logger petLogger = mock(Logger.class);

	private static final Pet pet_ID1 = new Pet();
	private static final Pet pet_ID2 = new Pet();
	private static final Pet pet_ID3 = new Pet();

	private final Integer id;
	private final Pet expectedPet;

	public PetServiceTest(Integer id, Pet pet) {
		this.id = id;
		this.expectedPet = pet;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(
			new Object[][]{
				{1, pet_ID1},
				{2, pet_ID2},
				{3, pet_ID3},
				{4, null}
			}
		);
	}

	@Before
	public void setup() {
		pet_ID1.setName("Leo");
		pet_ID2.setName("Basil");
		pet_ID3.setName("JewelRosy");
		given(petTimedCache.get(1)).willReturn(pet_ID1);
		given(petTimedCache.get(2)).willReturn(pet_ID2);
		given(petTimedCache.get(3)).willReturn(pet_ID3);

		this.petService = new PetService(petTimedCache, ownerRepository, petLogger);
	}

	@Test
	public void testConst() {

		// Assumption
		assumeTrue(this.id != null);

		// Act
		Pet gotten = this.petService.findPet(this.id);

		// Assertion
		assertEquals(this.expectedPet, gotten);
	}

	@After
	public void teardown() {
		this.petService = null;
	}
}
