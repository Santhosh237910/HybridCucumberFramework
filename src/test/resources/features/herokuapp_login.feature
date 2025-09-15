Feature: Login functionality
  Purpose: Allow users to login to access the secure dashboard

  @Login @validLogin
  Scenario Outline: Successful login with valid credentials
    When username "<username>" and password "<password>" are entered
    And the login button is clicked
    Then the dashboard page should be displayed

    Examples:
      | username | password             |
      | tomsmith | SuperSecretPassword! |

  @Login @invalidLogin
  Scenario Outline: Login failure with invalid credentials
    When username "<username>" and password "<password>" are entered
    And the login button is clicked
    Then an error message "<errorMessage>" should be displayed

    Examples:
      | username    | password  | errorMessage              |
      | invalidUser | wrongPass | Your username is invalid! |
      | tomsmith    | wrongPass | Your password is invalid! |