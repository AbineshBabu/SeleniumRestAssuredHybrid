package com.framework.api.steps;

import com.framework.config.ServiceRegistry;
import com.framework.utils.APIRequestBuilder;
import static  com.framework.utils.ScenarioContext.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import com.framework.utils.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.Map;

public class getCards {

    private ScenarioContext scenarioContext;
    private static final Logger log = LogManager.getLogger(ServiceRegistry.class);


    public getCards(ScenarioContext scenarioContext){
        this.scenarioContext=scenarioContext;
    }


    @Given("the user setup the {string} service")
    public void the_user_setup_the_service(String serviceName) {
        String baseUri=ServiceRegistry.getBaseUri(serviceName);
        RequestSpecification spec=APIRequestBuilder.getAuthOAuthServiceRequestSpec(baseUri,"");

        scenarioContext.set(Key.REQUEST_SPEC,spec);
        scenarioContext.set(Key.ACTIVE_SERVICE,serviceName);
    }
    @When("the user send GET request to {string}")
    public void the_user_send_get_request_to(String endpoint) {
        RequestSpecification spec= scenarioContext.get(Key.REQUEST_SPEC);

        Response response =RestAssured.given()
                .pathParam("cardid","69eb83cb37b5707ad8bf51af").spec(spec).get(endpoint+"/{cardid}");

        scenarioContext.set(Key.RESPONSE,response);
    }
    @Then("the user verify if the status code is {int}")
    public void the_user_verify_if_the_status_code_is(int expectedStatusCode) {
        Response response=scenarioContext.get(Key.RESPONSE);

        assertEquals(response.getStatusCode(),expectedStatusCode);
    }
    @Then("the user verify if the response contains {string}")
    public void the_user_verify_if_the_response_contains(String expectedProperty) {
        Response response =scenarioContext.get(Key.RESPONSE);

        String id=response.jsonPath().get(expectedProperty);

        assertNotNull(id);

    }
}
