package cucumber.steps;
import config.Browser;
import config.TestProperties;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class BasicSteps {
    /**
     * Loads the properties from the relevant .properties file in order to use them in the tests.
     * On localhost, local.properties is loaded.
     * On other environments, environment.properties is loaded
     */
    @Before(order = 10)
    public void readEnvironmentProperties() {
        TestProperties.getInstance();
    }

    /**
     * Causes the browser class to create an instance if one does not already exist.
     */
    @Before(order = 20)
    public void startBrowserIfNotStarted() {
        if (Browser.getCurrentInstance() == null) {
            Browser.getInstance();
        }
    }

    /**
     * Closes the browser after tests
     */
    @After(order = 10)
    public void closeBrowser() {
        Browser.getCurrentInstance().exit();
    }
}
