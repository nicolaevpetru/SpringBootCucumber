package com.app.steps.ui;

import com.app.util.WebDriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

public class SearchSteps {

    @When("^I navigate to (.*)")
    public void iNavigateTo(String navigateTo) {
        WebDriverFactory.getDriver().get(navigateTo);
    }

    @Then("^I search for (.*)")
    public void iSearchFor(String search) {
        WebDriverFactory.getDriver().findElement(By.name("q")).sendKeys(search);
    }
}
