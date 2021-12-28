package com.app.util;


import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class WebDriverFactory {

    private static final InheritableThreadLocal<WebDriver> localDriver = new InheritableThreadLocal<>();
    private static List<String> listOfBrowsers;
    private static boolean randomizedBrowser;
    private static String defaultBrowser;

    @Value("#{'${test.browser.list}'.split(',')}")
    public void setBrowserList(List<String> browserList) {
        listOfBrowsers = browserList;
    }

    @Value("${test.browser.randomize}")
    public void setRandomizedBrowser(boolean randomizedBrowser) {
        WebDriverFactory.randomizedBrowser = randomizedBrowser;
    }

    @Value("${test.browser.default}")
    public void setRandomizedBrowser(String defaultBrowser) {
        WebDriverFactory.defaultBrowser = defaultBrowser;
    }

    public static WebDriver getDriver() {
        if (localDriver.get() != null) {
            return localDriver.get();
        } else {
            log.error("WebDriver is null.");
            throw new RuntimeException("WebDriver is null. Check the WebDriverFactory");
        }
    }

    public static void createDriver() {
        String browserType = defaultBrowser;
        if (randomizedBrowser) {
            int randomBrowser = new Random().nextInt(listOfBrowsers.size());
            browserType = listOfBrowsers.get(randomBrowser);
        }
        log.info("{} browser type is used", browserType);
        if (Browser.CHROME.is(browserType)) {
            localDriver.set(setUpLocalChromeDriver());
        } else {
            log.info("Unsupported browser was provided.");
            throw new IllegalArgumentException("Please provide a supported browser.");
        }
    }

    public static void cleanUpDriver() {
        quitDriver();
        removeDriver();
    }

    private static void quitDriver() {
        if (localDriver.get() != null) {
            localDriver.get().quit();
            log.info("Quit the driver");
        }
    }

    private static void removeDriver() {
        if (localDriver.get() != null) {
            localDriver.remove();
            log.info("Removing the driver");
        }
    }

    private static WebDriver setUpLocalChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        setUpBrowserPreferences(driver);
        return driver;
    }

//    private static WebDriver setUpLocalFirefoxDriver() {
//        WebDriverManager.firefoxdriver().setup();
//        FirefoxOptions options = new FirefoxOptions();
//        return new FirefoxDriver(options);
//    }
//
//    private static WebDriver setUpLocalSafariDriver() {
//        WebDriverManager.safaridriver().setup();
//        SafariOptions options = new SafariOptions();
//        return new SafariDriver(options);
//    }

    private static void setUpBrowserPreferences(WebDriver driver) {
        driver.manage().window().fullscreen();
        driver.manage().deleteAllCookies();
    }
}
