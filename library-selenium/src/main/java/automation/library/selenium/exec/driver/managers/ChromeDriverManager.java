package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverContext;
import automation.library.selenium.exec.driver.factory.DriverManager;
import automation.library.selenium.exec.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverManager extends DriverManager {

    public static final String WINDOW_SIZE_WIDTH = "window-size-width";
    public static final String WINDOW_SIZE_HEIGHT = "window-size-height";
    protected Logger log = LogManager.getLogger(this.getClass().getName());


    @Override
    public void createDriver() {
        Capabilities cap = new Capabilities();
        String useDriverDownloadOnTheFly = Property.getProperty(Constants.SELENIUMRUNTIMEPATH, "cukes.webdrivermanager");
        if (useDriverDownloadOnTheFly != null && useDriverDownloadOnTheFly.equalsIgnoreCase("true")) {
            if (Property.getVariable("cukes.chromeDriver") != null) {
                WebDriverManager.chromedriver().driverVersion(Property.getVariable("cukes.chromeDriver")).setup();
            } else {
                WebDriverManager.chromedriver().setup();
            }
        } else {
            System.setProperty("webdriver.chrome.driver", getDriverPath("chromedriver"));
        }
        System.setProperty("webdriver.chrome.silentOutput", "true");
        ChromeOptions options = getChromeOptions(Property.getProperties(Constants.SELENIUMRUNTIMEPATH));

        log.debug("chrome.options=" + Property.getVariable("chrome.options"));
        if (Property.getVariable("chrome.options") != null) {
            for (String option : Property.getVariable("chrome.options").split(";")) {
                options.addArguments(option);
            }
        }

        if (DriverContext.getInstance().getBrowserName().contains("kiosk")) {
            options.addArguments("--kiosk");
        }

        cap.getCap().setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(cap.getCap());
    }

    public static ChromeOptions getChromeOptions(PropertiesConfiguration props) {
        ChromeOptions options = new ChromeOptions();

        // If enableHar2Jmx is true then an extra capability will be added to allow for 'untrusted' certificates.
        // This is needed for when using a proxy to capture network traffic when recording HAR data.
        if (Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
            options.addArguments("--ignore-ssl-errors=yes");
            options.addArguments("--ignore-certificate-errors");
        }

        String windowH = "";
        String windowL = "";
        for (String variable : props.getStringArray("options." + DriverContext.getInstance().getBrowserName().replaceAll("\\s", ""))) {
            if (variable.contains(WINDOW_SIZE_HEIGHT)) {
                windowH = variable.substring(WINDOW_SIZE_HEIGHT.length() + 1);
            } else if (variable.contains(WINDOW_SIZE_WIDTH)) {
                windowL = variable.substring(WINDOW_SIZE_WIDTH.length() + 1);
            } else {
                options.addArguments(variable);
            }
        }
        if (!windowH.isEmpty() && !windowL.isEmpty()) {
            options.addArguments("--window-size=" + windowL + "," + windowH);
        }
        return options;
    }

    @Override
    public void updateResults(String result) {
        //do nothing
    }
} 