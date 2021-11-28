package io.github.densudas.controls;

import io.github.densudas.Locatable;
import io.github.densudas.Locator;
import io.github.densudas.LocatorName;
import io.github.densudas.exceptions.ControlNotFoundException;
import io.github.densudas.exceptions.ControlNotInteractableException;
import io.github.densudas.utils.Utils;

public class Button extends BaseControl {

  public Button(Locatable pageObject, String name, int index) {
    this.location = pageObject.getLocation();
    this.name = name;
    this.index = index;
    this.controlType = ControlType.BUTTON;
  }

  public Button(Locatable pageObject,String name) {
    this.location = pageObject.getLocation();
    this.name = name;
    this.controlType = ControlType.BUTTON;
  }

  public Button(Locatable pageObject,LocatorName name) {
    this.location = pageObject.getLocation();
    this.name = name.getName();
    this.controlType = ControlType.BUTTON;
  }

  public Button(Locator locator) {
    this.saveToControlsStorage = false;
    this.searchControlInStorage = false;
    this.locator = locator;
    this.controlType = ControlType.BUTTON;
  }

  //TODO: check if it can be moved inside constructors
  public Button findControl() {
    //TODO: verify and update search mechanism
    findLocators();
    return this;
  }

  public Button click() throws ControlNotInteractableException {

    if (webElement == null) throw new ControlNotFoundException(this);
    if (!webElement.isEnabled()) throw new ControlNotInteractableException(this);

    switch ((ControlSorts.Button) controlSort) {
      case BUTTON_1, BUTTON_2 ->
          webElement.click();
      case BUTTON_3 ->
          Utils.clickWithJS(webElement);
      default ->
          throw new IllegalStateException("No such sort defined: " + controlSort);
    }
    return this;
  }

  public boolean isDisplayed() {
    boolean isDisplayed;
    switch ((ControlSorts.Button) controlSort) {
      case BUTTON_1, BUTTON_2 ->
          isDisplayed = webElement != null &&  webElement.isDisplayed();
      default ->
          throw new IllegalStateException("No such sort defined: " + controlSort);
    }
    return isDisplayed;
  }

  public boolean isHidden() {
    boolean isDisplayed;
    switch ((ControlSorts.Button) controlSort) {
      case BUTTON_1, BUTTON_2 ->
          isDisplayed = webElement == null || !webElement.isDisplayed();
      default ->
          throw new IllegalStateException("No such sort defined: " + controlSort);
    }
    return isDisplayed;
  }
}
