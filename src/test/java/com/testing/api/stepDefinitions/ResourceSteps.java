package com.testing.api.stepDefinitions;

import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resource;

    @Given("there are registered resources in the system")
    public void thereAreRegisteredResourcesInTheSystem() {
        response = resourceRequest.getResources();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        if (resourceList.isEmpty()) {
            response = resourceRequest.createDefaultResource();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @Given("I have a resource with the following details:")
    public void iHaveAResourceWithTheFollowingDetails(DataTable resourceData) {
        Map<String, String> resourceDataMap = resourceData.asMaps().get(0);
        resource = Resource.builder()
                .name(resourceDataMap.get("name"))
                .trademark(resourceDataMap.get("trademark"))
                .stock(Integer.parseInt(resourceDataMap.get("stock")))
                .price(Float.parseFloat(resourceDataMap.get("price")))
                .description(resourceDataMap.get("description"))
                .tags(resourceDataMap.get("tags"))
                .active(Boolean.parseBoolean(resourceDataMap.get("active")))
                .id(resourceDataMap.get("id"))
                .build();

        logger.info("Resource mapped: " + resource);
    }

    @When("I retrieve the details of the resource with ID {string}")
    public void sendGETRequest(String resourceId) {
        response = resourceRequest.getResource(resourceId);
        logger.info(response.jsonPath().prettify());
        logger.info("The status code is: " + response.statusCode());
    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
    }

    @When("I send a POST request to create a resource")
    public void iSendAPOSTRequestToCreateAResource() {
        response = resourceRequest.createResource(resource);
    }

    @When("I send a DELETE request to delete the resource with ID {string}")
    public void iSendADELETERequestToDeleteTheResourceWithID(String resourceId) {
        response = resourceRequest.deleteResource(resourceId);
    }

    @When("I send a PUT request to update the resource with ID {string}")
    public void iSendAPUTRequestToUpdateTheResourceWithID(String resourceId, String requestBody) {
        resource = resourceRequest.getResourceEntity(requestBody);
        response = resourceRequest.updateResource(resource, resourceId);
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        resource = resourceRequest.getResourceEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps().get(0);

        Assert.assertEquals(expectedDataMap.get("name"), resource.getName());
        Assert.assertEquals(expectedDataMap.get("trademark"), resource.getTrademark());
        Assert.assertEquals(Integer.parseInt(expectedDataMap.get("stock")), resource.getStock());
        Assert.assertEquals(Float.parseFloat(expectedDataMap.get("price")), resource.getPrice(), 0.01);
        Assert.assertEquals(expectedDataMap.get("description"), resource.getDescription());
        Assert.assertEquals(expectedDataMap.get("tags"), resource.getTags());
        Assert.assertEquals(Boolean.parseBoolean(expectedDataMap.get("active")), resource.isActive());
        Assert.assertEquals(expectedDataMap.get("id"), resource.getId());
    }

    @Then("validates the response with resource JSON schema")
    public void userValidatesResponseWithResourceJSONSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource object");
    }

    @Then("validates the response with resource list JSON schema")
    public void userValidatesResponseWithResourceListJSONSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource List object");
    }
}
