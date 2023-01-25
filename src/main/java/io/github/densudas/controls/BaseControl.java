package io.github.densudas.controls;

import static io.github.densudas.utils.Utils.findVisibleElement;

import io.github.densudas.ControlSort;
import io.github.densudas.Locator;
import io.github.densudas.Locators;
import io.github.densudas.utils.ControlsStorage;
import io.github.densudas.utils.DriverFactory;
import io.github.densudas.utils.LocatorMatcher;
import java.util.List;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public abstract class BaseControl {

  protected ControlSort controlSort;
  protected ControlType controlType;
  protected String name;
  protected Locator locator;
  protected int index = 1;
  protected boolean saveToControlsStorage = true;
  protected boolean searchControlInStorage = true;
  protected String location;
  protected WebElement webElement;
  protected Throwable error;
  private boolean searchFromRootNode;
  private Locator parentLocator;
  private boolean hasShadowRoot;

  protected void findLocators() throws Exception {

    if (!searchControlInStorage) {

      if (locator == null) {
        throw new IllegalArgumentException(
            "Locator is empty. Please define a Locator instance to execute search by Locator.");
      }
      webElement = findElementByLocator(locator);
      controlSort = locator.getControlSort();

    } else {

      var locator = ControlsStorage.getLocatorFromStorage(location, controlType, name);
      if (locator != null) {
        webElement = findElementByLocator(locator);
        controlSort = locator.getControlSort();

      } else {
        List<Locator> locators = Locators.getLocatorsByType(controlType);

        for (Locator locatorToBeAssembled : locators) {
          webElement = findElementByLocator(locatorToBeAssembled);
          controlSort = locatorToBeAssembled.getControlSort();
          if (isElementFound()) {
            this.locator = locatorToBeAssembled;
            ControlsStorage.addControlToStorage(this);
            break;
          }
        }
      }
    }
  }

  private WebElement findElementByLocator(Locator locator) {
    SearchContext searchFromNode = DriverFactory.getDriver();
    if (locator.getParentLocator() != null) {
      searchFromNode = findElementByLocator(locator.getParentLocator());
    }

    var by = new LocatorMatcher(locator.getBy()).formatWithName(name);
    return findVisibleElement(searchFromNode, by);
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

  public ControlType getControlSort() {
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
