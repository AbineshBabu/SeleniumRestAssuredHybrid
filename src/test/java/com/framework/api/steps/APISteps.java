//package com.framework.api.steps;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.framework.config.ServiceRegistry;
//import com.framework.dto.DtoResponseResolver;
//import com.framework.dto.request.*;
//import com.framework.utils.APIRequestBuilder;
//import com.framework.utils.ScenarioContext;
//import com.framework.utils.ScenarioContext.Key;
//import com.framework.utils.TestDataBuilder;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.module.jsv.JsonSchemaValidator;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Supplier;
//
//import static io.restassured.RestAssured.given;
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * APISteps — step definitions ordered by first appearance in microservice_api.feature
// *
// * STEP PATTERN ALIGNMENT:
// *   {string} in Cucumber matches only QUOTED values in the feature file.
// *   All service name parameters must therefore be quoted in the feature:
// *     ✅  And I have a valid "post" request body
// *     ❌  And I have a valid post request body   ← {string} won't match this
// *
// * REQUEST BODY BUILDER PATTERN:
// *   Full bodies  → @Given("I have a valid {string} request body")
// *                  Constructs method name at runtime: build{ServiceName}Request()
// *                    "post"    → TestDataBuilder.buildPostRequest()
// *                    "user"    → TestDataBuilder.buildUserRequest()
// *                    "comment" → TestDataBuilder.buildCommentRequest()
// *                    "album"   → TestDataBuilder.buildAlbumRequest()
// *                    "todo"    → TestDataBuilder.buildTodoRequest()
// *
// *   Patch bodies → @Given("I have a partial {string} update request body")
// *                  No DataTable required — default patch fields are generated
// *                  from DEFAULT_PATCH_FIELDS registry using TestDataBuilder helpers.
// *                  Callers who need custom fields can supply a DataTable variant (overload below).
// *
// * REGISTRIES (inline — add one entry per new service):
// *   REQUEST_DTO_MAP      → service name → request DTO class (for buildPatchRequest)
// *   DEFAULT_PATCH_FIELDS → service name → Supplier<Map> of faker-generated patch fields
// */
//public class APISteps {
//
//    private static final Logger log = LogManager.getLogger(APISteps.class);
//
//    // ── Request DTO class registry ────────────────────────────────────────────
//    private static final Map<String, Class<?>> REQUEST_DTO_MAP = new HashMap<>();
//    static {
//        REQUEST_DTO_MAP.put("post",    PostRequest.class);
//        REQUEST_DTO_MAP.put("user",    UserRequest.class);
//        REQUEST_DTO_MAP.put("comment", CommentRequest.class);
//        REQUEST_DTO_MAP.put("album",   AlbumRequest.class);
//        REQUEST_DTO_MAP.put("todo",    TodoRequest.class);
//    }
//
//    // ── Default patch fields registry ────────────────────────────────────────
//    // Each entry is a Supplier so faker is called fresh per scenario, not once at load.
//    // Add one entry when a new service needs a default PATCH body.
//    private static final Map<String, Supplier<Map<String, Object>>> DEFAULT_PATCH_FIELDS = new HashMap<>();
//    static {
//        DEFAULT_PATCH_FIELDS.put("post",    () -> Map.of("title",     "UPDATED - " + TestDataBuilder.randomTitle()));
//        DEFAULT_PATCH_FIELDS.put("comment", () -> Map.of("body",      "PATCHED - " + TestDataBuilder.randomBody()));
//        DEFAULT_PATCH_FIELDS.put("user",    () -> Map.of("email",     TestDataBuilder.randomEmail()));
//        DEFAULT_PATCH_FIELDS.put("album",   () -> Map.of("title",     "UPDATED - " + TestDataBuilder.randomTitle()));
//        DEFAULT_PATCH_FIELDS.put("todo",    () -> Map.of("completed", true));
//    }
//
//    private final ScenarioContext context;
//    private Response              response;
//    private Object                requestDto;
//
//    public APISteps(ScenarioContext context) {
//        this.context = context;
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_001 · line 35
//    //  Given the user sets up the "..." service
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @Given("the user sets up the {string} service")
//    public void theUserSetsUpTheService(String serviceName) {
//        String baseUri = ServiceRegistry.getBaseUri(serviceName);
//        log.info("STEP: Setting up '{}' service → Base URI: {}", serviceName, baseUri);
//
//        RequestSpecification spec = APIRequestBuilder.getServiceRequestSpec(baseUri);
//
//        context.set(Key.ACTIVE_SERVICE,   serviceName);
//        context.set(Key.SERVICE_BASE_URI, baseUri);
//        context.set(Key.REQUEST_SPEC,     spec);
//
//        log.info("Service '{}' configured and active", serviceName);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_001 · line 36
//    //  When I send a GET request to "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @When("I send a GET request to {string}")
//    public void iSendAGetRequest(String endpoint) {
//        log.info("STEP: GET {}", endpoint);
//        response = given()
//                .spec(getActiveSpec())
//                .when()
//                .get(endpoint)
//                .then()
//                .log().all()
//                .extract().response();
//
//        context.set(Key.RESPONSE, response);
//        deserialiseAndStoreResponseDto();
//        log.info("GET {} → Status: {}", endpoint, response.getStatusCode());
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_001 · line 37
//    //  Then the response status code should be {int}
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @Then("the response status code should be {int}")
//    public void theResponseStatusCodeShouldBe(int expected) {
//        response = context.get(Key.RESPONSE);
//        int actual = response.getStatusCode();
//        log.info("STEP: Verify status — Expected: {} | Actual: {}", expected, actual);
//
//        assertThat(actual)
//                .as("HTTP status code should be %d", expected)
//                .isEqualTo(expected);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_001 · line 38
//    //  And the response body should contain field "..." with value "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response body should contain field {string} with value {string}")
//    public void theResponseBodyShouldContainFieldWithValue(String field, String expected) {
//        if (context.contains(Key.RESPONSE_BODY)) {
//            Object dto    = context.get(Key.RESPONSE_BODY);
//            Object actual = getDtoFieldValue(dto, field);
//            log.info("STEP (DTO): Verify field '{}' — Expected: {} | Actual: {}", field, expected, actual);
//
//            assertThat(String.valueOf(actual))
//                    .as("DTO field [%s] should equal [%s]", field, expected)
//                    .isEqualTo(expected);
//        } else {
//            response = context.get(Key.RESPONSE);
//            Object actual = response.jsonPath().get(field);
//            log.info("STEP (jsonPath fallback): Verify field '{}' — Expected: {} | Actual: {}", field, expected, actual);
//
//            assertThat(String.valueOf(actual))
//                    .as("Response field [%s] should equal [%s]", field, expected)
//                    .isEqualTo(expected);
//        }
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_001 · lines 39–40
//    //  And the response body should contain a valid "..." field
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response body should contain a valid {string} field")
//    public void theResponseBodyShouldContainAValidField(String field) {
//        if (context.contains(Key.RESPONSE_BODY)) {
//            Object dto    = context.get(Key.RESPONSE_BODY);
//            Object actual = getDtoFieldValue(dto, field);
//            log.info("STEP (DTO): Verify field '{}' is non-null — Value: {}", field, actual);
//
//            assertThat(actual)
//                    .as("DTO field [%s] should be non-null", field)
//                    .isNotNull();
//        } else {
//            response = context.get(Key.RESPONSE);
//            Object value = response.jsonPath().get(field);
//            log.info("STEP (jsonPath fallback): Verify field '{}' is non-null — Value: {}", field, value);
//
//            assertThat(value)
//                    .as("Response body should contain a non-null [%s] field", field)
//                    .isNotNull();
//        }
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_001 · line 41
//    //  And the response body should not be empty
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response body should not be empty")
//    public void theResponseBodyShouldNotBeEmpty() {
//        response = context.get(Key.RESPONSE);
//        assertThat(response.getBody().asString())
//                .as("Response body should not be empty")
//                .isNotBlank();
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_001 · line 42  /  TC_002 · line 58  /  TC_003 · line 73  /  TC_005 · line 101
//    //  And the response should match the "..." schema
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response should match the {string} schema")
//    public void theResponseShouldMatchSchema(String schemaName) {
//        response = context.get(Key.RESPONSE);
//        String schemaPath = "schemas/" + schemaName;
//        log.info("STEP: Validating response against schema: {}", schemaPath);
//
//        response.then()
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
//
//        log.info("Schema validation PASSED for: {}", schemaName);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_002 · line 52
//    //  And I have a valid {string} request body
//    //
//    //  Feature: And I have a valid "post" request body   ← quotes required
//    //
//    //  DYNAMIC FULL-BODY BUILDER:
//    //    "post"    → TestDataBuilder.buildPostRequest()
//    //    "user"    → TestDataBuilder.buildUserRequest()
//    //    "comment" → TestDataBuilder.buildCommentRequest()
//    //    "album"   → TestDataBuilder.buildAlbumRequest()
//    //    "todo"    → TestDataBuilder.buildTodoRequest()
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @Given("I have a valid {string} request body")
//    public void iHaveAValidRequestBody(String serviceName) {
//        requestDto = buildRequestDto(serviceName);
//        context.set(Key.REQUEST_BODY, requestDto);
//        log.info("STEP: Full request body for '{}' → {}", serviceName, requestDto);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_002 · line 53
//    //  When I send a POST request to "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @When("I send a POST request to {string}")
//    public void iSendAPostRequest(String endpoint) {
//        log.info("STEP: POST {}", endpoint);
//        requestDto = context.get(Key.REQUEST_BODY);
//
//        response = given()
//                .spec(getActiveSpec())
//                .body(requestDto)
//                .when()
//                .post(endpoint)
//                .then()
//                .log().all()
//                .extract().response();
//
//        context.set(Key.RESPONSE, response);
//        deserialiseAndStoreResponseDto();
//
//        Integer createdId = response.jsonPath().get("id");
//        if (createdId != null) context.set(Key.RESOURCE_ID, createdId);
//
//        log.info("POST {} → Status: {} | Created ID: {}", endpoint, response.getStatusCode(), createdId);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_002 · lines 56–57
//    //  And the response body should contain the submitted "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response body should contain the submitted {string}")
//    public void theResponseBodyShouldContainTheSubmitted(String field) {
//        response   = context.get(Key.RESPONSE);
//        requestDto = context.get(Key.REQUEST_BODY);
//
//        Object submitted = getDtoFieldValue(requestDto, field);
//        Object returned  = context.contains(Key.RESPONSE_BODY)
//                ? getDtoFieldValue(context.get(Key.RESPONSE_BODY), field)
//                : response.jsonPath().get(field);
//
//        log.info("STEP: Verify submitted '{}' — Sent: {} | Returned: {}", field, submitted, returned);
//
//        assertThat(String.valueOf(returned))
//                .as("Response [%s] should match submitted value", field)
//                .isEqualTo(String.valueOf(submitted));
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_003 · line 68
//    //  And I have a partial {string} update request body
//    //
//    //  Feature: And I have a partial "comment" update request body  ← quotes required
//    //
//    //  DYNAMIC PATCH BUILDER — no DataTable needed.
//    //  Default patch fields are generated per service from DEFAULT_PATCH_FIELDS registry:
//    //    "post"    → { title: faker }
//    //    "comment" → { body:  faker }
//    //    "user"    → { email: faker }
//    //    "album"   → { title: faker }
//    //    "todo"    → { completed: true }
//    //
//    //  For custom patch fields, use the DataTable overload below.
//    // ══════════════════════════════════════════════════════════════════════════
//
//    /**
//     * Generates a partial PATCH body using the default fields defined in
//     * DEFAULT_PATCH_FIELDS for the given service. Faker is called fresh per
//     * scenario invocation via the Supplier, so values are always dynamic.
//     */
//    @Given("I have a partial {string} update request body")
//    public void iHaveAPartialUpdateRequestBody(String serviceName) {
//        Class<?> dtoClass = resolveRequestDtoClass(serviceName);
//
//        Supplier<Map<String, Object>> defaultFields = DEFAULT_PATCH_FIELDS.get(serviceName.toLowerCase());
//        if (defaultFields == null) {
//            throw new IllegalArgumentException(
//                    "No default patch fields registered for service: '" + serviceName
//                            + "'. Add it to DEFAULT_PATCH_FIELDS in APISteps.");
//        }
//
//        Map<String, Object> fields = defaultFields.get();   // call supplier — fresh faker values
//        requestDto = TestDataBuilder.buildPatchRequest(dtoClass, fields);
//        context.set(Key.REQUEST_BODY, requestDto);
//        log.info("STEP: Partial request body for '{}' (default fields {}) → {}", serviceName, fields, requestDto);
//    }
//
//    /**
//     * DataTable overload — use when the feature file needs to specify exact patch values:
//     *
//     *   Given I have a partial "post" update request body with fields
//     *     | title | My custom title |
//     *     | body  | My custom body  |
//     *
//     * The DataTable rows (field → value) are passed directly to buildPatchRequest.
//     */
//    @Given("I have a partial {string} update request body with fields")
//    public void iHaveAPartialUpdateRequestBodyWithFields(String serviceName,
//                                                         io.cucumber.datatable.DataTable dataTable) {
//        Class<?> dtoClass = resolveRequestDtoClass(serviceName);
//
//        Map<String, Object> fields = new HashMap<>(dataTable.asMap(String.class, Object.class));
//        requestDto = TestDataBuilder.buildPatchRequest(dtoClass, fields);
//        context.set(Key.REQUEST_BODY, requestDto);
//        log.info("STEP: Partial request body for '{}' (custom fields {}) → {}", serviceName, fields, requestDto);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_003 · line 69
//    //  When I send a PATCH request to "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @When("I send a PATCH request to {string}")
//    public void iSendAPatchRequest(String endpoint) {
//        log.info("STEP: PATCH {}", endpoint);
//        requestDto = context.get(Key.REQUEST_BODY);
//
//        response = given()
//                .spec(getActiveSpec())
//                .body(requestDto)
//                .when()
//                .patch(endpoint)
//                .then()
//                .log().all()
//                .extract().response();
//
//        context.set(Key.RESPONSE, response);
//        deserialiseAndStoreResponseDto();
//        log.info("PATCH {} → Status: {}", endpoint, response.getStatusCode());
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_003 · line 71
//    //  And the response body should contain the updated "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response body should contain the updated {string}")
//    public void theResponseBodyShouldContainTheUpdated(String field) {
//        response   = context.get(Key.RESPONSE);
//        requestDto = context.get(Key.REQUEST_BODY);
//
//        Object patched  = getDtoFieldValue(requestDto, field);
//        Object returned = context.contains(Key.RESPONSE_BODY)
//                ? getDtoFieldValue(context.get(Key.RESPONSE_BODY), field)
//                : response.jsonPath().get(field);
//
//        log.info("STEP: Verify patched '{}' — Patched: {} | Returned: {}", field, patched, returned);
//
//        assertThat(String.valueOf(returned))
//                .as("Response [%s] should reflect patched value", field)
//                .isEqualTo(String.valueOf(patched));
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_003 · line 72
//    //  And the response body field "..." should equal "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response body field {string} should equal {string}")
//    public void theResponseBodyFieldShouldEqual(String field, String expected) {
//        Object actual = context.contains(Key.RESPONSE_BODY)
//                ? getDtoFieldValue(context.get(Key.RESPONSE_BODY), field)
//                : context.<Response>get(Key.RESPONSE).jsonPath().get(field);
//
//        log.info("STEP: Verify field '{}' equals '{}' — Actual: {}", field, expected, actual);
//
//        assertThat(String.valueOf(actual))
//                .as("Response field [%s] should equal [%s]", field, expected)
//                .isEqualTo(expected);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_004 · line 83
//    //  When I send a DELETE request to "..."
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @When("I send a DELETE request to {string}")
//    public void iSendADeleteRequest(String endpoint) {
//        log.info("STEP: DELETE {}", endpoint);
//
//        response = given()
//                .spec(getActiveSpec())
//                .when()
//                .delete(endpoint)
//                .then()
//                .log().all()
//                .extract().response();
//
//        context.set(Key.RESPONSE, response);
//        log.info("DELETE {} → Status: {}", endpoint, response.getStatusCode());
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  TC_004 · line 85
//    //  And the response body should be an empty JSON object
//    // ══════════════════════════════════════════════════════════════════════════
//
//    @And("the response body should be an empty JSON object")
//    public void theResponseBodyShouldBeAnEmptyJsonObject() {
//        response = context.get(Key.RESPONSE);
//        String body = response.getBody().asString().trim();
//        log.info("STEP: Verify DELETE response is empty JSON — Actual: '{}'", body);
//
//        assertThat(body)
//                .as("DELETE response body should be an empty JSON object {}")
//                .isEqualTo("{}");
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  UTILITY STEPS
//    // ══════════════════════════════════════════════════════════════════════════
//
//    /** Legacy backward-compatible setup step used by posts_api.feature. */
//    @Given("the API base URI is configured")
//    public void theApiBaseUriIsConfigured() {
//        log.info("STEP: Configuring REST Assured with default base URI");
//        APIRequestBuilder.configureRestAssured();
//        context.set(Key.REQUEST_SPEC, APIRequestBuilder.getBaseRequestSpec());
//    }
//
//    @And("the response body should be a non-empty array")
//    public void theResponseBodyShouldBeANonEmptyArray() {
//        response = context.get(Key.RESPONSE);
//        int size = response.jsonPath().getList("$").size();
//        log.info("STEP: Verify response is non-empty array — Size: {}", size);
//
//        assertThat(size)
//                .as("Response array should not be empty")
//                .isGreaterThan(0);
//    }
//
//    // ══════════════════════════════════════════════════════════════════════════
//    //  PRIVATE HELPERS
//    // ══════════════════════════════════════════════════════════════════════════
//
//    private RequestSpecification getActiveSpec() {
//        if (context.contains(Key.REQUEST_SPEC)) {
//            return context.get(Key.REQUEST_SPEC);
//        }
//        log.warn("No active service spec in context — falling back to default base spec");
//        return APIRequestBuilder.getBaseRequestSpec();
//    }
//
//    /**
//     * Resolves the request DTO class from REQUEST_DTO_MAP for the given service name.
//     * Throws a clear error if the service is not registered.
//     */
//    private Class<?> resolveRequestDtoClass(String serviceName) {
//        Class<?> dtoClass = REQUEST_DTO_MAP.get(serviceName.toLowerCase());
//        if (dtoClass == null) {
//            throw new IllegalArgumentException(
//                    "No request DTO registered for service: '" + serviceName
//                            + "'. Add it to REQUEST_DTO_MAP in APISteps.");
//        }
//        return dtoClass;
//    }
//
//    /**
//     * Dynamically invokes TestDataBuilder.build{ServiceName}Request() at runtime.
//     * Method name pattern: "build" + capitalise(serviceName) + "Request"
//     *
//     *   "post"    → buildPostRequest()
//     *   "user"    → buildUserRequest()
//     *   "comment" → buildCommentRequest()
//     *   "album"   → buildAlbumRequest()
//     *   "todo"    → buildTodoRequest()
//     */
//    private Object buildRequestDto(String serviceName) {
//        String capitalised = Character.toUpperCase(serviceName.charAt(0))
//                + serviceName.substring(1).toLowerCase();
//        String methodName  = "build" + capitalised + "Request";
//        try {
//            Method method = TestDataBuilder.class.getMethod(methodName);
//            Object result = method.invoke(null);
//            log.info("TestDataBuilder.{}() invoked → {}", methodName, result);
//            return result;
//        } catch (NoSuchMethodException e) {
//            throw new IllegalArgumentException(
//                    "TestDataBuilder has no method '" + methodName + "'. "
//                            + "Add build" + capitalised + "Request() to TestDataBuilder.", e);
//        } catch (Exception e) {
//            throw new RuntimeException(
//                    "Failed to invoke TestDataBuilder." + methodName + "()", e);
//        }
//    }
//
//    /**
//     * Deserialises the response body into the appropriate typed DTO and stores it
//     * in ScenarioContext under RESPONSE_BODY. Called after every HTTP step that
//     * may return a body (GET, POST, PATCH).
//     */
//    private void deserialiseAndStoreResponseDto() {
//        if (!context.contains(Key.ACTIVE_SERVICE)) {
//            log.debug("No active service in context — skipping DTO deserialisation");
//            return;
//        }
//
//        String body = response.getBody().asString().trim();
//        if (body.isEmpty() || body.equals("{}") || body.equals("[]")) {
//            log.debug("Response body is empty/blank — skipping DTO deserialisation");
//            return;
//        }
//
//        String serviceName = context.get(Key.ACTIVE_SERVICE);
//        if (!DtoResponseResolver.isRegistered(serviceName)) {
//            log.warn("No response DTO registered for service '{}' — skipping deserialisation", serviceName);
//            return;
//        }
//
//        try {
//            Class<?> dtoClass = DtoResponseResolver.resolve(serviceName);
//            Object   dto      = response.as(dtoClass);
//            context.set(Key.RESPONSE_BODY, dto);
//            log.info("Response deserialised as {} → {}", dtoClass.getSimpleName(), dto);
//        } catch (Exception e) {
//            log.warn("DTO deserialisation failed for service '{}': {}", serviceName, e.getMessage());
//        }
//    }
//
//    /**
//     * Converts any DTO to a Map via Jackson and retrieves the field by name.
//     * Handles all field types (String, Integer, Boolean) without reflection
//     * or getter-name derivation.
//     */
//    private Object getDtoFieldValue(Object dto, String fieldName) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> map = objectMapper.convertValue(dto, Map.class);
//        return map.get(fieldName);
//    }
//}