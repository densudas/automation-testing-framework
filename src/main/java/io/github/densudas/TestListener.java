package io.github.densudas;

import io.github.densudas.utils.DriverFactory;
import io.github.densudas.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class TestListener implements ITestListener {

  Logger logger = Logger.getGlobal();

  private void takeScreenshot() {
    TakesScreenshot takesScreenshot = (TakesScreenshot) DriverFactory.getDriver();
    File srcFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
    String path = Utils.USER_DIR + Utils.FILE_SEPARATOR + "test_results";
    try {
      Path dirPath = Files.createDirectories(Paths.get(path));
      if (Files.exists(dirPath)) {
        Path destPath = Paths.get(path, srcFile.getName());
        Files.copy(srcFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(destPath)) {
          logger.log(new LogRecord(Level.INFO, "File copied successfully"));
        } else {
          throw new IOException("Failed to copy file to destination");
        }
      }
    } catch (IOException e) {
      logger.log(new LogRecord(Level.WARNING, e.getMessage()));
    }
  }

  @Override
  public void onTestFailure(ITestResult arg0) {
    takeScreenshot();
  }
}
