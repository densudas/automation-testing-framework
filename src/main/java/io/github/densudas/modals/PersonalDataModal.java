package io.github.densudas.modals;

import io.github.densudas.Locatable;
import io.github.densudas.controls.Button;

public class PersonalDataModal<HomePage> implements Locatable {

  private final HomePage parentPage;

  public PersonalDataModal(HomePage parentPage) {
    this.parentPage = parentPage;
  }

  public HomePage clickConsentButton() throws Exception {
    new Button(this, PersonalDataModalLabels.CONSENT).findControl().click();
    return parentPage;
  }

  public PersonalDataModal<HomePage> click() throws Exception {
    new Button(this, PersonalDataModalLabels.CONSENT).findControl().click();
    return this;
  }

  @Override
  public String getLocation() {
    return ((Locatable) parentPage).getLocation() + "." + this.getClass().getSimpleName();
  }
}
