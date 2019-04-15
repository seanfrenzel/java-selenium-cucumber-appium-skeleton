package selenium.pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import selenium.base.PageObjectBase;

import java.util.List;

public class ExamplePage extends PageObjectBase {

  @FindBy(css = "div.logo")
  private WebElement logo;

  @FindBy(css = "div.search-input-wrapper > input")
  private WebElement searchInput;

  @FindBy(css = "div.video-player-container.player-container")
  private WebElement neatGif;

  @FindBy(css = "button.search-button")
  private WebElement search;

  @FindBy(className = "grid-gfy-item")
  List<WebElement> gifs;

  public ExamplePage() {
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
