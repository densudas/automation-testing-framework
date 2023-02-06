package io.github.densudas;

import io.github.densudas.pages.indexpage.HomePage;
import org.testng.annotations.Test;

public class SimpleTest extends BaseTest {

  @Test
  public void test() throws Exception {

    HomePage
        .navigateToPage()
        .clickSearch()
        .fillInSearchField("text");

    System.out.println();
  }
}
