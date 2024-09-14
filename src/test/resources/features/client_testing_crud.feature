@active
Feature: Client testing CRUD

  @smoke
  Scenario: Read details of an existing client
    Given there are registered clients in the system
    When I retrieve the details of the client with client ID "1"
    Then the client response should have a status code of 200
    And the client response should have the following details:
      | name   | lastName | country  | city   | email                | phone       | id |
      | Manuel | Munoz    | Colombia | Bogota | manuel@example.com   | 555-1234567 | 1  |
    And validates the client response with client JSON schema

  @smoke
  Scenario: Create a new client
    Given I have a client with the following client details:
      | name | lastName | country | city     | email                | phone       | id   |
      | John | Doe      | USA     | New York | john.doe@example.com | 555-9876543 | 2    |
    When I send a POST request to create a client
    Then the client response should have a status code of 201
    And the client response should have the following details:
      | name | lastName | country | city     | email                | phone       | id   |
      | John | Doe      | USA     | New York | john.doe@example.com | 555-9876543 | 2    |
    And validates the client response with client JSON schema

  @smoke @regression
  Scenario: View all the clients
    Given there are registered clients in the system
    When I send a GET request to view all the clients
    Then the client response should have a status code of 200
    And validates the client response with client list JSON schema

  @smoke
  Scenario: Update client details
    Given there are registered clients in the system
    And I retrieve the details of the client with client ID "1"
    When I send a PUT request to update the client with client ID "1"
    """
    {
      "name": "Maria",
      "lastName": "Gomez",
      "country": "Spain",
      "city": "Barcelona",
      "email": "maria.gomez@example.com",
      "phone": "555-6543210"
    }
    """
    Then the client response should have a status code of 200
    And the client response should have the following details:
      | name  | lastName | country | city      | email                | phone       | id |
      | Maria | Gomez    | Spain   | Barcelona | maria.gomez@example.com | 555-6543210 | 1  |
    And validates the client response with client JSON schema

  @smoke @regression
  Scenario: Delete an existing client
    Given there are registered clients in the system
    When I send a DELETE request to delete the client with client ID "1"
    Then the client response should have a status code of 200
    And the client response should have the following details:
      | name  | lastName | country | city      | email                | phone       | id |
      | Maria | Gomez    | Spain   | Barcelona | maria.gomez@example.com | 555-6543210 | 1  |
    And validates the client response with client JSON schema
