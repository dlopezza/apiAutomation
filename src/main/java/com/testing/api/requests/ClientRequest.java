package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Client;
import com.testing.api.utils.Constants;
import com.testing.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The type Client request.
 */
public class ClientRequest extends BaseRequest {
    private String endpoint;

    /**
     * Get Client list
     *
     * @return rest -assured response
     */
    public Response getClients() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get client by id
     *
     * @param clientId string
     * @return rest -assured response
     */
    public Response getClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Create client
     *
     * @param client model
     * @return rest -assured response
     */
    public Response createClient(Client client) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestPost(endpoint, createBaseHeaders(), client);
    }

    /**
     * Update client by id
     *
     * @param client   model
     * @param clientId string
     * @return rest -assured response
     */
    public Response updateClient(Client client, String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestPut(endpoint, createBaseHeaders(), client);
    }

    /**
     * Delete client by id
     *
     * @param clientId string
     * @return rest -assured response
     */
    public Response deleteClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    /**
     * Gets client entity.
     *
     * @param response the response
     * @return the client entity
     */
    public Client getClientEntity(@NotNull Response response) {
        return response.as(Client.class);
    }

    /**
     * Gets clients entity.
     *
     * @param response the response
     * @return the clients entity
     */
    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }

    /**
     * Create default client response.
     *
     * @return the response
     */
    public Response createDefaultClient() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createClient(jsonFile.getClientByJson(Constants.DEFAULT_CLIENT_FILE_PATH));
    }

    /**
     * Gets client entity.
     *
     * @param clientJson the client json
     * @return the client entity
     */
    public Client getClientEntity(String clientJson) {
        Gson gson = new Gson();
        return gson.fromJson(clientJson, Client.class);
    }

    /**
     * Find client by name client.
     *
     * @param name     the name
     * @param response the response
     * @return the client
     */
    public Client findClientByName(String name, @NotNull Response response) {
        List<Client> clientList = getClientsEntity(response); // Get all clients
        return clientList.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client not found: " + name));
    }

    /**
     * Validate schema boolean.
     *
     * @param response   the response
     * @param schemaPath the schema path
     * @return the boolean
     */
    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

}