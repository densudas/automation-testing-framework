package io.github.densudas.controls;

import io.github.densudas.Locator;
import io.github.densudas.LocatorName;
import io.github.densudas.exceptions.ControlNotInteractableException;
import io.github.densudas.utils.Utils;

public class Button extends BaseControl {

  protected ControlSorts.Button sort = (ControlSorts.Button) controlSort;

  public Button(String name, int index) {
    this.name = name;
    this.index = index;
  }

  public Button(String name) {
    this.name = name;
  }

  public Button(LocatorName name) {
    this.name = name.getName();
  }

  public Button(Locator locator) {
    this.saveToControlsStorage = false;
    this.searchControlInStorage = false;
    this.locator = locator;
  }

  //TODO: check if it can be moved inside constructors
  public Button getControl() {
    //TODO: verify and update search mechanism
    findLocators();
    return this;
  }

  public Button click() throws ControlNotInteractableException {

    if (webElement == null || !webElement.isEnabled()) {
      throw new ControlNotInteractableException(this);
    }

    switch (sort) {
      case BUTTON_1, BUTTON_2 ->
          webElement.click();
      case BUTTON_3 ->
          Utils.clickWithJS(webElement);
      default ->
          throw new IllegalStateException("No such sort defined: " + sort);
    }
    return this;
  }

  public boolean isDisplayed() {
    boolean isDisplayed;
    switch (sort) {
      case BUTTON_1, BUTTON_2 ->
          isDisplayed = webElement != null &&  webElement.isDisplayed();
      default ->
          throw new IllegalStateException("No such sort defined: " + sort);
    }
    return isDisplayed;
  }

  public boolean isHidden() {
    boolean isDisplayed;
    switch (sort) {
      case BUTTON_1, BUTTON_2 ->
          isDisplayed = webElement == null || !webElement.isDisplayed();
      default ->
          throw new IllegalStateException("No such sort defined: " + sort);
    }
    return isDisplayed;
  }
}
