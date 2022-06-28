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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverManager extends DriverManager {
	protected Logger log = LogManager.getLogger(this.getClass().getName());
	
	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
    	PropertiesConfiguration props = Property.getProperties(Constants.SELENIUMRUNTIMEPATH);
		String useDriverDownloadOnTheFly = Property.getProperty(Constants.SELENIUMRUNTIMEPATH,"cukes.webdrivermanager");
		if (useDriverDownloadOnTheFly != null && useDriverDownloadOnTheFly.equalsIgnoreCase("true")) {
    		if (Property.getVariable("cukes.firefoxDriver")!=null) {
				WebDriverManager.firefoxdriver().driverVersion(Property.getVariable("cukes.firefoxDriver")).setup();
			}else {
				WebDriverManager.firefoxdriver().setup();
			}
    	}else {
    		System.setProperty("webdriver.gecko.driver",getDriverPath("geckodriver")); 			
    	}
    	
		FirefoxOptions options = new FirefoxOptions();
		for (String variable : props.getStringArray("options."+DriverContext.getInstance().getBrowserName().replaceAll("\\s",""))){
    		options.addArguments(variable);
		}

		// If enableHar2Jmx is true then an extra capability will be added to allow for 'untrusted' certificates.
		// This is needed for when using a proxy to capture network traffic when recording HAR data.
		if(Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
			options.setAcceptInsecureCerts(true);
		}

		log.debug("firefox.options="+ Property.getVariable("firefox.options"));
		if (Property.getVariable("firefox.options")!=null){
			options.addArguments(Property.getVariable("firefox.options"));
		}
//		options.addCapabilities(cap.getCap()); 	//TEMP
		driver = new FirefoxDriver(options);
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 