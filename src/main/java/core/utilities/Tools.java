package core.utilities;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tools {

  private Tools() {
    throw new IllegalStateException("Utility Class");
  }

  public static String getRandomString(int size) {
    return generateRandom("QWERTYUIOPASDFGHJKLZXCVBNM", size);
  }
  
  public static String getRandomInt(int size) {
    return generateRandom("123456789", size);
  }
  
  public static String removeSpecialCharacters(String value) {
    return value.replaceAll("[^a-zA-Z]", "");
  }
  
  private static String generateRandom(String characters, int count) {
    StringBuilder builder = new StringBuilder();

    while (count-- != 0) {
      int character = (int) (Math.random() * characters.length());
      builder.append(characters.charAt(character));
    }
    return builder.toString();
  }

  public static String getDate(String dateFormat, int daysToAddOrSubstract) {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, daysToAddOrSubstract);
    return formatter.format(calendar.getTime());
  }

  public static String toCamelCase(String string) {
    String[] lines = StringUtils.split(string, " ");
    lines[0] = lines[0].toLowerCase();
    return StringUtils.join(lines);
  }
}
