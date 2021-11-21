package io.github.densudas;

import io.github.densudas.controls.ControlSorts;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class Locators {

  public static List<Locator> buttons =
      new ArrayList<>() {
        {
          add(
              new Locator(
                  ControlSorts.Button.BUTTON_1,
                  By.xpath(".//button[descendant::label[text()=\"%s\"]]")));
          add(
              new Locator(
                  ControlSorts.Button.BUTTON_1,
                  By.xpath(".//input[@type='submit' and @aria-label=\"%s\"]")));
        }
      };

  public static List<Locator> textFields =
      new ArrayList<>() {
        {
          add(
              new Locator(
                  ControlSorts.TextField.TEXT_FIELD_1,
                  By.xpath(".//input[@type='text' and @title=\"%s\"]")));
        }
      };
}
