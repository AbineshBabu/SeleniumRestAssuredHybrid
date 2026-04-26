Feature: To test UI elements in QAAutomationLabs

#  @ui
#  Scenario: To Test form
#    Given the user navigate to homepage
#    When the user click on form page
#    And the user fills the form with valid data
#    And verify the system displays "Form submitted successfully"

#  @ui
#  Scenario: To Test web table
#    Given the user navigate to homepage
#    When the user click on web table
#    And the user search by enter letter "j"
#    And verify the name is "Jane Smith" where the country is "UK"

#  @ui
#  Scenario: To Test iFrame
#    Given the user navigate to homepage
#    When the user click on iFrame
#    Then the user click on Click Me button on iframe1
#    And verify the "You have clicked on iframe 2 button" text

#  @ui
#  Scenario: To Test shadow dom
#    Given the user navigate to homepage
#    When the user click on shadow dom
#    And verify the "Hello from Shadow DOM!" text inside shadow dom

#  @ui
#  Scenario: To Test alert
#    Given the user navigate to homepage
#    When the user click on alert page
#    And the user click on alert button
#    Then the user verifies "This is an alert message!" text and accept it
#    And the user click on confirm button
#    Then the user verifies "Do you confirm this action?" text and cancel it
#    And the user click on prompt button
#    Then the user verifies "What is your name?" text
#    And the user enter "Apoorva" in the prompt and accept it

  @ui
  Scenario: To Test alert
    Given the user navigate to homepage
    When the user click on modal popup page
    And then the user switch to second window
