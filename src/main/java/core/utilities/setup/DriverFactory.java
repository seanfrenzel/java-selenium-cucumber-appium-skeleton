package core.utilities.setup;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class DriverFactory {
  private final URL url;
  private final DesiredCapabilities capabilities;
  private Config config = new Config();

  public DriverFactory(String url, Map<String, Object> map) throws MalformedURLException {
    this.url = new URL(url);
    this.capabilities = new DesiredCapabilities(map);
    createDriver();
  }

  public RemoteWebDriver createDriver() {
    String platform = config.getPlatform().toUpperCase();

    switch (platform) {
      case "ANDROID":
        return new AndroidDriver<MobileElement>(url, capabilities);
      case "IOS":
        return new IOSDriver<MobileElement>(url, capabilities);
      case "WEB":
        return new RemoteWebDriver(url, capabilities);
      default:
        throw new IllegalArgumentException(
            String.format("Driver Factory type not implemented: [%s]", platform));
    }
  }
}
