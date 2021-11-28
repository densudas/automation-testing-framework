package io.github.densudas.pages;

import io.github.densudas.Locatable;
import org.openqa.selenium.WebElement;

public abstract class BasePage<T> implements Locatable {

  public static String pageID;
  public static String pageName;
  public static WebElement pageInstance;
}
