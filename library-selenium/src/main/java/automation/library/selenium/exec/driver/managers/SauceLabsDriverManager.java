package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.selenium.exec.Constants;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverContext;
import automation.library.selenium.exec.driver.factory.DriverManager;
import automation.library.selenium.exec.driver.factory.SauceCapabilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class SauceLabsDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();

		//get the connection details from SauceLabs
		// system property or environment variable or src/test/resources/config/selenium/driverManager.properties

		String sauceUserName = Property.getVariable("cukes.sauceUserName");
		if(sauceUserName == null) sauceUserName = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceUserName");

		String sauceEndPointMobile = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceEndPointMobile");
		if(sauceEndPointMobile == null) sauceEndPointMobile = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceEndPointMobile");

		String sauceAccessKeyMobile = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceAccessKeyMobile");
		if(sauceAccessKeyMobile == null) sauceAccessKeyMobile = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceEndPointMobile");

		String sauceEndPointWeb = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceEndPointWeb");
		if(sauceEndPointWeb == null) sauceEndPointWeb = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceEndPointWeb");

		String sauceAccessKeyWeb = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceAccessKeyWeb");
		if(sauceAccessKeyWeb == null) sauceAccessKeyWeb = Property.getProperty(Constants.SELENIUMDRIVERMANAGER,"cukes.sauceAccessKeyWeb");

		try {
			if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Android")) {
				cap.getCap().setCapability("automationName","uiautomator2");
				cap.getCap().setCapability("app","sauce-storage:"+TestContext.getInstance().getFwSpecificData("fw.appName"));

				String sauceServerAddress = "http://"+sauceUserName + ":" + sauceAccessKeyMobile + sauceEndPointMobile;

				driver = new AndroidDriver(new URL(sauceServerAddress), cap.getCap());
			}else if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("iOS")) {
				cap.getCap().setCapability("automationName","XCUITest");
				String sauceServerAddress = "http://"+sauceUserName + ":" + sauceAccessKeyMobile + sauceEndPointMobile;

				driver = new IOSDriver(new URL(sauceServerAddress), cap.getCap());
			}else {
				SauceCapabilities sCaps = new SauceCapabilities();

				String sauceServerAddress = "http://"+sauceUserName + ":" + sauceAccessKeyWeb + sauceEndPointWeb;
				String browser = DriverContext.getInstance().getBrowserName();

				if(browser.equalsIgnoreCase("safari")){
					driver = new RemoteWebDriver(new URL(sauceServerAddress), sCaps.getBrowserOptions());
					((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
				}
				else if(browser.equalsIgnoreCase("firefox")){
					driver = new RemoteWebDriver(new URL(sauceServerAddress), sCaps.getFirefoxOptions());
					((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
				}
				else{
					driver = new RemoteWebDriver(new URL(sauceServerAddress), cap.getCap());
					((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
				}
			}
		} catch (Exception e) {
			log.debug("Could not connect to SauceLabs: " + e.getMessage());
		}
	}

	@Override
	public void updateResults(String result){
		((JavascriptExecutor) driver).executeScript("sauce:job-result=" + result);
	}
}