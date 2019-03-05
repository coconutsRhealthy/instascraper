package cucumber.impl;

import config.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class TestStepsImpl {

    Browser browser;

    public boolean openBrowser() {
        try {
            browser = Browser.getInstance();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean goToUrl(String url) {
        try {
            browser.getDriver().get(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean openBrowserAndGoToUrl(String url) {
        try {
            openBrowser();
            goToUrl(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean logoIsPresent() {
        try {
            browser.getDriver().findElement(By.cssSelector("#logo > img"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean clickOnFinanceButton() {
        try {
            WebElement financeButton = browser.getDriver().findElement(By.cssSelector("#mainMenu > ul > li:nth-child(4) > a"));
            financeButton.click();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean clickOnInvestorDayButton() {
        try {
            WebElement investorDayButton = browser.getDriver().findElement(By.cssSelector("#subMenu > div > div.column.small-4.first > h3:nth-child(3) > a"));
            investorDayButton.click();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean investorDayPageIsDisplayed() {
        try {
            WebElement h1Element = browser.getDriver().findElement(By.cssSelector("#mainContainer > div.row > div > div > h1 > span"));
            h1Element.getText().equals("Investor Day");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
