package io.github.densudas;

import io.github.densudas.pages.indexpage.HomePage;
import io.github.densudas.utils.DriverFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class SimpleTest {

  @Test
  public void test() throws Exception {

    DriverFactory.getDriver().get("https://www.bbc.com/");
    new HomePage().focusOnPersonalDataModal().clickConsentButton().fillInSearchField("text");

    System.out.println();
  }

  @BeforeSuite
  public void setUp() {
    Runtime.getRuntime()
        .addShutdownHook(new Thread(DriverFactory::closeAllDrivers, "Shutdown-thread"));
  }

  @AfterSuite
  public void tearDown() {
    DriverFactory.closeAllDrivers();
  }
}
