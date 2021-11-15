package org.springframework.samples.petclinic.model.priceCalculators;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerDependentPriceCalculatorTest {

	@Mock
	PetType rarePT, noRarePT;

	Pet rareAgedPet = new Pet();
	Pet rareYoungPet = new Pet();
	Pet noRareAgedPet = new Pet();
	Pet noRareYoungPet = new Pet();

	Date longBefore = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
	Date shortBefore = new GregorianCalendar(LocalDate.now().getYear(), Calendar.JANUARY, 1).getTime();

	List<Pet> pets = new ArrayList<>(Arrays.asList(rareAgedPet, rareYoungPet, noRareAgedPet, noRareYoungPet));

	CustomerDependentPriceCalculator cdp = new CustomerDependentPriceCalculator();

	private List<Pet> buildHighDiscountPets() {
		List<Pet> temp = new ArrayList<>();
		for (int i = 0; i < 10; i++)
			temp.add(this.rareYoungPet);
		return temp;
	}

	@Before
	public void setup() {
		when(this.rarePT.getRare()).thenReturn(true);
		when(this.noRarePT.getRare()).thenReturn(false);
		this.rareAgedPet.setType(this.rarePT);
		this.rareYoungPet.setType(this.rarePT);
		this.noRareAgedPet.setType(this.noRarePT);
		this.noRareYoungPet.setType(this.noRarePT);
		this.rareAgedPet.setBirthDate(this.longBefore);
		this.rareYoungPet.setBirthDate(this.shortBefore);
		this.noRareAgedPet.setBirthDate(this.longBefore);
		this.noRareYoungPet.setBirthDate(this.shortBefore);
	}

	@Test
	public void calcPriceTestForVariationOfPets() {
		assertEquals(10160, cdp.calcPrice(this.pets, 1000, 2000, UserType.NEW), 0);
	}

	@Test
	public void calcPriceTestForGoldUserAndLowDiscount() {
		assertEquals(9128, cdp.calcPrice(this.pets, 1000, 2000, UserType.GOLD), 0);
	}

	@Test
	public void calcPriceTestForHighDiscountAndNewUser() {
		List<Pet> pl = this.buildHighDiscountPets();
		assertEquals(32920, cdp.calcPrice(pl, 1000, 2000, UserType.NEW), 0);
	}

	@Test
	public void calcPriceTestForHighDiscountAndSilverUser() {
		List<Pet> pl = this.buildHighDiscountPets();
		assertEquals(31140, cdp.calcPrice(pl, 1000, 2000, UserType.SILVER), 0);
	}

}
