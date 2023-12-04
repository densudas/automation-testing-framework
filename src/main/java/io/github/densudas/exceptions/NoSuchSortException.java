package io.github.densudas.exceptions;

import io.github.densudas.ControlSort;

public class NoSuchSortException extends IllegalArgumentException {

  public NoSuchSortException(ControlSort controlSort) {
    super("No such sort defined: " + controlSort);
  }
}
