package cucumber.steps;

import config.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.impl.TestStepsImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class TestSteps {

    private Connection con;

    List<String> allFollowingStrings = new ArrayList<>();

    TestStepsImpl testStepsImpl = new TestStepsImpl();

    @Given("^A visitor is on the Ingenico homepage$")
    public void aVisitorIsOnTheIngenicoHomepage() {
        assertTrue(testStepsImpl.openBrowserAndGoToUrl("http://www.ingenico.com"));
    }

    @Then("^The visitor sees the Ingenico logo$")
    public void theVisitorSeesTheIngenicoLogo() {
        assertTrue(testStepsImpl.logoIsPresent());
    }

    @When("^The visitor clicks on 'Finance' in the topmenu$")
    public void theVisitorClicksOnFinanceInTheTopmenu() {
        assertTrue(testStepsImpl.clickOnFinanceButton());
    }

    @And("^The visitor clicks on 'Investor Day'$")
    public void theVisitorClicksOnInvestorDay() {
        assertTrue(testStepsImpl.clickOnInvestorDayButton());
    }

    @Then("^The 'Investor Day' page will be shown$")
    public void theInvestorDayPageWillBeShown() {
        assertTrue(testStepsImpl.investorDayPageIsDisplayed());
    }

    @Given("^A visitor opens a browser$")
    public void aVisitorOpensABrowser() {
        assertTrue(testStepsImpl.openBrowser());
    }

    @When("^The visitor navigates to \"([^\"]*)\"$")
    public void theVisitorNavigatesTo(String url) {
        try {
            theWholeMethod();
        } catch (Exception e) {

        }





//        url = "http://www.instagram.com/dutchtoy";
//
//        testStepsImpl.goToUrl(url);
//
//        String followerButtonXpath = "//a[contains(@href, 'following')]";
//        String xpath2 = "//a[contains(@class, 'notranslate')]";
//
//        WebElement followerButton = Browser.getInstance().getDriver().findElement(By.xpath(followerButtonXpath));
//        followerButton.click();
//
//        System.out.println("sjaak");


        //teststeps

    }

    private void theWholeMethod() throws Exception {
        testStepsImpl.goToUrl("http://www.instagram.com/ordinanl");
        logInToInstagram();
        getFollowing();

        for(int i = 0; i < 50; i++) {
            testStepsImpl.goToUrl("http://www.instagram.com/" + allFollowingStrings.get(i));
            getFollowing();
        }

        storeUsernamesInDb();
    }

    private void logInToInstagram() throws Exception {
        Browser browser = Browser.getCurrentInstance();

        WebElement loginButton = browser.getDriver().findElement(By.xpath("//button[text()='Log In']"));
        loginButton.click();

        TimeUnit.MILLISECONDS.sleep(500);
        WebElement usernameField = browser.getDriver().findElement(By.xpath("//input[@name='username']"));
        TimeUnit.MILLISECONDS.sleep(300);
        usernameField.click();
        TimeUnit.MILLISECONDS.sleep(200);
        usernameField.sendKeys("influencertjes");

        TimeUnit.MILLISECONDS.sleep(500);
        WebElement passwordField = browser.getDriver().findElement(By.xpath("//input[@name='password']"));
        TimeUnit.MILLISECONDS.sleep(300);
        passwordField.click();
        TimeUnit.MILLISECONDS.sleep(200);
        passwordField.sendKeys("Vuurwerk00");


        TimeUnit.MILLISECONDS.sleep(200);
        WebElement loginButtonReal = browser.getDriver().findElement(By.xpath("//div[text()='Log in']"));
        loginButtonReal.click();

        TimeUnit.MILLISECONDS.sleep(200);
    }

    private boolean accountIsPrivate() {
        boolean accountIsPrivate;

        Browser browser = Browser.getCurrentInstance();

        try {
            browser.getDriver().findElement(By.xpath("//h2[text()='This Account is Private']"));
            accountIsPrivate = true;
        } catch (NoSuchElementException e) {
            accountIsPrivate = false;
        }

        return accountIsPrivate;
    }

    private void getFollowing() throws Exception {
        Browser browser = Browser.getCurrentInstance();

        try {
            WebElement followingButton = browser.getDriver().findElement(By.xpath("//a[contains(@href, 'following')]"));
            TimeUnit.MILLISECONDS.sleep(200);
            followingButton.click();

            System.out.println("wachff");

            List<WebElement> allFollowing = browser.getDriver().findElements(By.xpath("//a[contains(@class, 'notranslate')]"));

            for(WebElement webElement : allFollowing) {
                allFollowingStrings.add(webElement.getText());
            }
        } catch (NoSuchElementException e) {
            return;
        }
    }

    private void printAllFollowing() {
        for(String user : allFollowingStrings) {
            System.out.println(user);
        }
    }

    private void storeUsernamesInDb() throws Exception {
        initializeDbConnection();

        Statement st = con.createStatement();

        for(String user : allFollowingStrings) {
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE username = '" + user + "';");

            if(!rs.next()) {
                st.executeUpdate("INSERT INTO users (username) VALUES ('" + user + "')");
            }

            rs.close();
        }

        st.close();

        closeDbConnection();
    }

    private void initializeDbConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/insta?&serverTimezone=UTC", "root", "");
    }

    private void closeDbConnection() throws SQLException {
        con.close();
    }
}
