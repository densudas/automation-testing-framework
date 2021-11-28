package io.github.densudas.modals;

import io.github.densudas.Locatable;
import io.github.densudas.controls.Button;

public class PersonalDataModal<HomePage> implements Locatable {

  private HomePage parentPage;

  public PersonalDataModal(HomePage parentPage) {
    this.parentPage = parentPage;
  }

  public HomePage clickConsentButton() {
    new Button(this, PersonalDataModalLabels.CONSENT).findControl().click();
    return parentPage;
  }

  @Override
  public String getLocation() {
    return ((Locatable) parentPage).getLocation() + "." + this.getClass().getSimpleName();
  }
}
