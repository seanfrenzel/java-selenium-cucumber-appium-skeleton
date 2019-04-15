package selenium.utilities.setup;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import selenium.data.User;

import java.net.MalformedURLException;

public class Hooks {
  private boolean setup = false;
  private DriverFactory factory;
  private static RemoteWebDriver driver;
  private Config config = new Config();
  private User user = new User();

  public Hooks() {
    setDriver(driver);
  }

  public static RemoteWebDriver getDriver() {
    return driver;
  }

  public static void setDriver(RemoteWebDriver driver) {
    Hooks.driver = driver;
  }

  @Before(order = 1)
  public void beforeAll() throws MalformedURLException {

    if (!setup) {
      factory = new DriverFactory(Config.URL, config.getCapabilities());
      setup = true;
    }
  }

  @Before(order = 2)
  public void beforeScenario() {
    user.setUserData(Config.USER);
    setDriver(factory.createDriver());
    driver.get(Config.ENVIRONMENT);
  }

  @After
  public void afterScenario(Scenario scenario) {
    if (getDriver() != null && scenario.isFailed()) {
      try {
        byte[] screenshot = getDriver().getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
      } catch (WebDriverException webdriverException) {
        webdriverException.printStackTrace();
      }
    }
    if (getDriver() != null) getDriver().quit();
  }
}
