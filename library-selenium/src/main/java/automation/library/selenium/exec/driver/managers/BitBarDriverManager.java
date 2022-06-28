package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

public class BitBarDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	@Override
	public void createDriver(){

		Capabilities cap = new Capabilities();

		try {
			if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Android")) {
				cap.getCap().setCapability("automationName","uiautomator2");
				driver = new AndroidDriver(new URL(Property.getVariable("cukes.appiumEndPoint")), cap.getCap());
			}

			if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("iOS")) {
				cap.getCap().setCapability("automationName","XCUITest");
				driver = new IOSDriver(new URL(Property.getVariable("cukes.appiumEndPoint")), cap.getCap());
			}

			if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Windows")) {
				cap.getCap().setCapability("automationName","Windows");
				driver = new WindowsDriver<WindowsElement>(new URL(Property.getVariable("cukes.appiumEndPoint")), cap.getCap());
			}
		} catch (Exception e) {
			log.debug("Could not connect to Appium Server: " + e.getMessage());
		}
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 