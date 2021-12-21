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

		when(p1.getBirthDate()).thenReturn(LocalDate.of(2010, 1, 1));
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
	public void calcPriceWithAllPetTypesTest() {
		List<Pet> pets = new ArrayList<>(Arrays.asList(p1, p2, p3));
		assertEquals(8160.0, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}

	@Test
	public void calcPriceWithHighDiscountTest() {
		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < 6; i++)
			pets.add(p2);
		assertEquals(9.745544E7, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}

	@Test
	public void calcPriceWithP1Test() {
		List<Pet> pets = new ArrayList<>();
		for(int i = 0; i < 10; i++)
			pets.add(p1);
		assertEquals(46600.0, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}

	@Test
	public void calcPriceWithP3Test() {
		List<Pet> pets = new ArrayList<>();
		for(int i = 0; i < 10; i++)
			pets.add(p3);
		assertEquals(4590200.0, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}

	@Test
	public void calcPriceWithP1P3Test() {
		List<Pet> pets = new ArrayList<>(Arrays.asList(p1, p3, p3, p1, p3, p1, p3, p1, p1, p3));
		assertEquals(4590200.0, PriceCalculator.calcPrice(pets, 1000, 2000), 0);
	}
}
