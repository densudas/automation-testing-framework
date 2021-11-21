package io.github.densudas.components;

public abstract class LoadableComponent<T extends LoadableComponent<T>> {

  public LoadableComponent() {
    waitToLoad();
  }

  abstract T waitToLoad();
}
