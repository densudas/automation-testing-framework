package io.github.densudas.pages.indexpage;

import io.github.densudas.TranslatableLocatorName;

public enum ButtonLabels implements TranslatableLocatorName {
  ;

  ButtonLabels(String name) {
    this.name = name;
  }

  private String name;

  public String getName() {
    return name;
  }

  public String getEnglishName() {
    return name;
  }
}
