package io.github.densudas.controls;

public enum ControlType {
  BUTTON("Button"),
  TEXT_FIELD("Text field");

  private String name;

  ControlType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
