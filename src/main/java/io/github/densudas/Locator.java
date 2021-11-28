package io.github.densudas;

import io.github.densudas.controls.ControlType;
import org.openqa.selenium.By;

public class Locator {

  private boolean searchFromRootNode;
  private ControlSort controlSort;
  private ControlType controlType;
  private By by;
  private Locator parentLocator;
  private boolean hasShadowRoot;
  private int index = 1;

  public Locator(ControlSort controlSort, By by) {
    this.controlSort = controlSort;
    this.controlType = controlSort.getControlType();
    this.by = by;
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

  public By getBy() {
    return by;
  }

  public Locator setBy(By by) {
    this.by = by;
    return this;
  }

  public Locator getParentLocator() {
    return parentLocator;
  }

  public Locator setParentLocator(Locator parentLocator) {
    this.parentLocator = parentLocator;
    return this;
  }

  public ControlType getControlType() {
    return controlType;
  }

  public void setControlType(ControlType controlType) {
    this.controlType = controlType;
  }
}
