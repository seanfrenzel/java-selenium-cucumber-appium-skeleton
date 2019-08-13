package core.utilities.setup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;

public class Config {
  public static final String WORKSPACE = getProperty("user.dir");
  public static String env = getProperty("env", "https://gfycat.com/");
  public static final String USER = getProperty("user", "user1");

  private String platform;
  private String deviceName;
  private String url;
  private Map<String, Object> capabilities;

  private boolean isAndroid;
  private boolean isIos;
  private boolean isWeb;
  private boolean isMobile;

  public Config() {
    Logger.getLogger("org.openqa.core.remote").setLevel(Level.OFF);
    // set platform property to -> Android, iOS, or Web
    platform = getProperty("platform", "web");
    setCapabilitiesForPlatform(platform);
  }

  private void setCapabilitiesForPlatform(String platform) {
    isAndroid = platform.equalsIgnoreCase("Android");
    isIos = platform.equalsIgnoreCase("iOS");
    isWeb = platform.equalsIgnoreCase("Web");
    if (isAndroid || isIos) isMobile = true;

    if (isAndroid) setAndroidCapabilities();
    if (isIos) setIosCapabilities();
    if (isWeb) setWebCapabilities();
  }

  private void setIosCapabilities() {
    deviceName = getProperty("deviceName", "iPhone x");
    url = getProperty("seleniumGrid", "http://0.0.0.0:4723/wd/hub");

    capabilities = getDeviceCapabilities(deviceName);
    capabilities.put("app", Paths.get(WORKSPACE, "apps", "appHere").toString());
    capabilities.put("platformName", "iOS");
    capabilities.put("automationName", "XCUITest");
    capabilities.put("xcodeOrgId", getProperty("xcodeOrgId", "ID_HERE"));
    capabilities.put("xcodeSigningId", getProperty("xcodeSigningId", "iPhone Developer"));
  }

  // NOTE
  /* use -> capabilities.put("app", Paths.get(WORKSPACE, "createAppFolderInProject", "appInFolderHere").toString()); <-
  /* instead of [appPackage], [appActivity] you have an app to use */
  private void setAndroidCapabilities() {
    deviceName = getProperty("deviceName", "emulator-5554");
    url = getProperty("seleniumGrid", "http://0.0.0.0:4723/wd/hub");

    capabilities = getDeviceCapabilities(deviceName);
    capabilities.put("appPackage", "com.google.android.apps.maps");
    capabilities.put("appActivity", "com.google.android.maps.MapsActivity");
    capabilities.put("platformName", "Android");
    capabilities.put("automationName", "UiAutomator2");
    capabilities.put("systemPort", parseInt(getProperty("systemPort", "8200")));
    capabilities.put("autoGrantPermissions", true);
  }

  private void setWebCapabilities() {
    deviceName = getProperty("deviceName", "chrome");
    url = getProperty("seleniumGrid", "http://0.0.0.0:4444/wd/hub");

    capabilities = getDeviceCapabilities(deviceName);
  }

  private HashMap<String, Object> getDeviceCapabilities(String device) {
    InputStream file = getClass().getResourceAsStream("/jsonData/devices.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(file));
    Type hashType = new TypeToken<HashMap<String, Object>>() {}.getType();
    JsonElement jsonElement = new JsonParser().parse(reader).getAsJsonObject().get(device);
    return new Gson().fromJson(jsonElement, hashType);
  }

  //////////////////
  // Get and Sets //
  //////////////////
  String getPlatform() {
    return platform;
  }

  public Map<String, Object> getCapabilities() {
    return capabilities;
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

  public boolean isMobile() {
    return isMobile;
  }

  public String getUrl() {
    return url;
  }

  public static String getEnv() {
    return env;
  }

  public static void setEnv(String env) {
    Config.env = env;
  }

  String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }
}
