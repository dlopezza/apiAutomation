@active
Feature: Resource testing CRUD

  @smoke
  Scenario: Read details of an existing resource
    Given there are registered resources in the system
    When I retrieve the details of the resource with ID "1"
    Then the response should have a status code of 200
    And the response should have the following details:
      | name        | trademark  | stock | price | description          | tags          | active | id |
      | ResourceOne | Trademark1 | 100  | 29.99 | Description for Res1 | Tag1, Tag2    | true   | 1  |
    And validates the response with resource JSON schema

  @smoke
  Scenario: Create a new resource
    Given I have a resource with the following details:
      | name        | trademark  | stock | price | description          | tags          | active | id |
      | ResourceTwo | Trademark2 | 200  | 49.99 | Description for Res2 | Tag3, Tag4    | false  | 2  |
    When I send a POST request to create a resource
    Then the response should have a status code of 201
    And the response should include the details of the created resource
    And validates the response with resource JSON schema

  @smoke @regression
  Scenario: View all the resources
    Given there are registered resources in the system
    When I send a GET request to view all the resources
    Then the response should have a status code of 200
    And validates the response with resource list JSON schema

  @smoke
  Scenario: Update resource details
    Given there are registered resources in the system
    And I retrieve the details of the resource with ID "1"
    When I send a PUT request to update the resource with ID "1"
    """
    {
      "name": "UpdatedResource",
      "trademark": "UpdatedTrademark",
      "stock": 150,
      "price": 39.99,
      "description": "Updated description",
      "tags": "UpdatedTag1, UpdatedTag2",
      "active": true
    }
    """
    Then the response should have a status code of 200
    And the response should have the following details:
      | name            | trademark       | stock | price | description           | tags                      | active | id |
      | UpdatedResource | UpdatedTrademark | 150  | 39.99 | Updated description | UpdatedTag1, UpdatedTag2 | true   | 1  |
    And validates the response with resource JSON schema

  @smoke @regression
  Scenario: Delete an existing resource
    Given there are registered resources in the system
    When I send a DELETE request to delete the resource with ID "1"
    Then the response should have a status code of 200
    And validates the response with resource JSON schema
