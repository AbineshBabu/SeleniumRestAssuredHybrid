#@api @microservice
#Feature: Microservice API - Multi-Service CRUD Operations
#
#  As an API consumer
#  I want to perform targeted HTTP operations on each microservice
#  So that I can verify each service's API contract independently
#
#  @user-service @get @regression
#  Scenario: TC_API_001 - User Service - GET user by ID returns 200 with correct data
#    Given the user sets up the "user" service
#    When I send a GET request to "/users/1"
#    Then the response status code should be 200
#    And the response body should contain field "id" with value "1"
#    And the response body should contain a valid "name" field
#    And the response body should contain a valid "email" field
#    And the response body should not be empty
#    And the response should match the "user-schema.json" schema
#
#  @post-service @post @regression
#  Scenario: TC_API_002 - Post Service - POST a new post returns 201 with created resource
#    Given the user sets up the "post" service
#    And I have a valid "post" request body
#    When I send a POST request to "/posts"
#    Then the response status code should be 201
#    And the response body should contain a valid "id" field
#    And the response body should contain the submitted "title"
#    And the response body should contain the submitted "body"
#    And the response should match the "post-schema.json" schema
#
#  @comment-service @patch @regression
#  Scenario: TC_API_003 - Comment Service - PATCH a comment returns 200 with updated data
#    Given the user sets up the "comment" service
#    And I have a partial "comment" update request body
#    When I send a PATCH request to "/comments/1"
#    Then the response status code should be 200
#    And the response body should contain the updated "body"
#    And the response body field "id" should equal "1"
#    And the response should match the "comment-schema.json" schema
#
#  @album-service @delete @regression
#  Scenario: TC_API_004 - Album Service - DELETE an album returns 200 with empty body
#    Given the user sets up the "album" service
#    When I send a DELETE request to "/albums/1"
#    Then the response status code should be 200
#    And the response body should be an empty JSON object
#
#  @todo-service @get @regression
#  Scenario: TC_API_005 - Todo Service - GET todo by ID returns 200 with correct data
#    Given the user sets up the "todo" service
#    When I send a GET request to "/todos/1"
#    Then the response status code should be 200
#    And the response body should contain field "id" with value "1"
#    And the response body should contain a valid "title" field
#    And the response body should contain a valid "completed" field
#    And the response body should not be empty
#    And the response should match the "todo-schema.json" schema
