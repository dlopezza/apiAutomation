package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client client;

    @Given("there are registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem() {
        response = clientRequest.getClients();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Client> clientList = clientRequest.getClientsEntity(response);
        if (clientList.isEmpty()) {
            response = clientRequest.createDefaultClient();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @Given("I have a client with the following client details:")
    public void iHaveAClientWithTheFollowingClientDetails(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps().get(0);
        client = Client.builder()
                .name(clientDataMap.get("name"))
                .lastName(clientDataMap.get("lastName"))
                .country(clientDataMap.get("country"))
                .city(clientDataMap.get("city"))
                .email(clientDataMap.get("email"))
                .phone(clientDataMap.get("phone"))
                .id(clientDataMap.get("id"))
                .build();

        logger.info("Client mapped: " + client);
    }

    @When("I retrieve the details of the client with client ID {string}")
    public void sendGETRequest(String clientId) {
        response = clientRequest.getClient(clientId);
        logger.info(response.jsonPath().prettify());
        logger.info("The status code is: " + response.statusCode());
    }

    @When("I send a GET request to view all the clients")
    public void iSendAGETRequestToViewAllTheClients() {
        response = clientRequest.getClients();
    }

    @When("I send a POST request to create a client")
    public void iSendAPOSTRequestToCreateAClient() {
        response = clientRequest.createClient(client);
    }

    @When("I send a DELETE request to delete the client with client ID {string}")
    public void iSendADELETERequestToDeleteTheClientWithClientID(String clientId) {
        response = clientRequest.deleteClient(clientId);
    }

    @When("I send a PUT request to update the client with client ID {string}")
    public void iSendAPUTRequestToUpdateTheClientWithClientID(String clientId, String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updateClient(client, clientId);
    }

    @Then("the client response should have a status code of {int}")
    public void theClientResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("the client response should have the following details:")
    public void theClientResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        client = clientRequest.getClientEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps().get(0);

        Assert.assertEquals(expectedDataMap.get("Name"), client.getName());
        Assert.assertEquals(expectedDataMap.get("LastName"), client.getLastName());
        Assert.assertEquals(expectedDataMap.get("Country"), client.getCountry());
        Assert.assertEquals(expectedDataMap.get("City"), client.getCity());
        Assert.assertEquals(expectedDataMap.get("Email"), client.getId());
    }

    @Then("validates the client response with client JSON schema")
    public void userValidatesClientResponseWithClientJSONSchema() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    @And("validates the client response with client list JSON schema")
    public void validatesTheClientResponseWithClientListJSONSchema() {
        // todo
    }
}
