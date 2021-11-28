package io.github.densudas.controls;

import io.github.densudas.ControlSort;
import io.github.densudas.Locator;
import io.github.densudas.Locators;
import io.github.densudas.utils.ControlsStorage;
import io.github.densudas.utils.DriverFactory;
import io.github.densudas.utils.LocatorMatcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.github.densudas.utils.Utils.findVisibleElement;

public abstract class BaseControl {

  private boolean searchFromRootNode;
  protected ControlSort controlSort;
  protected ControlType controlType;
  protected String name;
  protected Locator locator;
  private Locator parentLocator;
  private boolean hasShadowRoot;
  protected int index = 1;
  protected boolean saveToControlsStorage;
  protected boolean searchControlInStorage = true;
  protected String location;

  protected WebElement webElement;
  protected Throwable error;

  protected void findLocators() {

    if (!searchControlInStorage) {

      if (locator == null) {
        throw new IllegalArgumentException(
            "Locator is empty. Please define a Locator instance to execute search by Locator.");
      }
      webElement = findElementByLocator(locator);
      controlSort = locator.getControlSort();

    } else {

      var locator = new ControlsStorage().getLocatorFromStorage(location, controlType, name);
      if (locator != null) {
        webElement = findElementByLocator(locator);
        controlSort = locator.getControlSort();

      } else {
        List<Locator> locators = Locators.getLocatorsByType(controlType);

        for (Locator locatorToBeAssembled : locators) {
          webElement = findElementByLocator(locatorToBeAssembled);
          controlSort = locatorToBeAssembled.getControlSort();
          if (isElementFound()) break;
        }
      }
    }
  }

  private WebElement findElementByLocator(Locator locator) {
    SearchContext searchFromNode = DriverFactory.getDriver();
    if (locator.getParentLocator() != null) {
      searchFromNode = findElementByLocator(locator.getParentLocator());
    }

    WebElement webElement;
    var by = new LocatorMatcher(locator.getBy()).formatWithName(name);
    webElement = findVisibleElement(searchFromNode, by);
    return webElement;
  }

  public boolean isElementFound() {
    return webElement != null;
  }

  public WebElement getWebElement() {
    return webElement;
  }

  public ControlType getControlType() {
    return controlType;
  }

  public boolean getShadowRoot() {
    return hasShadowRoot;
  }

  public Locator getLocator() {
    return locator;
  }

  public boolean getSaveToControlsStorage() {
    return saveToControlsStorage;
  }

  public String getName() {
    return name;
  }

  public Throwable getError() {
    return error;
  }

  public String getLocation() {
    return location;
  }

  public boolean getSearchFromRootNode() {
    return searchFromRootNode;
  }
}
