package io.github.densudas.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;

import java.util.List;

public class Utils {

  public static final String FILE_SEPARATOR = System.getProperty("file.separator");
  public static final String USER_DIR = System.getProperty("user.dir");

  public static void clickWithJS(SearchContext context) {
    getJSExecutor(context).executeScript("arguments[0].click()", context);
  }

  public static JavascriptExecutor getJSExecutor(SearchContext context) {
    return (JavascriptExecutor) getWebDriver(context);
  }

  public static WebDriver getWebDriver(SearchContext context) {
    if (context instanceof WebDriver) {
      return (WebDriver) context;
    }

    if (!(context instanceof WrapsDriver)) {
      throw new IllegalArgumentException("Context does not wrap a webdriver: " + context);
    }

    return ((WrapsDriver) context).getWrappedDriver();
  }

  public static WebElement findVisibleElement(By by) {
    return findVisibleElement(DriverFactory.getDriver(), by);
  }

  public static WebElement findVisibleElement(SearchContext fromNode, By by) {
    WebElement webElement = null;
    List<WebElement> foundElements = fromNode.findElements(by);
    for (WebElement element : foundElements) {
      if (element.isDisplayed()) {
        webElement = element;
        break;
      }
    }
    return webElement;
  }
}
