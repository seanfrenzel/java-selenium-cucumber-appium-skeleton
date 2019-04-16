package core.base;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

public class PageObjectMobile extends PageObjectBase {
  private RemoteWebDriver driver;

  public PageObjectMobile(RemoteWebDriver driver) {
    super();
    this.driver = driver;
  }
  
  
  /** Get Methods - Mobile */
  @Override
  public MobileElement getElement(String elementField) {
    return (MobileElement) getField(elementField);
  }

  public List<MobileElement> getElementsMobile(String elementsField) {
    try {
      List<MobileElement> elements = (List<MobileElement>) getField(elementsField);
      elements.removeAll(Collections.singleton(null));
      return elements;
    } catch (ClassCastException e) {
      return Collections.singletonList((MobileElement) getField(elementsField));
    }
  }

  public MobileElement getTargetElementMobile(List<MobileElement> elements, String text) {
    return elements.stream()
        .filter(elem -> elem.getText().equalsIgnoreCase(text))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("Element Target Text was not found: [%s]", text)));
  }
  
  /** Tap Methods - Mobile */
  public void tap(MobileElement element) {
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

  private void iosScrollToElement(MobileElement element) {
    String elementID = element.getId();
    HashMap<String, String> scrollObject = new HashMap<>();
    scrollObject.put("element", elementID);
    scrollObject.put("toVisible", "not an empty string");
    driver.executeScript("mobile:scroll", scrollObject);
  }

  private void androidScrollToElement(MobileElement element, String direction) {
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
      List<MobileElement> elements, String text, String direction) {

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
