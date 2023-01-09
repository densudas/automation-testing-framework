package io.github.densudas.pages.indexpage;

import static io.github.densudas.utils.Utils.waitForElementToBeDisplayed;

import io.github.densudas.controls.TextField;
import io.github.densudas.modals.PersonalDataModal;
import io.github.densudas.pages.BasePage;
import io.github.densudas.utils.DriverFactory;
import org.openqa.selenium.By;

public class HomePage extends BasePage<HomePage> {

  private static final String URL = "https://www.bbc.com/";

  public static HomePage navigateToPage() {
    DriverFactory.getDriver().get(URL);
    waitForElementToBeDisplayed(By.cssSelector("#orb-banner"));
    waitForElementToBeDisplayed(By.cssSelector("#page[role='main']"));
    return new HomePage();
  }

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
