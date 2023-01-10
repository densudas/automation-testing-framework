package io.github.densudas.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {

  private static final Map<Long, WebDriver> WEB_DRIVER_LIST = new HashMap<>();

  public static WebDriver getDriver() {
    if (WEB_DRIVER_LIST.get(Thread.currentThread().getId()) == null) {
      newDriverInstance();
    }
    return WEB_DRIVER_LIST.get(Thread.currentThread().getId());
  }

  public static void closeAllDrivers() {
    WEB_DRIVER_LIST.values().stream().filter(Objects::nonNull).forEach(WebDriver::quit);
  }

  private static void newDriverInstance() {
    WebDriverManager.chromedriver().setup();
    WEB_DRIVER_LIST.put(Thread.currentThread().getId(), new ChromeDriver());
  }
}
