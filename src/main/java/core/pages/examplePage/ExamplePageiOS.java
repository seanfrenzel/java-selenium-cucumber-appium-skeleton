package core.pages.examplePage;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ExamplePageiOS extends ExamplePage {

  @FindBy(css = "div.logo")
  private IOSElement logo;

  @FindBy(css = "div.search-input-wrapper > input")
  private IOSElement searchInput;

  @FindBy(css = "div.video-player-container.player-container")
  private IOSElement neatGif;

  @FindBy(css = "button.search-button")
  private IOSElement search;

  @FindBy(className = "grid-gfy-item")
  private List<IOSElement> gifs;

  public ExamplePageiOS() {
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
