package selenium.utilities;

import io.appium.java_client.MobileElement;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

  public static String getMonthString(int month) {
    String[] monthNames = {
      "January", "February", "March", "April", "May", "June",
      "July", "August", "September", "October", "November", "December"
    };
    return monthNames[month];
  }

  public static String getTag(Collection<String> tags) {
    try {
      return tags.stream()
          .filter(scenarioTags -> scenarioTags.matches("@\\d\\w+"))
          .findFirst()
          .orElse("TCM_ID_NOT_AVAILABLE");

    } catch (NoSuchFieldError ex) {
      System.out.println("Test case is not tagged properly! Assign TCMID");
      return "TCM_ID_NOT_AVAILABLE";
    }
  }

  public static String getDate(String dateFormat, int daysToAddOrSubstract) {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, daysToAddOrSubstract);
    return formatter.format(calendar.getTime());
  }

  public static String getMonth(String dateFormat, int monthsToAddOrSubstract) {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.MONTH, monthsToAddOrSubstract);
    return formatter.format(calendar.getTime());
  }

  public static String getSubstringTargetText(String text, String substringTarget) {
    try {
      int firstIndex = text.indexOf(substringTarget);
      int lastIndex = text.lastIndexOf(substringTarget);
      String string = text.substring(firstIndex, lastIndex).intern().concat(substringTarget);

      boolean dupeSubstrings = string.length() > substringTarget.length();
      if (dupeSubstrings) string = string.substring(0, substringTarget.length());

      return string;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException(String.format("Target [%s] Not Found", substringTarget));
    }
  }

  public static String getSubstringBeforeFirst(String text, String firstTargetIndex) {
    int lastIndex = text.indexOf(firstTargetIndex);
    return text.substring(0, lastIndex);
  }

  public static String getSubstringBeforeLast(String text, String lastTargetIndex) {
    int lastIndex = text.lastIndexOf(lastTargetIndex);
    return text.substring(0, lastIndex);
  }

  public static String getSubstringAfterLast(String text, String lastTargetIndex) {
    int lastIndex = text.lastIndexOf(lastTargetIndex);
    return text.substring(lastIndex, text.length());
  }

  public static String removeSpecialCharacters(String value) {
    return value.replaceAll("[^a-zA-Z]", "");
  }

  public static String toCamelCase(String string) {
    String[] lines = StringUtils.split(string, " ");
    lines[0] = lines[0].toLowerCase();
    return StringUtils.join(lines);
  }

  public static Map<String, String> transformDataTable(DataTable dataTable) {
    Map<String, String> convertedTable = dataTable.asMap(String.class, String.class);

    convertedTable.forEach(
        (String key, String value) ->
            value.replace("randomTxt", getRandomString(8)).replace("randomNum", getRandomInt(10)));
    return convertedTable;
  }

  private static String generateRandom(String characters, int count) {
    StringBuilder builder = new StringBuilder();

    while (count-- != 0) {
      int character = (int) (Math.random() * characters.length());
      builder.append(characters.charAt(character));
    }
    return builder.toString();
  }

  public static String buildStringFromElemList(List<MobileElement> elements) {
    StringBuilder stringBuild = new StringBuilder();
    for (MobileElement elem : elements) stringBuild.append(elem.getText());
    return stringBuild.toString();
  }

  public static List<String> buildStringListFromElemList(List<MobileElement> elements) {
    return elements.stream().map(MobileElement::getText).collect(Collectors.toList());
  }

  public static List<String> buildStringListFromIndex(List<MobileElement> elements, Integer index) {
    return elements.stream()
        .map(element -> elements.get(index).getText())
        .collect(Collectors.toList())
        .stream()
        .distinct()
        .collect(Collectors.toList());
  }

  public static List<String> buildSubstringListFromElemList(
      List<MobileElement> elements, String targetText) {

    List<String> stringList =
        elements.stream().map(MobileElement::getText).collect(Collectors.toList());

    return stringList.stream()
        .map(string -> getSubstringTargetText(string, targetText))
        .distinct()
        .collect(Collectors.toList());
  }

  public static List<String> buildSubstringTargetsList(List<String> strings, List<String> targets) {
    List<String> newList = new ArrayList<>();

    targets.stream()
        .map(target -> buildSubstringListFromStringList(strings, target))
        .forEach(newList::addAll);

    return newList;
  }

  private static List<String> buildSubstringListFromStringList(
      List<String> stringList, String targetText) {

    return stringList.stream()
        .map(
            string -> {
              try {
                return getSubstringTargetText(string, targetText);
              } catch (IndexOutOfBoundsException ignored) {
                // Ignored since not all strings in list will contain targetText
              }
              return "";
            })
        .distinct()
        .collect(Collectors.toList());
  }
}
