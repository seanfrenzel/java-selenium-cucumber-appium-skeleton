package core.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class User {
  public String username;
  public String password;
  public String placeholderForOtherData;
  public List<String> placeholderForListData;
  public PlaceholderForChildClassData childClass = new PlaceholderForChildClassData();

  public class PlaceholderForChildClassData {
    public String childClassDataEX;
    public String childClassDataEX2;
  }

  public User setUserData(String username) {
    InputStream file = getClass().getResourceAsStream(String.format("/jsonData/%s.json", username));
    BufferedReader reader = new BufferedReader(new InputStreamReader(file));

    Gson gson = new Gson();
    JsonParser parser = new JsonParser();

    JsonElement jsonElement = parser.parse(reader).getAsJsonObject();

    return gson.fromJson(jsonElement, User.class);
  }
}
