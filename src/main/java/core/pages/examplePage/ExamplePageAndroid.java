package core.pages.examplePage;

import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class ExamplePageAndroid extends ExamplePage {

  @FindBy(xpath = "//*[@text = 'SKIP']")
  private AndroidElement skip;

  @FindBy(id = "com.google.android.apps.maps:id/mainmap_container")
  private AndroidElement googleMap;

  public ExamplePageAndroid() {
    super();
  }

  @Override
  public void assertPagePresent() {
    assertDisplayed(skip);
  }

  @Override
  public void openNeatGif() {
    // not used by example test
    // NOTE: this is an example to keep it simple. Do not use the same page for different pages
  }
}
