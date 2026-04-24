package com.framework.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HomePage extends BasePage{

    @FindBy(css = ".card-tools >div:nth-child(2) >div")
    private WebElement form;

    @FindBy(css = ".card-tools >div:nth-child(2) >div:nth-child(2)")
    private WebElement webTable;

    @FindBy(css = ".card-tools >div:nth-child(2) >div:nth-child(3)")
    private WebElement iFrame;

    @FindBy(css =".card-tools >div:nth-child(3) >div:nth-child(1)")
    private WebElement shadowDom;

    @FindBy(css =".card-tools >div:nth-child(4) >div:nth-child(1)")
    private WebElement alert;

    @FindBy(css =".card-tools >div:nth-child(5) >div:nth-child(1)")
    private WebElement modalPop;


    public FormPage openForm(){
        click(form);
        return new FormPage();
    }

    public WebTable openWebTable(){
        click(webTable);
        return new WebTable();
    }

    public IframePage openIFrame(){
        click(iFrame);
        return new IframePage();
    }

    public ShadowDomPage openShadowDom(){
        click(shadowDom);
        return new ShadowDomPage();
    }

    public AlertPage openAlert(){
        click(alert);
        return new AlertPage();
    }

    public ModalPopPage openModalPop() throws AWTException {
        String originalHandle = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript("window.focus();");

        new Actions(driver).contextClick(modalPop).perform();

        Robot robot = new Robot();
        robot.delay(1000); // ✅ wait for context menu to render
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.delay(300);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(500);

        return new ModalPopPage(originalHandle);
    }

    @Override
    public boolean isLoaded() {
        return false;
    }
}
