package core.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestData {
  public final User user;

  public TestData(String username) {
    this.user = setUserData(username);
  }

  private User setUserData(String username) {
    InputStream file = getClass().getResourceAsStream(String.format("/jsonData/%s.json", username));
    BufferedReader reader = new BufferedReader(new InputStreamReader(file));

    Gson gson = new Gson();
    JsonParser parser = new JsonParser();

    JsonElement jsonElement = parser.parse(reader).getAsJsonObject();

    return gson.fromJson(jsonElement, User.class);
  }
}
