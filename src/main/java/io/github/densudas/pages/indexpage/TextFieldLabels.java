package io.github.densudas.pages.indexpage;

import io.github.densudas.TranslatableLocatorName;

public enum TextFieldLabels implements TranslatableLocatorName {

  SEARCH("Search");

  private final String name;

  TextFieldLabels(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getEnglishName() {
    return name;
  }
}
