@petService_annotation
Feature: Pet Service
  Background: Preconditions Explanation
    Given There is one owner in ownerRepository with id 2 and first name "Sajjad" and last name "Sanikhani"

  Scenario: Find Owner by Id
    When owner with id 2 is requested
    Then owner first name is "Sajjad"
    And his last name is "Sanikhani"

  Scenario: Find Pet by Id
    Given There is a pet with id 5 and name "Lucy" in pets
    When pet with id 7 is requested
    Then returned pet is null

  Scenario: New Pet for Owner
    When requested new pet for existing owner
    Then owner should have one pet without name and id

  Scenario: Save Pet for Owner
    Given There is a pet without id and with name "JoJo"
    When requested to save this pet for existing owner
    Then owner should have one pet with "JoJo" name
