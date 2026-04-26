Feature:  To Test https://api.restful-api.dev/objects

#  @api
#  Scenario: To test the GET call
#    Given the user sets up the "apirestful" service
#    When I send a GET request to "/objects/1"
#    Then the response status code should be 200
#    And the response body should contain field "name" with value "Google Pixel 6 Pro"
#    And the response body should contain a valid "data" field
#    And the response body should not be empty
#    And the response should match the "getApirestful-schema.json" schema

#  @api
#  Scenario: To test the POST call
#    Given the user sets up the "apirestful" service
#    When I send a POST request to "/objects"
#    Then the response status code should be 200
#    And the response body should contain a valid "id" field
#    And the response body should not be empty
#    And the response should match the "postApirestful-schema.json" schema


#  @api
#  Scenario: To test the PUT call
#    Given the user sets up the "apirestful" service
#    When I send a PUT request to "/objects/ff8081819cd4022c019ced5f46c32989"
#    Then the response status code should be 200
#    And the response body should contain field "name" with value "Abi"
#    And the response body should not be empty
#    And the response should match the "putApirestful-schema.json" schema

#  @api
#  Scenario: To test the DELETE call
#    Given the user sets up the "apirestful" service
#    When I send a POST request to "/objects"
#    When I send a DELETE request to "/objects" to newly created user
#    Then the response status code should be 200