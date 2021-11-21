package io.github.densudas.controls;

import io.github.densudas.ControlSort;

public class ControlSorts {

  public enum Button implements ControlSort {
    BUTTON_1,
    BUTTON_2,
    BUTTON_3;

    public ControlType controlType = ControlType.BUTTON;
  }

  public enum TextField implements ControlSort {
    TEXT_FIELD_1;

    public ControlType controlType = ControlType.TEXT_FIELD;
  }
}
