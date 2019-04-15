Feature: Look at a neat gif


  ####################
  ## Neat Gif Tests ##
  ####################
  @testID
  Scenario: Looks at neat gif
    Given the user navigates to the site
    And searches for a neat gif
    Then verifies a neat "Neat Gif" is shown