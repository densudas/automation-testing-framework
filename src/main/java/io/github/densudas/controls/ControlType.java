package io.github.densudas.controls;

import io.github.densudas.ControlSort;

public enum ControlType {
  BUTTON("Button", ControlSorts.Button.class, ControlSorts.Button.values()),
  TEXT_FIELD("Text field", ControlSorts.TextField.class, ControlSorts.TextField.values());

  private String name;
  private ControlSort[] controlSorts;
  private Class controlSortClass;

  ControlType(String name, Class controlSortClass, ControlSort[] controlSorts) {
    this.name = name;
    this.controlSorts = controlSorts;
    this.controlSortClass = controlSortClass;
  }

  public String getName() {
    return name;
  }

  public ControlSort[] getControlSorts() {
    return controlSorts;
  }

  public ControlSort defineControlSort(String controlSort) {
    return (ControlSort) Enum.valueOf(this.controlSortClass, "controlSort");
  }
}
