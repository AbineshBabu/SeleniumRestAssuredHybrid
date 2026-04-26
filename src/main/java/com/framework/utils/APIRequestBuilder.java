package com.framework.utils;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


public class APIRequestBuilder {

    private static final Logger log = LogManager.getLogger(APIRequestBuilder.class);
    private static final ConfigManager config = ConfigManager.ConfigProvider.getInstance();

    private APIRequestBuilder() {}

    // ── Request Specs ─────────────────────────────────────────────────────────

    /** Original base spec using the default api.base.uri from config. */
    public static RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
            .setBaseUri(config.apiBaseUri())
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
    }


    public static RequestSpecification getServiceRequestSpec(String serviceBaseUri) {
        log.info("Building RequestSpec for service URI: {}", serviceBaseUri);
        return new RequestSpecBuilder()
            .setBaseUri(serviceBaseUri)
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
//                .addFilter(new RequestLoggingFilter(LogDetail.BODY))
//                .addFilter(new ResponseLoggingFilter(LogDetail.BODY))
            .build();
    }

    public static RequestSpecification getAuthBearerRequestSpec(String token) {
        return new RequestSpecBuilder()
            .addRequestSpecification(getBaseRequestSpec())
            .addHeader("Authorization", "Bearer " + token)
            .build();
    }

    public static RequestSpecification getAuthBearerServiceRequestSpec(String serviceBaseUri, String token) {
        return new RequestSpecBuilder()
            .addRequestSpecification(getServiceRequestSpec(serviceBaseUri))
            .addHeader("Authorization", "Bearer " + token)
            .build();
    }

    public static RequestSpecification getAuthOAuthServiceRequestSpec(String serviceBaseUri, String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getServiceRequestSpec(serviceBaseUri))
                .addHeader("Authorization",
                        "OAuth oauth_consumer_key=\"" + config.trellokey() + "\", " +
                                "oauth_token=\"" + config.trellotoken() + "\"")
                .build();
    }

    public static RequestSpecification getRequestSpecWithHeaders(Map<String, String> headers) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
            .addRequestSpecification(getBaseRequestSpec());
        headers.forEach(builder::addHeader);
        return builder.build();
    }

    // ── Response Specs ────────────────────────────────────────────────────────

    public static ResponseSpecification getOkResponseSpec() {
        return new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();
    }

    public static ResponseSpecification getCreatedResponseSpec() {
        return new ResponseSpecBuilder()
            .expectStatusCode(201)
            .expectContentType(ContentType.JSON)
            .build();
    }

    public static ResponseSpecification getResponseSpec(int statusCode) {
        return new ResponseSpecBuilder()
            .expectStatusCode(statusCode)
            .build();
    }

    // ── Global Config ─────────────────────────────────────────────────────────

    public static void configureRestAssured() {
        RestAssured.baseURI = config.apiBaseUri();
        RestAssured.useRelaxedHTTPSValidation();
        log.info("RestAssured configured. Default Base URI: {}", config.apiBaseUri());
    }
}
