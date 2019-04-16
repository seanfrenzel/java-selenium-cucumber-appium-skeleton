package core.base;

import core.utilities.Tools;
import core.utilities.setup.Config;
import core.utilities.setup.Hooks;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageObjectBase {
  private RemoteWebDriver driver;

  public PageObjectBase() {
    this.driver = Hooks.getDriver();
    if (new Config().isMobile()) setAppiumDecorator();
    if (new Config().isWeb()) setAjaxDecorator();
  }

  private void setAppiumDecorator() {
    AppiumFieldDecorator appiumFieldDecorator =
        new AppiumFieldDecorator(driver, Duration.ofSeconds(5));
    PageFactory.initElements(appiumFieldDecorator, this);
  }

  private void setAjaxDecorator() {
    AjaxElementLocatorFactory decorator = new AjaxElementLocatorFactory(driver, 5);
    PageFactory.initElements(decorator, this);
  }

  /** Mobile Page Object Initialization */
  public PageObjectMobile mobile() {
    return new PageObjectMobile(driver);
  }

  /** Get Methods */
  Object getField(String elementField) {
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

  public List<WebElement> getElements(String elementsField) {
    try {
      List<WebElement> elements = (List<WebElement>) getField(elementsField);
      elements.removeAll(Collections.singleton(null));
      return elements;
    } catch (ClassCastException e) {
      return Collections.singletonList((WebElement) getField(elementsField));
    }
  }

  public WebElement getTargetElement(List<WebElement> elements, String text) {
    return elements.stream()
        .filter(elem -> elem.getText().equalsIgnoreCase(text))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("Element Target Text was not found: [%s]", text)));
  }

  /** Assert Methods */
  public void assertDisplayed(WebElement element) {
    fluentWait(3, 1).until(ExpectedConditions.visibilityOf(element)).isDisplayed();
  }

  public void assertElementDoesNotExist(WebElement element) {
    try {
      element.isDisplayed();
      throw new IllegalArgumentException("Element was unexpectedly present");
    } catch (NoSuchElementException | IndexOutOfBoundsException | ElementNotVisibleException e) {
      // Element does not exist
    }
  }

  public void assertTextNotFound(List<WebElement> elements, String text) {
    Assert.assertTrue(
        String.format("Text was found: [%s]", text),
        elements.stream().noneMatch(element -> element.getText().equalsIgnoreCase(text)));
  }

  public void valuesContained(List<String> actualValues, List<String> expectedValues) {
    List<String> values = new ArrayList<>(expectedValues);

    String errorMsg =
        String.format(
            "%n%s - actual values did not contain %n%s - expected values ", actualValues, values);

    Assert.assertTrue(errorMsg, actualValues.containsAll(values));
  }

  public void valuesNotContained(List<String> actualValues, List<String> expectedValues) {
    List<String> values = new ArrayList<>(expectedValues);

    String errorMsg =
        String.format(
            "%n%s -  actual values contained %n%s -  unexpected values ", actualValues, values);

    Assert.assertFalse(errorMsg, actualValues.containsAll(values));
  }

  /** Wait Methods */
  public void waitForListLoad(List<WebElement> elements, Integer seconds) {
    int timeWaited = seconds;
    boolean listIsEmpty = true;

    while (listIsEmpty && seconds-- > -1)
      try {
        listIsEmpty = elements.isEmpty();
      } catch (Exception passed) {
        // list was not found, continue wait
      }

    String errMsg = String.format("List was empty after waiting [%s] seconds", timeWaited);
    Assert.assertFalse(errMsg, listIsEmpty);
  }

  public void waitForInvisibilityOf(WebElement element, Integer seconds) {
    Assert.assertTrue("Seconds waited needs to be >= 0", seconds >= 0);
    int timeWaited = seconds;
    boolean visible = true;

    while (visible && seconds-- > -1)
      try {
        visible = element.isDisplayed();
      } catch (StaleElementReferenceException | NoSuchElementException passed) {
        visible = false;
      }

    String errMsg =
        String.format(
            "[%s] unexpectedly visible after [%s] seconds", element.toString(), timeWaited);

    Assert.assertFalse(errMsg, visible);
  }

  public FluentWait<WebDriver> fluentWait(Integer seconds, Integer pollTime) {
    return new FluentWait<WebDriver>(driver)
        .withTimeout(Duration.ofSeconds(seconds))
        .pollingEvery(Duration.ofSeconds(pollTime))
        .ignoring(org.openqa.selenium.WebDriverException.class);
  }
}
