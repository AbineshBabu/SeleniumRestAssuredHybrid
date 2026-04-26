## ============================================================
## Feature : User Login - UI Test (Data-Driven)
## TC      : TC_UI_001 (positive), TC_UI_002 (negative - invalid user),
##           TC_UI_003 (negative - invalid pass), TC_UI_004 (negative - empty)
## Target  : https://practicetestautomation.com/practice-test-login/
## Tags    : @ui @smoke @regression
##
## FIX APPLIED: Converted single hardcoded scenario to Scenario Outline
## with Examples table for data-driven positive + negative testing.
## ============================================================
#
#@ui @smoke
#Feature: User Login Authentication
#
#  As a registered user
#  I want to log in to the application
#  So that I can access protected dashboard resources
#
#  Background:
#    Given the user is on the login page
#
#  # ── TC_UI_001: Successful login with valid credentials ────────────────────
#  @regression @positive
#  Scenario: TC_UI_001 - Successful login with valid credentials
#    When the user enters username "student" and password "Password123"
#    And the user clicks the login button
#    Then the user should be redirected to the dashboard page
#    And the page heading should contain "Logged In Successfully"
#    And the current URL should contain "logged-in-successfully"
#    And the logout button should be visible
#
#  # ── TC_UI_002–004: Negative login scenarios (Data-Driven) ────────────────
#  @regression @negative
#  Scenario Outline: <test_id> - Login fails with <description>
#    When the user enters username "<username>" and password "<password>"
#    And the user clicks the login button
#    Then the user should see an error message
#    And the error message should contain "<expected_error>"
#    And the user should remain on the login page
#
#    Examples:
#      | test_id   | description              | username    | password    | expected_error             |
#      | TC_UI_002 | invalid username         | wronguser   | Password123 | Your username is invalid!  |
#      | TC_UI_003 | invalid password         | student     | wrongpass   | Your password is invalid!  |
