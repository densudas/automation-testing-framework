package io.github.densudas.utils;

import com.google.common.reflect.ClassPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;

import java.io.IOException;

public class Utils {

  public static Class findClassByClassName(String className) throws IOException {
    var locationRa = className.split("\\.");
    String finalClassName = locationRa[locationRa.length - 1] + "Enums";

    return ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
        .filter(
            clazz -> {
              var name = clazz.getName().split("\\.");
              return name[name.length - 1].equalsIgnoreCase(finalClassName);
            })
        .map(ClassPath.ClassInfo::load)
        .findFirst()
        .orElse(null);
  }

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
}
