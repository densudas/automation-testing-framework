package io.github.densudas.pages.indexpage;

import io.github.densudas.Locator;
import io.github.densudas.controls.Button;
import io.github.densudas.controls.ControlSorts;
import io.github.densudas.pages.BasePage;
import org.openqa.selenium.By;

public class IndexPage extends BasePage<IndexPage> {

  public IndexPage clickSearchButton() {
    new Button(ButtonLabels.GOOGLE_SEARCH).getControl().click();
    return this;
  }

  public IndexPage clickImFillingLuckyButton() {
    new Button(
            new Locator(
                ControlSorts.Button.BUTTON_1,
                By.cssSelector(
                    "input[type='submit'][aria-label='"
                        + ButtonLabels.IM_FEELING_LUCKY.getName()
                        + "']")))
        .getControl()
        .click();
    return this;
  }
}
