package io.github.densudas.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

  private static final Map<Long, WebDriver> WEB_DRIVER_LIST = new HashMap<>();
  private static final Map<Long, WebDriverManager> WEB_DRIVER_MANAGER_LIST = new HashMap<>();
  private static boolean runInDocker = false;
  private static boolean dockerVnc = false;

  public static WebDriver getDriver() {
    if (WEB_DRIVER_LIST.get(Thread.currentThread().getId()) == null) {
      newDriverInstance(BrowserType.CHROME);
    }
    return WEB_DRIVER_LIST.get(Thread.currentThread().getId());
  }

  public static void closeAllDrivers() {
    WEB_DRIVER_LIST.values().stream().filter(Objects::nonNull).forEach(WebDriver::quit);
    WEB_DRIVER_MANAGER_LIST.values().stream().filter(Objects::nonNull).forEach(WebDriverManager::quit);
  }

  private static void newDriverInstance(BrowserType browserType) {
    switch (browserType) {
      case CHROME -> {
        WebDriver driver;
        if (runInDocker) {
          WebDriverManager wdm = WebDriverManager.chromedriver().browserInDocker();
          if (dockerVnc) wdm.enableVnc();

          WEB_DRIVER_MANAGER_LIST.put(Thread.currentThread().getId(), wdm);
          driver = wdm.create();
        } else {
          WebDriverManager.chromedriver().setup();
          driver = getChromeDriverInstance();
        }
        WEB_DRIVER_LIST.put(Thread.currentThread().getId(), driver);
      }
      case FIREFOX -> {
        WebDriverManager.firefoxdriver().setup();
        WEB_DRIVER_LIST.put(Thread.currentThread().getId(), new FirefoxDriver());
      }
    }
  }

  private static ChromeDriver getChromeDriverInstance() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("start-maximized");
    options.addArguments("ignore-ssl-errors=yes");
    options.addArguments("ignore-certificate-errors");
    return new ChromeDriver(options);
  }
}
