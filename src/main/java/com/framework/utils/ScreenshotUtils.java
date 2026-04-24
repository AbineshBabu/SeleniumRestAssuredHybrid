package com.framework.utils;

import com.framework.constants.FrameworkConstants;
import com.framework.driver.DriverManager;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtils
 * Captures screenshots and embeds them into Cucumber HTML reports.
 *
 * Interview Tip: scenario.attach() embeds screenshot as bytes directly —
 * no external file path needed, works cleanly in CI/CD pipelines.
 * Always cast with instanceof — not all drivers implement TakesScreenshot.
 */
public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {}

    /** Embeds screenshot directly into the Cucumber scenario report. */
    public static void captureAndEmbedScreenshot(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot: " + scenario.getName());
            log.info("Screenshot embedded for scenario: {}", scenario.getName());
        } else {
            log.warn("Driver does not support screenshots");
        }
    }

    /** Saves a screenshot to disk and returns its absolute path. */
    public static String captureToFile(String name) {
        WebDriver driver = DriverManager.getDriver();
        if (!(driver instanceof TakesScreenshot)) return null;
        try {
            Path dir = Paths.get(FrameworkConstants.SCREENSHOT_DIR);
            Files.createDirectories(dir);
            String fileName = name + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
            Path filePath = dir.resolve(fileName);
            Files.write(filePath, ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            log.info("Screenshot saved: {}", filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage());
            return null;
        }
    }
}
