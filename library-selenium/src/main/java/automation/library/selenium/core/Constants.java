package automation.library.selenium.core;

/**
 * POJO used to define constants of selenium
 */
public class Constants {

    public static final String BASEPATH = System.getProperty("user.dir") + "/src/test/resources/";
    public static final String SELENIUMRUNTIMEPATH = BASEPATH + "config/selenium/" + "runtime.properties";
    public static final String SELENIUMDRIVERMANAGER = BASEPATH + "config/selenium/" + "driverManager.properties";
    public static final String DEV_MOD_PATH = BASEPATH + "config/selenium/" + "devMode.properties";


    public static final String SCREEN_SHOT_PARAM_ABSOLUTE_PATH = "absolutePath";
    public static final String SCREEN_SHOT_PARAM_RELATIVE_PATH = "relativePath";
    public static final int DEFAULT_WAIT = 10;
    public static final String DEFAULT_WAIT_KEY = "defaultWait";
    public static final String DEFAULT_RETRY_KEY = "defaultRetry";
}
