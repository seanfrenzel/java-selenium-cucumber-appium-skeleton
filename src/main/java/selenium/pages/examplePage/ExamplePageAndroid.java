package selenium.pages.examplePage;

import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ExamplePageAndroid extends ExamplePage {

  @FindBy(css = "div.logo")
  private AndroidElement logo;

  @FindBy(css = "div.search-input-wrapper > input")
  private AndroidElement searchInput;

  @FindBy(css = "div.video-player-container.player-container")
  private AndroidElement neatGif;

  @FindBy(css = "button.search-button")
  private AndroidElement search;

  @FindBy(className = "grid-gfy-item")
  private List<AndroidElement> gifs;

  public ExamplePageAndroid() {
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
