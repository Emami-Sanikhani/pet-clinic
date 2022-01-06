package bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PetServiceSteps {

	PetService petService;
	Owner owner;
	Owner requestedOwner;
	Pet pet;
	Pet requestedPet;

	PetTimedCache pets = mock(PetTimedCache.class);

	OwnerRepository owners = mock(OwnerRepository.class);

	Logger logger = mock(Logger.class);

	@Before("@petService_annotation")
	public void setUp() {
		this.petService = new PetService(this.pets, this.owners, this.logger);
		reset(this.pets, this.owners, this.logger);
	}

	@Given("There is one owner in ownerRepository with id {int} and first name {string} and last name {string}")
	public void thereIsOneOwner(int id, String firstName, String lastName) {
		owner = new Owner();
		owner.setId(id);
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		when(this.owners.findById(id)).thenReturn(owner);
	}

	// .findOwner method
	@When("owner with id {int} is requested")
	public void whenOwnerWithIdIsRequested(int id) {
		this.requestedOwner = this.owners.findById(id);
	}

	@Then("owner first name is {string}")
	public void ownerFirstNameIs(String firstName) {
		assertNotNull(this.requestedOwner);
		assertEquals(this.requestedOwner.getFirstName(), firstName);
	}

	@And("his last name is {string}")
	public void ownerLastNameIs(String lastName) {
		assertEquals(this.requestedOwner.getLastName(), lastName);
	}


	// .findPet method
	@Given("There is a pet with id {int} and name {string} in pets")
	public void thereIsAPetWithIdAndName(int id, String name) {
		pet = new Pet();
		pet.setId(id);
		pet.setName(name);
		when(this.pets.get(id)).thenReturn(pet);
	}

	@When("pet with id {int} is requested")
	public void petWithIdIsRequested(int id) {
		this.requestedPet = petService.findPet(id);
	}

	@Then("returned pet is null")
	public void returnedPetIsNull() {
		assertNull(this.requestedPet);
	}


	// .newPet method
	@When("requested new pet for existing owner")
	public void requestedNewPet() {
		petService.newPet(this.owner);
	}

	@Then("owner should have one pet without name and id")
	public void ownerShouldHaveOnePetWithoutNameAndId() {
		List<Pet> ownerPets = this.owner.getPets();
		assertEquals(ownerPets.size(), 1);
		Pet ownerPet = ownerPets.get(0);
		assertNull(ownerPet.getId());
		assertNull(ownerPet.getName());
	}


	// .savePet method
	@Given("There is a pet without id and with name {string}")
	public void thereIsPetWithoutIdAndWithName(String name) {
		pet = new Pet();
		pet.setName(name);
	}

	@When("requested to save this pet for existing owner")
	public void requestedSavePetForOwner() {
		petService.savePet(pet, owner);
	}

	@Then("owner should have one pet with {string} name")
	public void ownerShouldHavePetWithNameAndId(String name) {
		List<Pet> ownerPets = this.owner.getPets();
		assertEquals(ownerPets.size(), 1);
		Pet ownerPet = ownerPets.get(0);
		assertEquals(ownerPet.getName(), name);
	}

}
