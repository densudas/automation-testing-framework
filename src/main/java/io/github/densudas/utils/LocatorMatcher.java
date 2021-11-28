package io.github.densudas.utils;

import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocatorMatcher {

  private static final String LOCATOR_REGEX = "^By\\.(\\w+): (.*)$";

  private final String locatorType;
  private String locator;

  public LocatorMatcher(String locatorType, String locator) {
    this.locatorType = locatorType;
    this.locator = locator;
  }

  public LocatorMatcher(By by) {
    List<String> matchLocatorRegex = matchLocatorRegex(by);

    if (matchLocatorRegex.isEmpty() || matchLocatorRegex.size() < 2) {
      throw new IllegalArgumentException("Locator By '" + by +"' can not be matched by regex '" + LOCATOR_REGEX + "'");
    }

    this.locatorType = matchLocatorRegex.get(1);
    this.locator = matchLocatorRegex.get(2);
  }

  public String getLocator() {
    return locator;
  }

  public String getLocatorType() {
    return locatorType;
  }

  public static List<String> matchLocatorRegex(By locator) {
    if (locator == null) {
      throw new IllegalArgumentException("Instance of class " + By.class + " is null.");
    }

    List<String> groups = new ArrayList<>();
    Matcher matcher = Pattern.compile(LOCATOR_REGEX).matcher(locator.toString());
    if (matcher.find()) {
      for (int i = 0; i <= matcher.groupCount(); i++) {
        groups.add(matcher.group(i));
      }
    }
    return groups;
  }

  public By formatWithName(String name) {
    locator = String.format(this.locator, name);
    return getBy();
  }

  public By getBy() {
    switch (locatorType) {
      case "xpath" -> {
        return By.xpath(locator);
      }
      case "cssSelector" -> {
        return By.cssSelector(locator);
      }
      case "id" -> {
        return By.id(locator);
      }
      case "linkText" -> {
        return By.linkText(locator);
      }
      case "partialLinkText" -> {
        return By.partialLinkText(locator);
      }
      case "name" -> {
        return By.name(locator);
      }
      case "tagName" -> {
        return By.tagName(locator);
      }
      case "className" -> {
        return By.className(locator);
      }
      default -> throw new IllegalArgumentException("No such locator type: " + locatorType);
    }
  }
}
