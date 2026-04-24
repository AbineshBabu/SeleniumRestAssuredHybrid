package com.framework.pages;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// ── ModalPopPage.java ────────────────────────────────────────────
public class ModalPopPage extends BasePage {

    private final String originalHandle; // ✅ store original handle

    public ModalPopPage(String originalHandle) {
        super();
        this.originalHandle = originalHandle;
    }

    public void switchToActiveTab() {

        // ✅ Wait until new tab is registered
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.getWindowHandles().size() > 1);

        log.info("Total handles: {}", driver.getWindowHandles().size());
        log.info("Original handle: {}", originalHandle);

        // ✅ Filter out original — don't rely on index
        driver.getWindowHandles()
                .stream()
                .filter(h -> !h.equals(originalHandle))
                .findFirst()
                .ifPresent(h -> {
                    driver.switchTo().window(h);
                    log.info("Switched to new tab: {}", driver.getTitle());
                });
    }

    public void switchBackToOriginal() {
        driver.switchTo().window(originalHandle);
        log.info("Switched back to: {}", driver.getTitle());
    }

    @Override
    public boolean isLoaded() { return false; }
}