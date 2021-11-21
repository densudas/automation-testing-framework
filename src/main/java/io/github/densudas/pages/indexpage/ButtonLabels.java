package io.github.densudas.pages.indexpage;

import io.github.densudas.LocatorName;

public enum ButtonLabels implements LocatorName {
  GOOGLE_SEARCH("Google Search"),
  IM_FEELING_LUCKY("I'm Feeling Lucky");

  ButtonLabels(String name) {
    this.name = name;
  }

  private String name;

  public String getName() {
    return name;
  }
}
