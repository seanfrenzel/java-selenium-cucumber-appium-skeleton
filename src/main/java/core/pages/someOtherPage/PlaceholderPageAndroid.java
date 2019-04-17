package core.pages.someOtherPage;

import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class PlaceholderPageAndroid extends PlaceholderPage {

  @FindBy(id = "placeholderExample")
  private AndroidElement placeholderExample;

  public PlaceholderPageAndroid() {
    super();
  }

  @Override
  public void placeholderMethod() {}
}
