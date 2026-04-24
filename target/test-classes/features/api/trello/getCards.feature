Feature: To verify the go rest feature

  @api
  Scenario: To verify the get call
    Given the user setup the "trello" service
    When the user send GET request to "/cards"
    Then the user verify if the status code is 200
    Then the user verify if the response contains "id"