package io.github.densudas.exceptions;

import io.github.densudas.controls.BaseControl;
import org.openqa.selenium.NoSuchElementException;

//TODO: Update Errors mechanism
public class ControlNotFoundException extends RuntimeException {
  public ControlNotFoundException(BaseControl control) {
    var message =
        new StringBuilder()
            .append(control.getControlType().getName())
            .append(" with name '")
            .append(control.getName())
            .append("' is not found on page.");

    if (control.getError() != null) {
      throw new NoSuchElementException(message.toString(), control.getError());
    } else {
      throw new NoSuchElementException(message.toString());
    }
  }
}
