//package com.framework.api.steps;
//
//import com.framework.config.ConfigManager;
//import com.framework.config.ServiceRegistry;
//import com.framework.dto.request.apirestful.Objects;
//import com.framework.utils.APIRequestBuilder;
//import com.framework.utils.ScenarioContext;
//import com.framework.utils.ScenarioContext.Key;
//import com.framework.utils.TestDataBuilder;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.module.jsv.JsonSchemaValidator;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.testng.Assert;
//
//import static io.restassured.RestAssured.given;
//
//public class APISteps_2 {
//
//    public ScenarioContext context;
//    public ConfigManager config;
//    public Response response;
//
//
//    public APISteps_2(ScenarioContext context){
//        this.context=context;
//        this.config=ConfigManager.ConfigProvider.getInstance();
//    }
//
//    @Given("the user sets up the {string} service")
//    public void the_user_sets_up_the_service(String serviceName) {
//        String baseURL = ServiceRegistry.getBaseUri(serviceName);
//        RequestSpecification spec =APIRequestBuilder.getServiceRequestSpec(baseURL);
//
//        context.set(Key.ACTIVE_SERVICE,serviceName);
//        context.set(Key.REQUEST_SPEC,spec);
//        context.set(Key.SERVICE_BASE_URI,baseURL);
//    }
//
//    @When("I send a GET request to {string}")
//    public void i_send_a_get_request_to(String endpoint) {
//        RequestSpecification spec=context.get(Key.REQUEST_SPEC);
//        response = given().spec(spec).get(endpoint);
//        context.set(Key.RESPONSE,response);
//    }
//
//    @When("I send a POST request to {string}")
//    public void i_send_a_post_request_to(String endpoint) {
//        RequestSpecification spec=context.get(Key.REQUEST_SPEC);
//        Objects payload=TestDataBuilder.buildObjectRequest().build();
//
//        response = given().spec(spec).body(payload).post(endpoint);
//
//        context.set(Key.RESPONSE,response);
//    }
//
//    @When("I send a PUT request to {string}")
//    public void i_send_a_put_request_to(String endpoint) {
//        RequestSpecification spec=context.get(Key.REQUEST_SPEC);
//        Objects payload=TestDataBuilder.buildObjectPutRequest().name("Abi").build();
//
//        response = given().spec(spec).body(payload).put(endpoint);
//
//        context.set(Key.RESPONSE,response);
//    }
//
//    @When("I send a DELETE request to {string} to newly created user")
//    public void i_send_a_delete_request_to_to_newly_created_user(String endpoint) {
//        RequestSpecification spec=context.get(Key.REQUEST_SPEC);
//        response =context.get(Key.RESPONSE);
//        String id=response.getBody().jsonPath().get("id");
//
//
//        response = given().spec(spec).delete(endpoint+"/"+id);
//
//        context.set(Key.RESPONSE,response);
//    }
//
//    @Then("the response status code should be {int}")
//    public void the_response_status_code_should_be(int expectedStatusCode) {
//        response = context.get(Key.RESPONSE);
//        System.out.println("the response is "+response.getBody());
//        Assert.assertEquals(response.getStatusCode(),expectedStatusCode);
//    }
//    @Then("the response body should contain field {string} with value {string}")
//    public void the_response_body_should_contain_field_with_value(String field, String value) {
//        response = context.get(Key.RESPONSE);
//
//        Object actual=response.getBody().jsonPath().get(field);
//
//        Assert.assertEquals(actual,value);
//    }
//    @Then("the response body should contain a valid {string} field")
//    public void the_response_body_should_contain_a_valid_field(String field) {
//            response = context.get(Key.RESPONSE);
//            Assert.assertTrue(response.getBody().asString().contains(field));
//    }
//    @Then("the response body should not be empty")
//    public void the_response_body_should_not_be_empty() {
//          response = context.get(Key.RESPONSE);
//          Assert.assertFalse(response.getBody().asString().isEmpty());
//    }
//    @Then("the response should match the {string} schema")
//    public void the_response_should_match_the_schema(String schemaName) {
//        response = context.get(Key.RESPONSE);
//
//        System.out.println("the response is "+response);
//
//        Assert.assertTrue(JsonSchemaValidator.
//                matchesJsonSchemaInClasspath("schemas/apirestful/"+schemaName)
//                .matches(response.asString()));
//
//    }
//
//
//}
