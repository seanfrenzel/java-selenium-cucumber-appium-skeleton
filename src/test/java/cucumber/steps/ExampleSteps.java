package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import selenium.pages.ExamplePageWeb;
import selenium.utilities.setup.Config;

public class ExampleSteps {

  private final ExamplePageWeb page;

  public ExampleSteps(Config config) {
    ExamplePageWeb platformPage = null;
    if (config.isAndroid()) platformPage = new ExamplePageWeb();
    if (config.isIos()) platformPage = new ExamplePageWeb();
    if (config.isWeb()) platformPage = new ExamplePageWeb();

    this.page = platformPage;
  }

  @Given("the user navigates to the site")
  public void theUserNavigatesToTheSite() {
    page.assertPagePresent();
  }

  @Then("searches for a neat gif")
  public void searchesForANeatGif() {
    page.openNeatGif();
  }

  @Then("verifies a neat {string} is shown")
  public void verifiresANeatIsShown(String elementField) {
    page.assertDisplayed(page.getElement(elementField));
  }
}
