package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Collections;
import java.util.List;

public class WebTable extends BasePage{

    @FindBy(css = "#dataTable tbody tr")
    private WebElement table;

    @FindBy(id = "searchInput")
    private WebElement search;

    public List<WebElement> returnTable(){
        return driver.findElements(By.cssSelector("#dataTable tbody tr"));
    }

    public void searchEntry(String searchText){
        type(search,searchText);
    }

    @Override
    public boolean isLoaded() {
        return false;
    }
}
