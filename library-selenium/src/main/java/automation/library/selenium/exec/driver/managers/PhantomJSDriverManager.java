package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.Constants;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class PhantomJSDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		String useDriverDownloadOnTheFly = Property.getProperty(Constants.SELENIUMRUNTIMEPATH,"cukes.webdrivermanager");
		if (useDriverDownloadOnTheFly != null && useDriverDownloadOnTheFly.equalsIgnoreCase("true")) {
    		WebDriverManager.phantomjs().setup();
    		if (Property.getVariable("cukes.phantomjsDriver")!=null) {
				WebDriverManager.phantomjs().driverVersion(Property.getVariable("cukes.phantomjsDriver")).setup();
			}else {
				WebDriverManager.phantomjs().setup();
			}
    	}else {
    		System.setProperty("phantomjs.binary.path", getDriverPath("phantomjs"));		
    	}
		System.setProperty("phantomjs.binary.path", getDriverPath("phantomjs"));		
		driver = new PhantomJSDriver(cap.getCap());
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 