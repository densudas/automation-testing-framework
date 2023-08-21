package io.github.densudas.pages.indexpage;

import io.github.densudas.TranslatableLocatorName;
import java.util.Locale;

public enum ButtonLabel implements TranslatableLocatorName {
  ;

  private final String name;

  ButtonLabel(String name) {
    this.name = name;
  }

  public String getName() {
    return getName(Locale.ENGLISH);
  }

  public String getName(Locale locale) {
    return name;
  }
}
