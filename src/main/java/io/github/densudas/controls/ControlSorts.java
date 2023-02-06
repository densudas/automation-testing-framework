package io.github.densudas.controls;

import io.github.densudas.ControlSort;

public class ControlSorts {

  public enum Button implements ControlSort {
    BUTTON_1,
    BUTTON_2,
    BUTTON_3;

    private final ControlType controlType = ControlType.BUTTON;
    private String description = "";

    Button(String description) {
      this.description = description;
    }

    Button() {
    }

    public ControlType getControlType() {
      return controlType;
    }

    public String getDescription() {
      return description;
    }
  }

  public enum TextField implements ControlSort {
    TEXT_FIELD_1;

    private final ControlType controlType = ControlType.TEXT_FIELD;
    private String description = "";

    TextField(String description) {
      this.description = description;
    }

    TextField() {
    }

    public ControlType getControlType() {
      return controlType;
    }

    public String getDescription() {
      return description;
    }
  }

  public enum Link implements ControlSort {
    LINK_1;

    private final ControlType controlType = ControlType.LINK;
    private String description = "";

    Link(String description) {
      this.description = description;
    }

    Link() {
    }

    public ControlType getControlType() {
      return controlType;
    }

    public String getDescription() {
      return description;
    }
  }
}
