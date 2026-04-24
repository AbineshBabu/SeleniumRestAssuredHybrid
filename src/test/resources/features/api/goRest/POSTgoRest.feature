#Feature: To verify the go rest feature
#
#  @api
#  Scenario: To verify the post call
#    Given the user setup the "gorest" service
#    When the user send POST request to "/users"
#    Then the user verify if the status code is 201
#    Then the user verify if the response contains "name"