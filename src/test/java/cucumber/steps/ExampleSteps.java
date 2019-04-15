package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import selenium.pages.ExamplePage;
import selenium.utilities.setup.Hooks;

public class ExampleSteps {

  private final ExamplePage page;

  public ExampleSteps(ExamplePage page) {
    new Hooks();
    this.page = page;
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
