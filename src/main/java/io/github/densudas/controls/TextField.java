package io.github.densudas.controls;

import io.github.densudas.Locatable;
import io.github.densudas.Locator;
import io.github.densudas.LocatorName;
import io.github.densudas.exceptions.ControlNotFoundException;
import io.github.densudas.exceptions.ControlNotInteractableException;

public class TextField extends BaseControl {

  public TextField(Locatable locatable, String name, int index) {
    this.location = locatable.getLocation();
    this.name = name;
    this.index = index;
    this.controlType = ControlType.TEXT_FIELD;
  }

  public TextField(Locatable locatable, String name) {
    this.location = locatable.getLocation();
    this.name = name;
    this.controlType = ControlType.TEXT_FIELD;
  }

  public TextField(Locatable locatable, LocatorName name) {
    this.location = locatable.getLocation();
    this.name = name.getName();
    this.controlType = ControlType.TEXT_FIELD;
  }

  public TextField(Locator locator) {
    this.saveToControlsStorage = false;
    this.searchControlInStorage = false;
    this.locator = locator;
  }

  // TODO: check if it can be moved inside constructors
  public TextField findControl() throws Exception {
    // TODO: verify and update search mechanism
    findLocators();
    return this;
  }

  public TextField fillIn(String text) throws ControlNotInteractableException {

    if (webElement == null) throw new ControlNotFoundException(this);
    if (!webElement.isEnabled()) throw new ControlNotInteractableException(this);

    switch ((ControlSorts.TextField) controlSort) {
      case TEXT_FIELD_1 -> webElement.sendKeys(text);
      default ->
          throw new IllegalStateException("No such sort defined: " + controlSort);
    }
    return this;
  }

  public boolean isDisplayed() {
    return switch ((ControlSorts.TextField) controlSort) {
      case TEXT_FIELD_1 -> webElement != null && webElement.isDisplayed();
      default -> throw new IllegalStateException("No such sort defined: " + controlSort);
    };
  }

  public boolean isHidden() {
    return switch ((ControlSorts.TextField) controlSort) {
      case TEXT_FIELD_1 -> webElement == null || !webElement.isDisplayed();
      default -> throw new IllegalStateException("No such sort defined: " + controlSort);
    };
  }
}
