package io.github.densudas.controls;

import io.github.densudas.ControlSort;

public class ControlSorts {

  public enum Button implements ControlSort {
    BUTTON_1,
    BUTTON_2,
    BUTTON_3;

    private final ControlType controlType = ControlType.BUTTON;

    public ControlType getControlType() {
      return controlType;
    }
  }

  public enum TextField implements ControlSort {
    TEXT_FIELD_1;

    private final ControlType controlType = ControlType.TEXT_FIELD;

    public ControlType getControlType() {
      return controlType;
    }
  }
}
