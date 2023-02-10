package io.github.densudas;

import io.github.densudas.utils.DriverFactory;
import io.github.densudas.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class TestListener implements ITestListener {

  private void takeScreenshot() {
    TakesScreenshot takesScreenshot = (TakesScreenshot) DriverFactory.getDriver();
    File SrcFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
    String path = Utils.USER_DIR + Utils.FILE_SEPARATOR + "test_results";
    try {
      Files.createDirectories(Paths.get(path));
      File DestFile = new File(path + Utils.FILE_SEPARATOR + SrcFile.getName());
      FileUtils.copyFile(SrcFile, DestFile);
    } catch (IOException e) {
      System.out.println("Screenshot save failed. " + e.getMessage());
    }
  }

  @Override
  public void onTestFailure(ITestResult arg0) {
    takeScreenshot();
  }
}
