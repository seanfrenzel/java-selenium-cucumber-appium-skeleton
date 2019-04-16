package core.utilities.setup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;

public class Config {
  public static final String WORKSPACE = getProperty("user.dir");
  public static final String ENVIRONMENT = getProperty("environment", "https://gfycat.com/");
  public static final String URL = getProperty("seleniumGrid", "http://0.0.0.0:4444/wd/hub");
  public static final String USER = getProperty("user", "user1");

  private String platform;
  private String deviceName;
  private Map<String, Object> capabilities;

  public Config() {
    Logger.getLogger("org.openqa.core.remote").setLevel(Level.OFF);
    setPlatform(getProperty("platform", "web"));
    setCapabilitesForPlatform(getPlatform());
  }

  private boolean isAndroid;
  private boolean isIos;
  private boolean isWeb;

  private void setCapabilitesForPlatform(String platform) {
    isAndroid = platform.equalsIgnoreCase("Android");
    isIos = platform.equalsIgnoreCase("iOS");
    isWeb = platform.equalsIgnoreCase("Web");

    if (isAndroid) setAndroidCapabilites();
    if (isIos) setIosCapabilities();
    if (isWeb) setWebCapabilites();
  }

  private void setIosCapabilities() {
    setDeviceName(getProperty("deviceName", "iPhone x"));
    setCapabilities(getDeviceCapabilities(getDeviceName()));

    capabilities.put("automationName", "XCUITest");
    capabilities.put("xcodeOrgId", getProperty("xcodeOrgId", "ID_HERE"));
    capabilities.put("xcodeSigningId", getProperty("xcodeSigningId", "iPhone Developer"));
  }

  private void setAndroidCapabilites() {
    setDeviceName(getProperty("deviceName", "emulator-5554"));
    setCapabilities(getDeviceCapabilities(getDeviceName()));

    capabilities.put("automationName", "UiAutomator2");
    capabilities.put("systemPort", parseInt(getProperty("systemPort", "8200")));
    capabilities.put("autoGrantPermissions", true);
  }

  private void setWebCapabilites() {
    setDeviceName(getProperty("deviceName", "chrome"));
    setCapabilities(getDeviceCapabilities(getDeviceName()));
  }

  private HashMap<String, Object> getDeviceCapabilities(String device) {
    InputStream file = getClass().getResourceAsStream("/jsonData/devices.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(file));
    Type hashType = new TypeToken<HashMap<String, Object>>() {}.getType();
    JsonElement jsonElement = new JsonParser().parse(reader).getAsJsonObject().get(device);
    return new Gson().fromJson(jsonElement, hashType);
  }

  /** Get and Sets */
  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public Map<String, Object> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Map<String, Object> capabilities) {
    this.capabilities = capabilities;
  }

  public boolean isAndroid() {
    return isAndroid;
  }

  public boolean isIos() {
    return isIos;
  }

  public boolean isWeb() {
    return isWeb;
  }
  // @formatter:on
}
