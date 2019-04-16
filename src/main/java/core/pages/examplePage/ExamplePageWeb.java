package core.pages.examplePage;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ExamplePageWeb extends ExamplePage {

  @FindBy(css = "div.logo")
  private WebElement logo;

  @FindBy(css = "div.search-input-wrapper > input")
  private WebElement searchInput;

  @FindBy(css = "div.video-player-container.player-container")
  private WebElement neatGif;

  @FindBy(css = "button.search-button")
  private WebElement search;

  @FindBy(className = "grid-gfy-item")
  private List<WebElement> gifs;

  public ExamplePageWeb() {
    super();
  }

  public void assertPagePresent() {
    assertDisplayed(logo);
  }

  public void openNeatGif() {
    searchInput.sendKeys("Neat");
    search.click();
    gifs.get(0).click();
  }
}
