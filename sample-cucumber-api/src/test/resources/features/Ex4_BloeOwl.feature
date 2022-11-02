Feature: Pet Store Rest API



  @BlueOwl
  Scenario: Create Pet Information RI
    Given a rest api "blueolw"
    Given a header
      | Content-Type | application/json |
    And base input data "<<UserDataRI.default>>"
    And a request body "{}"
    When the system requests POST "beta/hiroad/quotes/v2/jurisdiction"
    Then the response code is 200
    And I'm parsing blueowl response

  @BlueOwl
  Scenario: Create Pet Information AZ
    Given a rest api "blueolw"
    Given a header
      | Content-Type | application/json |
    And base input data "<<UserDataAZ.default>>"
    And a request body "{}"
    When the system requests POST "beta/hiroad/quotes/v2/jurisdiction"
    Then the response code is 200
    And I'm parsing blueowl response

