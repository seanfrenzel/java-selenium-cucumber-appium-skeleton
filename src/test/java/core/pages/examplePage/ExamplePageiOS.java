package core.pages.examplePage;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.support.FindBy;

public class ExamplePageiOS extends ExamplePage {

  @FindBy(css = "placeholder")
  private IOSElement placeholder;

  public ExamplePageiOS() {
    super();
  }

  public void assertPagePresent() {
    // placeholder, not implement
  }

  public void openNeatGif() {
    // not used by example test
    // NOTE: this is an example to keep it simple. Do not use the same page for different pages
  }
}
