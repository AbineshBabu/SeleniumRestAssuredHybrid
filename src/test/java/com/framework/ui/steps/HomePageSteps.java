package com.framework.ui.steps;


import com.framework.config.ConfigManager;
import com.framework.pages.*;
import com.framework.utils.ScenarioContext;
import com.framework.utils.ScenarioContext.Key;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.awt.*;
import java.util.List;

public class HomePageSteps {

    public ConfigManager config;
    public ScenarioContext context;
    public HomePage homePage;
    public FormPage formPage;
    public WebTable webTable;
    public IframePage iframePage;
    public ShadowDomPage shadowDomPage;
    public AlertPage alertPage;
    public ModalPopPage modalPopPage;

    public Faker faker=new Faker();

    public  HomePageSteps(ScenarioContext context){
        this.config=ConfigManager.ConfigProvider.getInstance();
        this.context=context;
    }


    @Given("the user navigate to homepage")
    public void the_user_navigate_to_homepage() {
        homePage = new HomePage();
        homePage.navigateTo(config.uiBaseUrl());

        context.set(Key.HOME_PAGE,homePage);
    }
    @When("the user click on form page")
    public void the_user_click_on_form_page() {
        homePage = context.get(Key.HOME_PAGE);
        formPage=homePage.openForm();

        context.set(Key.FORM_PAGE,formPage);
    }
    @When("the user fills the form with valid data")
    public void the_user_fills_the_form_with_valid_data() {
        formPage = context.get(Key.FORM_PAGE);

        formPage.fillForm(faker.name().firstName(),faker.name().nameWithMiddle()
        ,faker.name().lastName(),"abi"+faker.number().randomDigit()+"@gmail.com"
        ,faker.bothify("??????????"), String.valueOf(faker.address()),faker.address().city()
        ,faker.address().state(),faker.address().zipCode().replace("-",""));
    }
    @When("verify the system displays {string}")
    public void verify_the_system_displays(String expectedMessage) {
        formPage = context.get(Key.FORM_PAGE);

        Assert.assertEquals(formPage.getSubmissionmessage(),expectedMessage);

    }

    @When("the user click on web table")
    public void theUserClickOnWebTable() {
        homePage = context.get(Key.HOME_PAGE);
        webTable = homePage.openWebTable();

        context.set(Key.WEBTABLE_PAGE,webTable);
    }

    @And("the user search by enter letter {string}")
    public void theUserSearchByEnterLetter(String searchText) {
        webTable = context.get(Key.WEBTABLE_PAGE);
        webTable.searchEntry(searchText);
    }


    @And("verify the name is {string} where the country is {string}")
    public void verifyTheNameIsWhereTheCountryIs(String name, String country) {
        webTable =context.get(Key.WEBTABLE_PAGE);

        List<WebElement> rows=webTable.returnTable();

        for(WebElement row:rows){
            List<WebElement> cell=row.findElements(By.tagName("td"));

            if (cell.size() > 0){
                String nameIntable = cell.get(1).getText();
                String countrInTable = cell.get(2).getText();

                if (countrInTable == country){
                    Assert.assertEquals(nameIntable,name);
                    break;
                }

            }

        }


    }

    @When("the user click on iFrame")
    public void theUserClickOnIFrame() {
            homePage = context.get(Key.HOME_PAGE);
            iframePage=homePage.openIFrame();

            context.set(Key.IFRAME_PAGE,iframePage);
    }

    @Then("the user click on Click Me button on iframe{int}")
    public void theUserClickOnClickMeButtonOnIframe(int arg0) {
            iframePage = context.get(Key.IFRAME_PAGE);
            iframePage.clickMeIframe1();
    }


    @And("verify the {string} text")
    public void verifyTheText(String expectetext) {
        iframePage =context.get(Key.IFRAME_PAGE);

        Assert.assertEquals(iframePage.getResultTest(),expectetext);
    }

    @When("the user click on shadow dom")
    public void theUserClickOnShadowDom() {
        homePage = context.get(Key.HOME_PAGE);
        shadowDomPage= homePage.openShadowDom();

        context.set(Key.SHADOWDOM_PAGE,shadowDomPage);

    }

    @And("verify the {string} text inside shadow dom")
    public void verifyTheTextInsideShadowDom(String expectedText) {
        shadowDomPage =context.get(Key.SHADOWDOM_PAGE);

        Assert.assertEquals(shadowDomPage.getTextInsideShadow(),expectedText);
    }

    @When("the user click on alert")
    public void theUserClickOnAlert() {
        homePage = context.get(Key.HOME_PAGE);
        alertPage=homePage.openAlert();

        context.set(Key.ALERT_PAGE,alertPage);
    }

    @When("the user click on alert page")
    public void theUserClickOnAlertPage() {
        homePage = context.get(Key.HOME_PAGE);
        alertPage=homePage.openAlert();

        context.set(Key.ALERT_PAGE,alertPage);
    }

    @And("the user click on alert button")
    public void theUserClickOnAlertButton() {
        alertPage = context.get(Key.ALERT_PAGE);
        alertPage.clickOnShowAlert();

    }

    @Then("the user verifies {string} text and accept it")
    public void theUserVerifiesTextAndAcceptIt(String expectedText) {
        alertPage = context.get(Key.ALERT_PAGE);
        Assert.assertEquals(alertPage.getAlertText(),expectedText);
        alertPage.acceptAlert();
    }

    @And("the user click on confirm button")
    public void theUserClickOnConfirmButton() {
        alertPage = context.get(Key.ALERT_PAGE);
        alertPage.clickOnShowConfirm();
    }

    @Then("the user verifies {string} text and cancel it")
    public void theUserVerifiesTextAndCancelIt(String expectedText) {
        alertPage = context.get(Key.ALERT_PAGE);

        Assert.assertEquals(alertPage.getConfirmText(),expectedText);
        alertPage.cancelConfirm();
    }


    @And("the user click on prompt button")
    public void theUserClickOnPromptButton() {
        alertPage = context.get(Key.ALERT_PAGE);
        alertPage.clickOnShowPrompt();
    }


    @Then("the user verifies {string} text")
    public void theUserVerifiesText(String expectedText) {
        alertPage = context.get(Key.ALERT_PAGE);

        Assert.assertEquals(alertPage.getPromptText(),expectedText);
    }

    @And("the user enter {string} in the prompt and accept it")
    public void theUserEnterInThePromptAndAcceptIt(String enteringText) {
        alertPage = context.get(Key.ALERT_PAGE);
        alertPage.enterInPrompt(enteringText);
        alertPage.acceptPrompt();
    }

    @When("the user click on modal popup page")
    public void theUserClickOnModalPopupPage() throws InterruptedException, AWTException {
        homePage = context.get(Key.HOME_PAGE);

        modalPopPage=homePage.openModalPop();

        Thread.sleep(5000);

        context.set(Key.MODALPOP_PAGE,modalPopPage);
    }

    @And("then the user switch to second window")
    public void thenTheUserSwitchToSecondWindow() throws InterruptedException {
        modalPopPage = context.get(Key.MODALPOP_PAGE);

        modalPopPage.switchToActiveTab();
    }
}
