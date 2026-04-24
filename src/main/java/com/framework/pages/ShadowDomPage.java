package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ShadowDomPage extends BasePage{

    @FindBy(id="shadow-host")
    private WebElement shadowHost;

    private final By shadowHost1= By.id("shadow-host");


    public String getTextInsideShadow(){
        WebElement element=driver.findElement(shadowHost1);
        SearchContext context =element.getShadowRoot();
        WebElement elementInside=context.findElement(By.className("box"));
        return  getText(elementInside);
    }

    @Override
    public boolean isLoaded() {
        return false;
    }
}
