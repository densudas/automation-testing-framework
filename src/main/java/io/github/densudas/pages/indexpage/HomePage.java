package io.github.densudas.pages.indexpage;

import io.github.densudas.controls.TextField;
import io.github.densudas.modals.PersonalDataModal;
import io.github.densudas.pages.BasePage;

public class HomePage extends BasePage<HomePage> {

  public PersonalDataModal<HomePage> focusOnPersonalDataModal() {
    return new PersonalDataModal<>(this);
  }

  public HomePage fillInSearchField(String text) throws Exception {
    Thread.sleep(1000);
    new TextField(this, TextFieldLabels.SEARCH).findControl().fillIn(text);
    return this;
  }

  @Override
  public String getLocation() {
    return this.getClass().getSimpleName();
  }
}
