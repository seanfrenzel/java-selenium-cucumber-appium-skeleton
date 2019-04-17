package core.pages.someOtherPage;

import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class PlaceholderPageWeb extends PlaceholderPage {

  @FindBy(id = "placeholderExample")
  private AndroidElement placeholderExample;

  public PlaceholderPageWeb() {
    super();
  }

  @Override
  public void placeholderMethod() {}
}
