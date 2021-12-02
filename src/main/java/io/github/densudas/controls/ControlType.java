package io.github.densudas.controls;

import io.github.densudas.ControlSort;

import java.util.Arrays;

public enum ControlType {
  BUTTON("Button", ControlSorts.Button.values()),
  TEXT_FIELD("Text field", ControlSorts.TextField.values());

  private String name;
  private ControlSort[] controlSorts;

  ControlType(String name, ControlSort[] controlSorts) {
    this.name = name;
    this.controlSorts = controlSorts;
  }

  public String getName() {
    return name;
  }

  public ControlSort defineControlSort(String controlSort) {
    return Arrays.stream(controlSorts)
        .filter(value -> value.toString().equalsIgnoreCase(controlSort))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("There is no such control sort " + controlSort));
  }
}
