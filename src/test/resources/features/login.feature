Feature:
  The user should be able to log into the application.
  The username and password must match those stored in the database. The password will be sent directly to the server.

  Scenario: A user tries to login and succeeds
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    When the user introduces username "user1" and password "user1"
    Then the user successfully logs in

  Scenario: A user tries to login and fails
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    When the user introduces username "user3" and password "user2"
    Then the login fails

  Scenario: A user puts a wrong password
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    When the user introduces username "user1" and password "user2"
    Then the login fails
