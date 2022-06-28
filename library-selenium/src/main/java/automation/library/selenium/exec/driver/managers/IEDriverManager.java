package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.Constants;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;

public class IEDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		String useDriverDownloadOnTheFly = Property.getProperty(Constants.SELENIUMRUNTIMEPATH,"cukes.webdrivermanager");
		if (useDriverDownloadOnTheFly != null && useDriverDownloadOnTheFly.equalsIgnoreCase("true")) {
			if (Property.getVariable("cukes.ieDriver")!=null) {
				WebDriverManager.iedriver().driverVersion(Property.getVariable("cukes.ieDriver")).setup();
			}else {
				WebDriverManager.iedriver().setup();
			}
    	}else {
    		System.setProperty("webdriver.ie.driver", getDriverPath("IEDriverServer"));
    	}
    	
		System.setProperty("webdriver.ie.driver.silent", "true");
		System.setProperty("ie.ensureCleanSession", "true");

		// If enableHar2Jmx is true then an extra capability will be added to allow for 'untrusted' certificates.
		// This is needed for when using a proxy to capture network traffic when recording HAR data.
		if(Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
			cap.getCap().setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		}

		driver = new InternetExplorerDriver(cap.getCap());
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 