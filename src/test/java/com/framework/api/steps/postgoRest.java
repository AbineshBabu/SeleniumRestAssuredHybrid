package com.framework.api.steps;

import com.framework.config.ServiceRegistry;
import com.framework.dto.request.gorest.POSTUsersReq;
import com.framework.dto.response.gorest.POSTUsers;
import com.framework.utils.APIRequestBuilder;
import static  com.framework.utils.ScenarioContext.*;
import static org.testng.Assert.*;

import com.framework.utils.FileUtils;
import com.framework.utils.ScenarioContext;
import com.framework.utils.TestDataBuilder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;


public class postgoRest {

    private ScenarioContext scenarioContext;
    private static final Logger log = LogManager.getLogger(ServiceRegistry.class);


    public postgoRest(ScenarioContext scenarioContext){
        this.scenarioContext=scenarioContext;
    }

    @Given("the user setup the {string} service")
    public void the_user_setup_the_service(String serviceName) {
        String uri=ServiceRegistry.getBaseUri(serviceName);
        RequestSpecification spec =APIRequestBuilder.getAuthServiceRequestSpec(uri,"4d3b50232fabf4623d62ad26984567a94c038c0e96a60951a0b09d6ff9248182");

        scenarioContext.set(Key.ACTIVE_SERVICE,serviceName);
        scenarioContext.set(Key.REQUEST_SPEC,spec);
    }
    @When("the user send POST request to {string}")
    public void the_user_send_post_request_to(String endpoint) {
        RequestSpecification spec=scenarioContext.get(Key.REQUEST_SPEC);

//        Static String way of loading
//        String body = "{ \"name\": \"John e\", \"email\": \"johnd3@test.com\", " +
//                "\"gender\": \"male\", \"status\": \"active\" }";

//        using builder pattern, but providing constant value to payload
//        POSTUsersReq body=POSTUsersReq.builder().email("aa@gmnail.com").name("aam").status("active").gender("male").build();

//        loading file using file for static payload
//        String body = FileUtils.loadFile("POSTUsers.json");
//        log.info("paru da"+body);


        //using builder pattern + faker to have random test data
        POSTUsersReq body=TestDataBuilder.postUsersReqBuilder().build();

        Response response=RestAssured.given().spec(spec)
                .body(body).post(endpoint);

        scenarioContext.set(Key.RESPONSE,response);
    }
    @Then("the user verify if the status code is {int}")
    public void the_user_verify_if_the_status_code_is(int expectedStatusCode) {
        Response response=scenarioContext.get(Key.RESPONSE);

        assertEquals(response.getStatusCode(),expectedStatusCode);
    }
    @Then("the user verify if the response contains {string}")
    public void the_user_verify_if_the_response_contains(String string) {
        Response response=scenarioContext.get(Key.RESPONSE);

        POSTUsers postUsers=response.as(POSTUsers.class);
        assertTrue(postUsers.getEmail().contains("@"));

        int id=response.jsonPath().get("id");
        assertNotNull(id);
    }


}
