Feature: Look at a neat gif


  ####################
  ## Neat Gif Tests ##
  ####################
  @Web
  Scenario: Looks at neat gif
    Given the user navigates to the website
    And searches for a neat gif
    Then verifies "Neat Gif" is displayed

  ####################
  ## Open Gmail App ##
  ####################
  @Android
  Scenario: Open Google Maps App
    Given the user navigates to the app
    And taps "Skip"
    Then verifies "Google Map" is displayed