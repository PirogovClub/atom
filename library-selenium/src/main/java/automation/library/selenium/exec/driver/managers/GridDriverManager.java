package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.Constants;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static automation.library.selenium.exec.driver.managers.ChromeDriverManager.getChromeOptions;

public class GridDriverManager extends DriverManager {

    protected Logger log = LogManager.getLogger(this.getClass().getName());

    /**
     * create driver for the selenium grid - get the grid URL (cukes.seleniumGrid) from
     * system property or environment variable
     * or src/test/resources/config/selenium/driverManager.properties
     */
    @Override
    public void createDriver() {
        Capabilities cap = new Capabilities();
        try {

            String url = Property.getVariable("cukes.seleniumGrid");
            if (url == null) url = Property.getProperty(Constants.SELENIUMDRIVERMANAGER, "cukes.seleniumGrid");

            if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Android")) {
                driver = new AndroidDriver(new URL(url), cap.getCap());
            } else if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("iOS")) {
                driver = new IOSDriver(new URL(url), cap.getCap());
			/*}else if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Windows")) {
				driver = new WindowsDriver<WindowsElement>(new URL(url), cap.getCap());*/
            } else {
                if (cap.getCap().getCapability("browserName").toString().equals("chrome")) {
                    ChromeOptions options = getChromeOptions(Property.getProperties(Constants.SELENIUMRUNTIMEPATH));
                    cap.getCap().setCapability(ChromeOptions.CAPABILITY, options);
				}
                driver = new RemoteWebDriver(new URL(url), cap.getCap());
            }
        } catch (Exception e) {
            log.debug("Could not connect to Selenium Grid: " + e.getMessage());
        }
    }

    @Override
    public void updateResults(String result) {
        //do nothing
    }
} 