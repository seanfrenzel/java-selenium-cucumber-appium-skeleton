package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import selenium.pages.examplePage.ExamplePage;
import selenium.pages.examplePage.ExamplePageAndroid;
import selenium.pages.examplePage.ExamplePageWeb;
import selenium.pages.examplePage.ExamplePageiOS;
import selenium.utilities.setup.Config;

public class ExampleSteps {

  private ExamplePage page;

  public ExampleSteps(Config config) {
    if (config.isAndroid()) page = new ExamplePageAndroid();
    if (config.isIos()) page = new ExamplePageiOS();
    if (config.isWeb()) page = new ExamplePageWeb();
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
