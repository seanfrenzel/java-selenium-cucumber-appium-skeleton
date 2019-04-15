package selenium.data;

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
  public String name;
  public String firstName;
  public String initials;
  public List<String> parentNames;
  public Child child = new Child();

  public class Child {
    public String childUsername;
    public String childPassword;
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
