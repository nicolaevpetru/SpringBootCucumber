@ui
Feature: Google search test

  Scenario: Search on Google.com
    When I navigate to https://www.google.com
    Then I search for apple