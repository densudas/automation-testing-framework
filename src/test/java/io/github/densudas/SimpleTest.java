package io.github.densudas;

import io.github.densudas.pages.indexpage.HomePage;
import io.github.densudas.utils.DriverFactory;
import org.testng.annotations.Test;

public class SimpleTest extends BaseTest {

  @Test
  public void test() throws Exception {

    DriverFactory.getDriver().get("https://www.bbc.com/");
    new HomePage().focusOnPersonalDataModal().clickConsentButton().fillInSearchField("text");

    System.out.println();
  }
}
