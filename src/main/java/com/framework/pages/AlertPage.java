package com.framework.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AlertPage extends BasePage{


    @FindBy(css = ".row button:nth-child(1)")
    private WebElement alert;

    @FindBy(css = ".row > div:nth-child(2) button")
    private WebElement confirm;

    @FindBy(css = ".row > div:nth-child(3) button")
    private WebElement prompt;

    public void clickOnShowAlert(){
        click(alert);
    }

    public void clickOnShowConfirm(){
        click(confirm);
    }

    public void clickOnShowPrompt(){
        click(prompt);
    }

    public String getAlertText(){
        return driver.switchTo().alert().getText().trim();
    }

    public void acceptAlert(){
        driver.switchTo().alert().accept();
    }

    public String getConfirmText(){
        return driver.switchTo().alert().getText().trim();
    }

    public void cancelConfirm(){
        driver.switchTo().alert().dismiss();
    }

    public String getPromptText(){
        return driver.switchTo().alert().getText().trim();
    }

    public void enterInPrompt(String value){
        driver.switchTo().alert().sendKeys(value);
    }

    public void acceptPrompt(){
        driver.switchTo().alert().dismiss();
    }


    @Override
    public boolean isLoaded() {
        return false;
    }
}
