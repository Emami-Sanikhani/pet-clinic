package org.springframework.samples.petclinic.utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceCalculatorTest {

	@Mock
	Pet p1, p2, p3;

	@Mock
	Visit v1, v2;

	@Before
	public void setup() {
		when(v1.getDate()).thenReturn(LocalDate.of(2000, 1, 1));
		when(v2.getDate()).thenReturn(LocalDate.of(LocalDate.now().getYear(), 1, 1));

		when(p1.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
		when(p1.getVisitsUntilAge(anyInt())).thenReturn(new ArrayList<>());
		when(p2.getBirthDate()).thenReturn(LocalDate.of(LocalDate.now().getYear(), 1, 1));
		when(p2.getVisitsUntilAge(anyInt())).thenReturn(Arrays.asList(v1, v1));
		when(p3.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
		List<Visit> p3_visits = new ArrayList<>();
		for(int i = 0; i < 100; i++)
			p3_visits.add(v1);
		for(int i = 0; i < 100; i++)
			p3_visits.add(v2);
		when(p3.getVisitsUntilAge(anyInt())).thenReturn(p3_visits);
	}

	@Test
	public void calcPriceWithNoPetsTest() {
		List<Pet> pets = new ArrayList<>();
		assertEquals(0.0, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}

	@Test
	public void calcPriceWithAllPetTypes() {
		List<Pet> pets = new ArrayList<>(Arrays.asList(p1, p2, p3));
		assertEquals(8160.0, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}

	@Test
	public void calcPriceWithHighDiscount() {
		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < 6; i++)
			pets.add(p2);
		assertEquals(9.745544E7, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}
}

//public class CustomerDependentPriceCalculatorTest {
//
//	@Mock
//	PetType rarePT, noRarePT;
//
//	LocalDate longBefore = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
//	Date shortBefore = new GregorianCalendar(LocalDate.now().getYear(), Calendar.JANUARY, 1).getTime();
//
//	List<Pet> pets = new ArrayList<>(Arrays.asList(rareAgedPet, rareYoungPet, noRareAgedPet, noRareYoungPet));
//
//	CustomerDependentPriceCalculator cdp = new CustomerDependentPriceCalculator();
//
//	private List<Pet> buildHighDiscountPets() {
//		List<Pet> temp = new ArrayList<>();
//		for (int i = 0; i < 10; i++)
//			temp.add(this.rareYoungPet);
//		return temp;
//	}
//
//	@Before
//	public void setup() {
//		when(this.rarePT.getRare()).thenReturn(true);
//		when(this.noRarePT.getRare()).thenReturn(false);
//		this.rareAgedPet.setType(this.rarePT);
//		this.rareYoungPet.setType(this.rarePT);
//		this.noRareAgedPet.setType(this.noRarePT);
//		this.noRareYoungPet.setType(this.noRarePT);
//		this.rareAgedPet.setBirthDate(this.longBefore);
//		this.rareYoungPet.setBirthDate(this.shortBefore);
//		this.noRareAgedPet.setBirthDate(this.longBefore);
//		this.noRareYoungPet.setBirthDate(this.shortBefore);
//	}
//
//	@Test
//	public void calcPriceTestForVariationOfPets() {
//		assertEquals(10160, cdp.calcPrice(this.pets, 1000, 2000, UserType.NEW), 0);
//	}
//
//	@Test
//	public void calcPriceTestForGoldUserAndLowDiscount() {
//		assertEquals(9128, cdp.calcPrice(this.pets, 1000, 2000, UserType.GOLD), 0);
//	}
//
//	@Test
//	public void calcPriceTestForHighDiscountAndNewUser() {
//		List<Pet> pl = this.buildHighDiscountPets();
//		assertEquals(32920, cdp.calcPrice(pl, 1000, 2000, UserType.NEW), 0);
//	}
//
//	@Test
//	public void calcPriceTestForHighDiscountAndSilverUser() {
//		List<Pet> pl = this.buildHighDiscountPets();
//		assertEquals(31140, cdp.calcPrice(pl, 1000, 2000, UserType.SILVER), 0);
//	}
//
//}
