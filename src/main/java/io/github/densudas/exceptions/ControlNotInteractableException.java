package io.github.densudas.exceptions;

import io.github.densudas.controls.BaseControl;
import org.openqa.selenium.ElementNotInteractableException;

//TODO: Update Errors mechanism
public class ControlNotInteractableException extends RuntimeException {
  public ControlNotInteractableException(BaseControl control) {
    var message =
        new StringBuilder()
            .append(control.getControlType().getName())
            .append(" with name '")
            .append(control.getName())
            .append("' is not interactable.");

    if (control.getError() != null) {
      throw new ElementNotInteractableException(message.toString(), control.getError());
    } else {
      throw new ElementNotInteractableException(message.toString());
    }
  }
}
