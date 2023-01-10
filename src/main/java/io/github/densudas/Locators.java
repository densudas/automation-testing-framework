package io.github.densudas;

import io.github.densudas.controls.ControlSorts;
import io.github.densudas.controls.ControlType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;

public class Locators {

  public static Map<ControlType, List<Locator>> locatorsList = new HashMap<>();

  static {
    locatorsList.put(
        ControlType.BUTTON,
        new ArrayList<>() {
          {
            add(new Locator(
                ControlSorts.Button.BUTTON_1,
                By.xpath(".//button[descendant::label[text()=\"%s\"]]")));
            add(new Locator(
                ControlSorts.Button.BUTTON_1, By.xpath(".//button[@aria-label=\"%s\"]")));
            add(new Locator(
                ControlSorts.Button.BUTTON_1,
                By.xpath(".//input[@type='submit' and @aria-label=\"%s\"]")));
          }
        });
    locatorsList.put(
        ControlType.TEXT_FIELD,
        new ArrayList<>() {
          {
            add(new Locator(
                ControlSorts.TextField.TEXT_FIELD_1,
                By.xpath(".//input[@type='text' and @title=\"%s\"]")));
            add(new Locator(
                ControlSorts.TextField.TEXT_FIELD_1,
                By.xpath(".//input[@type='text' and @placeholder=\"%s\"]")));
          }
        });
  }

  public static List<Locator> getLocatorsByType(ControlType controlType) {
    return locatorsList.get(controlType);
  }
}
