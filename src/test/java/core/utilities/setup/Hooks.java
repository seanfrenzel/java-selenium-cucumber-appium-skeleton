package core.utilities.setup;

import core.data.TestData;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;

public class Hooks {
  private boolean setup = false;
  private DriverFactory factory;
  private static RemoteWebDriver driver;
  private Config config = new Config();
  private static TestData testData;
  private static SoftAssertions softAssert;
  private static Scenario currentScenario;

  public static SoftAssertions getSoftAssert() {
    return softAssert;
  }

  public static TestData getTestData() {
    return testData;
  }

  public static void setTestData(TestData testData) {
    Hooks.testData = testData;
  }

  public Hooks() {
    setDriver(driver);
    softAssert = new SoftAssertions();
  }

  public static RemoteWebDriver getDriver() {
    return driver;
  }

  private static void setDriver(RemoteWebDriver driver) {
    Hooks.driver = driver;
  }

  @Before(order = 1)
  public void beforeAll() throws MalformedURLException {
    if (!setup) {
      factory = new DriverFactory(config.getUrl(), config.getCapabilities());
      setup = true;
    }

    setTestData(new TestData(Config.USER));
    setDriver(factory.createDriver());

    if (config.isWeb()) driver.get(Config.env);
  }

  @After(order = 1)
  public void afterAll(Scenario scenario) {
    boolean driverNotNull = driver != null;
    try {
      if (scenario.isFailed()) embedScreenshot();
      if (!softAssert.errorsCollected().isEmpty()) softAssert.assertAll();

    } finally {
      setup = false;
      if (driverNotNull) driver.quit();
    }
  }

  public static void embedScreenshot() {
    try {
      final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      currentScenario.embed(screenshot, "image/png");
    } catch (WebDriverException | NullPointerException e) {
      System.out.println("Failed to take embed Screenshot");
    }
  }
}
