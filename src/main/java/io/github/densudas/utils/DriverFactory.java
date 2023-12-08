package io.github.densudas.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * The DriverFactory class provides methods for managing WebDriver instances.
 */
public class DriverFactory {

  private static final Map<Long, WebDriver> WEB_DRIVER_LIST = new HashMap<>();
  private static final Map<Long, WebDriverManager> WEB_DRIVER_MANAGER_LIST = new HashMap<>();
  private static final boolean RUN_IN_DOCKER = false;
  private static final boolean DOCKER_VNC = false;

  private DriverFactory() {
  }

  /**
   * Retrieves the WebDriver instance associated with the current thread. If an instance does not exist, a new one is created.
   *
   * @return the WebDriver instance
   * @throws IllegalStateException if the creation of a new WebDriver instance fails
   */
  public static WebDriver getDriver() {
    long currentThreadId = Thread.currentThread().threadId();
    if (WEB_DRIVER_LIST.get(currentThreadId) == null) {
      try {
        newDriverInstance(BrowserType.CHROME);
      } catch (Exception e) {
        throw new IllegalStateException("Failed to create a new WebDriver instance: " + e.getMessage());
      }
    }
    return WEB_DRIVER_LIST.get(currentThreadId);
  }

  public static void closeAllDrivers() {
    WEB_DRIVER_LIST.values().stream().filter(Objects::nonNull).forEach(WebDriver::quit);
    WEB_DRIVER_MANAGER_LIST.values().stream().filter(Objects::nonNull).forEach(WebDriverManager::quit);
  }

  /**
   * Creates a new instance of a WebDriver based on the specified browser type.
   *
   * @param browserType the type of browser to create the WebDriver instance for
   */
  private static void newDriverInstance(BrowserType browserType) {
    WebDriver driver = null;

    if (Objects.requireNonNull(browserType) == BrowserType.CHROME) {

      if (RUN_IN_DOCKER) {
        WebDriverManager wdm = WebDriverManager.chromedriver().browserInDocker();
        if (DOCKER_VNC) wdm.enableVnc();

        WEB_DRIVER_MANAGER_LIST.put(Thread.currentThread().threadId(), wdm);
        driver = wdm.create();
      } else {
        WebDriverManager.chromedriver().setup();
        driver = getChromeDriverInstance();
      }

    } else if (browserType == BrowserType.FIREFOX) {
      WebDriverManager.firefoxdriver().setup();
      driver = new FirefoxDriver();
    }

    WEB_DRIVER_LIST.put(Thread.currentThread().threadId(), driver);
  }

  private static ChromeDriver getChromeDriverInstance() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");
    options.addArguments("--ignore-ssl-errors=yes");
    options.addArguments("--ignore-certificate-errors");
    return new ChromeDriver(options);
  }
}
