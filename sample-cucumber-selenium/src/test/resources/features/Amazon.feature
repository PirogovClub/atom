#------------------------------------------------------------------------------------------
#This feature uses standard approach of java coded cucumber steps + page objects.
#------------------------------------------------------------------------------------------

Feature: Check NFS in Google
  As an Employee I Would like to see it first in search result on Amazon
  Background:
    Given the application "amazon"

  @amazon
  Scenario: Check google on the first line against microservice
    When user search on Amazon for "iphone 13"
    Then user clicks on 1st found with conditions
    Then create screenshot for the report

