package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IframePage extends BasePage{

    @FindBy(name = "iframe1")
    private WebElement frame1;

    @FindBy(name = "iframe2")
    private WebElement frame2;

    @FindBy(id="message")
    private WebElement resultText;

    public String getResultTest(){
        return getText(resultText);
    }

    public void clickMeIframe1(){
        driver.switchTo().frame(frame2).findElement(By.tagName("button")).click();
        driver.switchTo().defaultContent();
    }


    @Override
    public boolean isLoaded() {
        return false;
    }
}