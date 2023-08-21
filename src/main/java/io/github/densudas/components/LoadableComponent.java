package io.github.densudas.components;

public abstract class LoadableComponent<T extends LoadableComponent<T>> {

  protected LoadableComponent() {
    waitToLoad();
  }

  abstract T waitToLoad();
}
