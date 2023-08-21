package io.github.densudas.pages.indexpage;

import io.github.densudas.TranslatableLocatorName;

public enum ButtonLabels implements TranslatableLocatorName {
  ;

  private final String name;

  ButtonLabels(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getEnglishName() {
    return name;
  }
}
