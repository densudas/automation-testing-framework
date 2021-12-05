package io.github.densudas.modals;

import io.github.densudas.TranslatableLocatorName;

public enum PersonalDataModalLabels implements TranslatableLocatorName {
  MANAGE_OPTIONS("Manage options"),
  CONSENT("Consent");

  PersonalDataModalLabels(String name) {
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
