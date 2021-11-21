package io.github.densudas;

import io.github.densudas.pages.indexpage.IndexPage;
import org.testng.annotations.Test;

public class SimpleTest {

  @Test
  public void test() {

    new IndexPage().clickSearchButton();

    System.out.println();
  }
}
