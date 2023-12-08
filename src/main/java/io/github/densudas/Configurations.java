package io.github.densudas;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

// Configurations.java
public class Configurations {
  private static final Properties props;

  static {
    props = new Properties();
    try (InputStream input = Configurations.class.getClassLoader().getResourceAsStream("config.properties")) {
      props.load(input);
    } catch (IOException ex) {
      throw new UncheckedIOException("Could not load configuration file", ex);
    }
  }

  public static int getTimeoutValue() {
    return Integer.parseInt(props.getProperty("timeoutInSeconds"));
  }

  public static int getPollingInterval() {
    return Integer.parseInt(props.getProperty("pollingIntervalInSeconds"));
  }
}
