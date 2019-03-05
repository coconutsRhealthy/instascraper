package config;

import com.onehippo.cms7.qa.selenium.WebDriverContainer;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Browser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Browser.class);
    private static final int MAX_ATTEMPTS = 3;

    private static volatile Browser instance;

    private WebDriver driver;
    private WebDriverContainer webDriverContainer;
    private boolean initialized;

    /**
     * Private constructor.
     * Prepares a wrapper for the WebDriver that will be used for the test.
     */
    private Browser() {
        this.webDriverContainer = new WebDriverContainer();
        int count = 0;
        while (count < MAX_ATTEMPTS) {
            if (initDriver()) {
                break;
            }
            count++;
        }
        maximizeWindow();
    }


    private boolean initDriver() {
        try {
            this.driver = webDriverContainer.getDriver();
        } catch (UnreachableBrowserException e) {
            LOGGER.error("Can't start a new browser session", e);
        }
        return this.driver != null;
    }

    /**
     * Get an instance of the {@link Browser}
     * @return current Browser
     */
    public static Browser getInstance() {
        if (instance == null || !instance.initialized) {
            instance = new Browser();
            instance.initialized = true;
        }
        return instance;
    }

    /**
     * Reinitialize the browser instance when connection is lost.
     * @return the new instance
     */
    public static Browser reInit() {
        instance = new Browser();
        return instance;
    }

    /**
     * Get an instance of the {@link Browser}
     * Useful when you just want to return the instance, eg in an after hook
     * (you don't want to open a browser in an after hook)
     * @return current Browser
     */
    public static Browser getCurrentInstance() {
        return instance;
    }

    private void maximizeWindow() {
        driver.manage().window().setPosition(new Point(0, 0));
        this.driver.manage().window().maximize();
    }

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Close the browser
     */
    public void exit() {
        if (initialized) {
            initialized = false;
            if (webDriverContainer != null && webDriverContainer.isDriverStarted()) {
                webDriverContainer.getDriver().close();
                LOGGER.info("webDriverContainer is closed.");
            }
        }
    }
}
