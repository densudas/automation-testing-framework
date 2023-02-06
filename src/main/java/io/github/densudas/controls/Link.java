package io.github.densudas.controls;

import io.github.densudas.BaseActions;
import io.github.densudas.Locatable;
import io.github.densudas.Locator;
import io.github.densudas.LocatorName;
import io.github.densudas.exceptions.ControlNotFoundException;
import io.github.densudas.exceptions.ControlNotInteractableException;

public class Link extends BaseControl {

  public Link(Locatable pageObject, String name, int index) {
    super(pageObject, name, index);
    this.controlType = ControlType.LINK;
  }

  public Link(Locatable pageObject, String name) {
    super(pageObject, name);
    this.controlType = ControlType.LINK;
  }

  public Link(Locatable pageObject, LocatorName name) {
    super(pageObject, name);
    this.controlType = ControlType.LINK;
  }

  public Link(Locator locator) {
    super(locator);
    this.controlType = ControlType.LINK;
  }

  // TODO: check if it can be moved inside constructors
  public Actions findControl() throws Exception {
    // TODO: verify and update search mechanism
    findLocators();
    return new Actions(this);
  }


  public class Actions extends BaseActions {

    private Link control;

    Actions(Link control) {
      this.control = control;
    }

    public Link click() throws ControlNotInteractableException {
      if (webElement == null) throw new ControlNotFoundException(control);
      if (!webElement.isEnabled()) throw new ControlNotInteractableException(control);

      switch ((ControlSorts.Link) controlSort) {
        case LINK_1 -> webElement.click();
        default -> throw new IllegalStateException("No such sort defined: " + controlSort);
      }
      return control;
    }

    public boolean isDisplayed() {
      return switch ((ControlSorts.Link) controlSort) {
        case LINK_1 -> webElement != null && webElement.isDisplayed();
        default -> throw new IllegalStateException("No such sort defined: " + controlSort);
      };
    }

    public boolean isHidden() {
      return switch ((ControlSorts.Link) controlSort) {
        case LINK_1 -> webElement == null || !webElement.isDisplayed();
        default -> throw new IllegalStateException("No such sort defined: " + controlSort);
      };
    }
  }
}
