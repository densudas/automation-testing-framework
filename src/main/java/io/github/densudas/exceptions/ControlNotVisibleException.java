package io.github.densudas.exceptions;

import io.github.densudas.controls.BaseControl;
import org.openqa.selenium.ElementNotVisibleException;

//TODO: Update Errors mechanism
public class ControlNotVisibleException extends RuntimeException {
  public ControlNotVisibleException(BaseControl control) {
    var message =
        new StringBuilder()
            .append(control.getControlType().getName())
            .append(" with name '")
            .append(control.getName())
            .append("' is not visible.");

    if (control.getError() != null) {
      throw new ElementNotVisibleException(message.toString(), control.getError());
    } else {
      throw new ElementNotVisibleException(message.toString());
    }
  }
}
