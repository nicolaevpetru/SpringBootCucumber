package com.app.steps;

import com.app.util.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Hooks {

    @Before("@ui")
    public void browserSetUp(){
        WebDriverFactory.createDriver();
    }

    @After("@ui")
    public void tearDownBrowser(){
        WebDriverFactory.cleanUpDriver();
    }
}
