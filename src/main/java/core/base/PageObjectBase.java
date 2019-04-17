package core.base;

import core.utilities.Tools;
import core.utilities.setup.Config;
import core.utilities.setup.Hooks;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

public class PageObjectBase {
  public RemoteWebDriver driver;

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

  /** Get Methods */
  private Object getField(String elementField) {
    String target = Tools.toCamelCase(elementField);
    Class aClass = null;
    try {
      aClass = getClass();
      Field field = aClass.getDeclaredField(target);
      field.setAccessible(true);
      return field.get(this);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("Element not found: [%s] in Class [%s]", target, aClass.getSimpleName()));
    }
  }

  public WebElement getElement(String elementField) {
    return (WebElement) getField(elementField);
  }

  public List<WebElement> getElementsWeb(String elementsField) {
    return (List<WebElement>) getField(elementsField);
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

  public WebElement getTargetElementMobile(List<WebElement> elements, String text) {
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
    try {
      fluentWait(3, 1).until(ExpectedConditions.visibilityOf(element)).isDisplayed();
    } catch (TimeoutException e) {
      throw new TimeoutException(String.format("[%s] was not displayed", element.toString()));
    }
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

  /** Tap Methods - Mobile */
  public void tap(WebElement element) {
    new TouchAction((MobileDriver) driver)
        .tap(
            TapOptions.tapOptions()
                .withElement(ElementOption.element(element).withCoordinates(5, 5)))
        .release()
        .perform();
  }

  /** Scroll Methods - Mobile */
  public void iosScroll(String direction) {
    HashMap<String, String> scrollObject = new HashMap<>();
    scrollObject.put("direction", direction);
    driver.executeScript("mobile:scroll", scrollObject);
  }

  private void iosScrollToElement(WebElement element) {
    MobileElement elem = (MobileElement) element;
    String elementID = elem.getId();
    HashMap<String, String> scrollObject = new HashMap<>();
    scrollObject.put("element", elementID);
    scrollObject.put("toVisible", "not an empty string");
    driver.executeScript("mobile:scroll", scrollObject);
  }

  private void androidScrollToElement(WebElement element, String direction) {
    int count = 0;
    boolean isDisplayed = false;

    while (!isDisplayed && count++ < 10) {
      try {
        isDisplayed = element.isDisplayed();
      } catch (NoSuchElementException | AssertionError | IndexOutOfBoundsException e) {
        androidScroll(direction);
      }
    }

    Assert.assertTrue(
        String.format("Element was not found after [%s] scrolls", count), isDisplayed);
  }

  private void androidScrollToElementWithText(
      List<WebElement> elements, String text, String direction) {

    int count = 0;
    boolean isDisplayed = false;

    while (!isDisplayed && count++ < 10) {
      try {
        isDisplayed = getTargetElementMobile(elements, text).isDisplayed();
      } catch (Exception e) {
        androidScroll(direction);
      }
    }

    Assert.assertTrue(
        String.format("Element with text [%s] was not found after [%s] scrolls", text, count),
        isDisplayed);
  }

  private void androidScroll(String scrollDirection) {
    Map<String, Integer> map;
    Dimension dimension = driver.manage().window().getSize();

    int centerX = dimension.width / 2;
    int centerY = dimension.height / 2;
    int topScreen = (int) (dimension.height * .30);
    int bottomScreen = (int) (dimension.height * .70);
    int rightScreen = (int) (dimension.width * .90);
    int leftScreen = (int) (dimension.width * .10);

    switch (scrollDirection) {
      case "up":
        map = setMoveToCOORD(centerX, topScreen, centerX, bottomScreen);
        break;
      case "down":
        map = setMoveToCOORD(centerX, bottomScreen, centerX, topScreen);
        break;
      case "right":
        map = setMoveToCOORD(rightScreen, centerY, leftScreen, centerY);
        break;
      case "left":
        map = setMoveToCOORD(leftScreen, centerY, rightScreen, centerY);
        break;
      default:
        throw new IllegalArgumentException(
            "Incorrect scroll direction given: Direction must be [up], [down], [left], or [right]");
    }
    
    new TouchAction((MobileDriver) driver)
        .longPress(longPressOptions().withPosition(point(map.get("fromX"), map.get("fromY"))))
        .moveTo(point(map.get("toX"), map.get("toY")))
        .release()
        .perform();
  }

  private Map<String, Integer> setMoveToCOORD(int fromX, int fromY, int toX, int toY) {
    Map<String, Integer> coordinates = new HashMap<>();
    coordinates.put("fromX", fromX);
    coordinates.put("fromY", fromY);
    coordinates.put("toX", toX);
    coordinates.put("toY", toY);
    return coordinates;
  }
}
