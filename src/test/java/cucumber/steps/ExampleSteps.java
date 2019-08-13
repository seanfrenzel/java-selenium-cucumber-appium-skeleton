package cucumber.steps;

import core.pages.examplePage.ExamplePage;
import core.pages.examplePage.ExamplePageAndroid;
import core.pages.examplePage.ExamplePageWeb;
import core.pages.examplePage.ExamplePageiOS;
import core.utilities.setup.Config;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class ExampleSteps {

  private ExamplePage page;

  public ExampleSteps(Config config) {
    if (config.isAndroid()) page = new ExamplePageAndroid();
    if (config.isIos()) page = new ExamplePageiOS();
    if (config.isWeb()) page = new ExamplePageWeb();
  }

  @Given("the user navigates to the website/app")
  public void theUserNavigatesToTheSite() {
    page.assertPagePresent();
  }

  @Then("searches for a neat gif")
  public void searchesForANeatGif() {
    page.openNeatGif();
  }

  @Then("verifies {string} is displayed")
  public void verifirezIsDisplayed(String elementField) {
    page.assertDisplayed(page.getElement(elementField), 5);
  }

  @And("taps {string}")
  public void taps(String elementField) {
    page.tap(page.getElement(elementField));
  }
}
