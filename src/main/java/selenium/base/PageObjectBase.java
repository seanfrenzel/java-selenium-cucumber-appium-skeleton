package selenium.base;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import selenium.utilities.Tools;
import selenium.utilities.setup.Hooks;

import java.lang.reflect.Field;
import java.time.Duration;

public class PageObjectBase {
  private RemoteWebDriver driver;

  public PageObjectBase() {
    this.driver = Hooks.getDriver();
    AppiumFieldDecorator decorator = new AppiumFieldDecorator(driver, Duration.ofSeconds(5));
    PageFactory.initElements(decorator, this);
  }

  /** Get Methods */
  private Object getField(String elementField) {
    String target = Tools.toCamelCase(elementField);
    try {
      Field field = getClass().getDeclaredField(target);
      field.setAccessible(true);
      return field.get(this);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalArgumentException(String.format("Element not found: [%s]", target));
    }
  }

  public WebElement getElement(String elementField) {
    return (WebElement) getField(elementField);
  }

  /** Assert Methods */
  public void assertDisplayed(WebElement element) {
    fluentWait(3, 1).until(ExpectedConditions.visibilityOf(element)).isDisplayed();
  }

  public FluentWait<WebDriver> fluentWait(Integer seconds, Integer pollTime) {
    return new FluentWait<WebDriver>(driver)
        .withTimeout(Duration.ofSeconds(seconds))
        .pollingEvery(Duration.ofSeconds(pollTime))
        .ignoring(org.openqa.selenium.WebDriverException.class);
  }
}
