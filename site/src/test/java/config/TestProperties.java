package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

/**
 * Loads properties for the performance of tests.
 * When run locally, the dev-resources/local.properties file is used. This file can be filled in by the developer for local tests.
 * When run on the test environment, the resources/environment.properties file is loaded. This file should not be changed
 * unless necessary.
 */
public class TestProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestProperties.class);
    private static final String ENVIRONMENT_PROPERTIES = "environment.properties";
    private static final String RELATIVE_PATH_TO_LOCAL_PROPERTIES_FILE = "src/test/dev-resources/local.properties";
    private static final int NUMBER_OF_SUBFOLDERS = 2;
    public static final String EMPTY_PATH = "";
    private static TestProperties instance = new TestProperties();

    private static Properties properties;

    /**
     * Private constructor.
     * Loads the properties to be used by cucumber tests.
     * When run locally, the local.properties file is used.
     * When run on an environment, the environment.properties file is used.
     */
    private TestProperties() {
        Properties properties;
        String pathToPropertiesFile = getPathToLocalPropertiesFile();
        File propertiesFile = new File(pathToPropertiesFile);

        if (propertiesFile.exists()) {
            properties = loadLocalProperties(propertiesFile);

        } else {
            properties = loadEnvironmentProperties();
        }

        if (properties == null) {
            throw new NullPointerException("Failed to retrieve properties.");
        }

        TestProperties.properties = properties;

        addPropertiesToSystemProperties();
    }

    /**
     * Get the loaded properties
     * @return the test properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Gets an instance of this class in order to access the loaded test properties
     * @return an instance of this class
     */
    public static TestProperties getInstance() {
        return instance;
    }

    private static Properties loadLocalProperties(File propertiesFile) {
        if (propertiesFile == null) {
            throw new IllegalArgumentException("Properties file cannot be null");
        }

        return readProperties(propertiesFile.getPath());
    }

    private static Properties loadEnvironmentProperties() {
        URL resourceUrl = TestProperties.class.getClassLoader().getResource(ENVIRONMENT_PROPERTIES);

        if (resourceUrl == null) {
            throw new RuntimeException("No environment.properties file found.");
        }

        return readProperties(resourceUrl.getPath());
    }

    private static Properties readProperties(String path) {
        Properties properties = new Properties();
        try (FileInputStream propertyInputStream = new FileInputStream(path);
             InputStreamReader reader = new InputStreamReader(propertyInputStream)) {

            properties.load(reader);

        } catch (FileNotFoundException e) {
            LOGGER.error("Properties file does not exist: {}", path, e);
        } catch (IOException e) {
            LOGGER.error("Failed to load properties file: {}", path, e);
        }
        return properties;
    }

    private static String getPathToLocalPropertiesFile() {
        URL resourcePath = TestProperties.class.getClassLoader().getResource(EMPTY_PATH);

        if (resourcePath == null) {
            throw new RuntimeException("Resource path could not be found.");
        }

        String[] parts = resourcePath.getPath().split(File.separator);

        StringBuilder propertiesFileSB = new StringBuilder();
        for (int i = 0; i < parts.length - (NUMBER_OF_SUBFOLDERS); i++) {
            propertiesFileSB.append(parts[i]).append(File.separator);
        }

        return propertiesFileSB.toString() + RELATIVE_PATH_TO_LOCAL_PROPERTIES_FILE;
    }

    private void addPropertiesToSystemProperties() {
        StringBuilder sb = new StringBuilder();
        Set<Object> allKeys = properties.keySet();
        for (Object k : allKeys) {
            String key = (String) k;
            String value = properties.getProperty(key);
            sb.append(String.format("[%s]=[%s]", key, value)).append("\n");
            System.setProperty(key, value);
        }
        System.out.println(sb.toString());
    }

}
