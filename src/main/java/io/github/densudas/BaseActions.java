package io.github.densudas;

import org.openqa.selenium.WebElement;

public abstract class BaseActions {

  protected WebElement webElement;

  public boolean isDisplayed() {
    return webElement != null && webElement.isDisplayed();
  }

  public boolean isHidden() {
    return webElement == null || !webElement.isDisplayed();
  }
}
