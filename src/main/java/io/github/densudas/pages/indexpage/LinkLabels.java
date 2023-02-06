package io.github.densudas.pages.indexpage;

import io.github.densudas.TranslatableLocatorName;

public enum LinkLabels implements TranslatableLocatorName {

  SEARCH_BBC("Search BBC");

  private String name;

  LinkLabels(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getEnglishName() {
    return name;
  }
}
