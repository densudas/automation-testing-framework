package io.github.densudas;

import java.util.Locale;

public interface TranslatableLocatorName extends LocatorName {

  String getName(Locale locale);
}
