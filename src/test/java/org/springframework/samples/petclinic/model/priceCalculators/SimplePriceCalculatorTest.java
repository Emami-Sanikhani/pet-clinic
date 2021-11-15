package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimplePriceCalculatorTest {

	@Mock
	PetType pt1, pt2;

	List<Pet> pets = new ArrayList<>();

	SimplePriceCalculator spc = new SimplePriceCalculator();

	@Before
	public void setup() {
		when(pt1.getRare()).thenReturn(true);
		when(pt2.getRare()).thenReturn(false);
		Pet p1 = new Pet();
		Pet p2 = new Pet();
		p1.setType(pt1);
		p2.setType(pt2);
		pets.addAll(Arrays.asList(p1, p2));
	}

	@Test
	public void calcPriceNewUserTest() {
		assertEquals(5130, spc.calcPrice(pets, 1000, 2000, UserType.NEW), 0);
	}

	@Test
	public void calcPriceSilverUserTest() {
		assertEquals(5400, spc.calcPrice(pets, 1000, 2000, UserType.SILVER), 0);
	}
}
