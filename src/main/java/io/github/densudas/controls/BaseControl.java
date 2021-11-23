package io.github.densudas.controls;

import io.github.densudas.ControlSort;
import io.github.densudas.Locator;
import org.openqa.selenium.WebElement;

public abstract class BaseControl {

  private boolean searchFromRootNode;
  protected ControlSort controlSort;
  protected ControlType controlType;
  protected String name;
  protected Locator locator;
  private Locator parentLocator;
  private boolean hasShadowRoot; // does control have shadow root
  // a sequence number of controls with the same name on the page (starts with 1)
  protected int index = 1;
  // save the control in the repository
  protected boolean saveToControlsStorage;
  protected boolean searchControlInStorage = true;

  protected WebElement webElement;
  protected Throwable error;

  public void findLocators() {
    controlSort = locator.getControlSort();
    controlType = locator.getControlType();
  }

  public boolean isFound() {
    return webElement != null;
  }

  public WebElement getWebElement() {
    return webElement;
  }

  public ControlType getControlType() {
    return controlType;
  }

  public String getName() {
    return name;
  }

  public Throwable getError() {
    return error;
  }
}
