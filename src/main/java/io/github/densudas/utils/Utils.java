package io.github.densudas.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class Utils {

  public static final String FILE_SEPARATOR = System.getProperty("file.separator");
  public static final String USER_DIR = System.getProperty("user.dir");

  public static void clickWithJS(final SearchContext context) {
    getJSExecutor(context).executeScript("arguments[0].click()", context);
  }

  public static JavascriptExecutor getJSExecutor(final SearchContext context) {
    return (JavascriptExecutor) getWebDriver(context);
  }

  public static WebDriver getWebDriver(final SearchContext context) {
    if (context instanceof WebDriver) {
      return (WebDriver) context;
    }

    if (!(context instanceof WrapsDriver)) {
      throw new IllegalArgumentException("Context does not wrap a webdriver: " + context);
    }

    return ((WrapsDriver) context).getWrappedDriver();
  }

  public static WebElement findVisibleElement(final By by) {
    return findVisibleElement(DriverFactory.getDriver(), by);
  }

  public static WebElement findVisibleElement(final SearchContext fromNode, By by) {
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

  public static List<String> stringMatch(final String string, final String regex) {
    List<String> groups = new ArrayList<>();
    Matcher matcher = Pattern.compile(regex).matcher(string);
    if (matcher.find()) {
      for (int i = 0; i <= matcher.groupCount(); i++) {
        groups.add(matcher.group(i));
      }
    }
    return groups;
  }

  public static void waitForElementToBeDisplayed(final By by) {
    waitUntil(ExpectedConditions.visibilityOfElementLocated(by));
  }

  public static void waitForElementToBeDisplayed(final WebElement element) {
    waitUntil(ExpectedConditions.visibilityOf(element));
  }

  public static void waitForInvisibilityOfElementLocated(final By by) {
    waitUntil(ExpectedConditions.invisibilityOfElementLocated(by));
  }

  public static <V> V waitUntil(Function<? super WebDriver, V> isTrue) {
    FluentWait<WebDriver> wait = new FluentWait<>(DriverFactory.getDriver());
    wait.withTimeout(Duration.ofSeconds(20));
    wait.pollingEvery(Duration.ofSeconds(1));
    return wait.until(isTrue);
  }
}
