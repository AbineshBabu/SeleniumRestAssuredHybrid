//package com.framework.api.steps;
//
//import com.framework.config.ServiceRegistry;
//import com.framework.dto.response.gorest.GETUsers;
//import com.framework.utils.APIRequestBuilder;
//import static com.framework.utils.ScenarioContext.*;
//import com.framework.utils.ScenarioContext;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.List;
//
//import static org.testng.Assert.*;
//
//
//public class getgoRest {
//
//    private ScenarioContext scenarioContext;
//    private static final Logger log = LogManager.getLogger(ServiceRegistry.class);
//
//
//    public getgoRest(ScenarioContext scenarioContext){
//        this.scenarioContext=scenarioContext;
//    }
//
//    @Given("the user setup the {string} service")
//    public void the_user_setup_the_service(String serviceName) {
//        String baseUri=ServiceRegistry.getBaseUri(serviceName);
//        RequestSpecification spec =APIRequestBuilder.getServiceRequestSpec(baseUri);
//
//        scenarioContext.set(Key.ACTIVE_SERVICE, serviceName);
//        scenarioContext.set(Key.SERVICE_BASE_URI,baseUri);
//        scenarioContext.set(Key.REQUEST_SPEC,spec);
//    }
//    @When("the user send GET request to {string}")
//    public void the_user_send_get_request_to(String endpoint) {
//        RequestSpecification spec= scenarioContext.get(Key.REQUEST_SPEC);
//        Response response =RestAssured.given().spec(spec).get(endpoint);
//
//        scenarioContext.set(Key.RESPONSE,response);
//    }
//    @Then("the user verify if the status code is {int}")
//    public void the_user_verify_if_the_status_code_is(int expectedCode) {
//        Response response =scenarioContext.get(Key.RESPONSE);
//
//        assertEquals(response.getStatusCode(),expectedCode);
//    }
//    @Then("the user verify if the response contains {string}")
//    public void the_user_verify_if_the_response_contains(String expectedProperty) {
//        Response response = scenarioContext.get(Key.RESPONSE);
//
//        List<GETUsers> getUsers=response.jsonPath().getList("$", GETUsers.class);
//
//        long count=getUsers.stream().filter( d -> d.getId() !=0).distinct().count();
//
//        assertFalse(count == 0);
//        assertEquals(count, getUsers.size());
//
//        GETUsers g=getUsers.stream().
//                filter(d -> d.getName().equals("Aashritha Bhattacharya")).findFirst()
//                .orElse(null);
//
//        assertEquals(g.getId(),8446845);
//
//        assertTrue(
//        getUsers.stream().allMatch(d -> d.getEmail() != null && d.getEmail().contains("@")));
//
//    }
//
//
//}
