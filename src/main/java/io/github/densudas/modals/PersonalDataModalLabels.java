package io.github.densudas.modals;

import io.github.densudas.TranslatableLocatorName;

public enum PersonalDataModalLabels implements TranslatableLocatorName {
  MANAGE_OPTIONS("Manage options"),
  CONSENT("Consent");

  private final String name;

  PersonalDataModalLabels(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getEnglishName() {
    return name;
  }
}
