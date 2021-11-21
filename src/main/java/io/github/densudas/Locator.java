package io.github.densudas;

import org.openqa.selenium.By;

public class Locator {

  private boolean searchFromRootNode;
  private ControlSort controlSort;
  private By locator;
  private Locator parentLocator;
  private boolean hasShadowRoot; // does control have shadow root
  // a sequence number of controls with the same name on the page (starts with 1)
  private int index = 1;
  // save the control in the repository
  private boolean saveToControlsStorage;

  public Locator() {}

  public Locator(ControlSort controlSort, By locator) {
    this.controlSort = controlSort;
    this.locator = locator;
  }

  public boolean isSearchFromRootNode() {
    return searchFromRootNode;
  }

  public Locator setSearchFromRootNode(boolean searchFromRootNode) {
    this.searchFromRootNode = searchFromRootNode;
    return this;
  }

  public ControlSort getControlSort() {
    return controlSort;
  }

  public Locator setControlSort(ControlSort controlSort) {
    this.controlSort = controlSort;
    return this;
  }

  public boolean isHasShadowRoot() {
    return hasShadowRoot;
  }

  public Locator setHasShadowRoot(boolean hasShadowRoot) {
    this.hasShadowRoot = hasShadowRoot;
    return this;
  }

  public int getIndex() {
    return index;
  }

  public Locator setIndex(int index) {
    this.index = index;
    return this;
  }

  public boolean isSaveToControlsStorage() {
    return saveToControlsStorage;
  }

  public Locator setSaveToControlsStorage(boolean saveToControlsStorage) {
    this.saveToControlsStorage = saveToControlsStorage;
    return this;
  }

  public By getLocator() {
    return locator;
  }

  public Locator setLocator(By locator) {
    this.locator = locator;
    return this;
  }

  public Locator getParentLocator() {
    return parentLocator;
  }

  public Locator setParentLocator(Locator parentLocator) {
    this.parentLocator = parentLocator;
    return this;
  }
}
