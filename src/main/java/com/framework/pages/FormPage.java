package com.framework.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FormPage extends  BasePage {

    @FindBy(id="firstname")
    private WebElement firstname;

    @FindBy(id="middlename")
    private WebElement middlename;

    @FindBy(id="lastname")
    private WebElement lastname;

    @FindBy(id="email")
    private WebElement email;

    @FindBy(id="password")
    private WebElement password;

    @FindBy(id = "address")
    private WebElement address;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "states")
    private WebElement states;

    @FindBy(id = "pincode")
    private WebElement pincode;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(id = "message")
    private WebElement message;

    public void fillForm(String firstname,String middlename,String lastname,String email
    ,String password,String address,String city, String states,String pincode){
        type(this.firstname,firstname);
        type(this.middlename,middlename);
        type(this.lastname,lastname);
        type(this.email,email);
        type(this.password,password);
        type(this.address,address);
        type(this.city,city);
        type(this.states,states);
        type(this.pincode,pincode);
        click(submitButton);
    }

    public String getSubmissionmessage(){
        return  getText(message);
    }

    @Override
    public boolean isLoaded() {
        return false;
    }
}