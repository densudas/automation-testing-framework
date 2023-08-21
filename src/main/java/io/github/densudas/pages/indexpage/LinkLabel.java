package io.github.densudas.pages.indexpage;

import io.github.densudas.TranslatableLocatorName;
import java.util.Locale;

public enum LinkLabel implements TranslatableLocatorName {

  SEARCH_BBC("Search BBC");

  private String name;

  LinkLabel(String name) {
    this.name = name;
  }

  public String getName() {
    return getName(Locale.ENGLISH);
  }

  public String getName(Locale locale) {
    return name;
  }
}
