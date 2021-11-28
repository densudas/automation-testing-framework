package io.github.densudas.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DriverFactory {

  private static Map<Long, WebDriver> webDriverList = new HashMap<>();

  public static WebDriver getDriver() {
    if (webDriverList.get(Thread.currentThread().getId()) == null) {
      newDriverInstance();
    }
    return webDriverList.get(Thread.currentThread().getId());
  }

  public static void closeAllDrivers() {
    webDriverList.values().stream().filter(Objects::nonNull).forEach(WebDriver::close);
  }

  private static void newDriverInstance() {
    WebDriverManager.chromedriver().setup();
    webDriverList.put(Thread.currentThread().getId(), new ChromeDriver());
  }
}
