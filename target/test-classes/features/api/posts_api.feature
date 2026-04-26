## ============================================================
## Feature : Posts API - CRUD Operations (Legacy Single-Service)
## TC      : TC_API_LEGACY_001 to TC_API_LEGACY_004
## ============================================================
#
#@api @smoke
#Feature: Posts API - CRUD Operations
#
#  As an API consumer
#  I want to perform CRUD operations on the Posts resource
#  So that I can verify the API contract is met
#
#  Background:
#    Given the API base URI is configured
#
#  @regression @get
#  Scenario: TC_API_LEGACY_001 - GET a post by ID returns 200 with correct data
#    When I send a GET request to "/posts/1"
#    Then the response status code should be 200
#    And the response body should contain field "id" with value "1"
#    And the response body should contain field "userId" with value "1"
#    And the response body should not be empty
#    And the response should match the "post-schema.json" schema
#
#  @regression @post
#  Scenario: TC_API_LEGACY_002 - POST a new post returns 201 with created resource
#    Given I have a valid "post" request body
#    When I send a POST request to "/posts"
#    Then the response status code should be 201
#    And the response body should contain a valid "id" field
#    And the response body should contain the submitted "title"
#    And the response body should contain the submitted "body"
#
#  @regression @patch
#  Scenario: TC_API_LEGACY_003 - PATCH an existing post returns 200 with updated data
#    Given I have a partial "post" update request body
#    When I send a PATCH request to "/posts/1"
#    Then the response status code should be 200
#    And the response body should contain the updated "title"
#    And the response body field "id" should equal "1"
#
#  @regression @delete
#  Scenario: TC_API_LEGACY_004 - DELETE an existing post returns 200 with empty body
#    When I send a DELETE request to "/posts/1"
#    Then the response status code should be 200
#    And the response body should be an empty JSON object
