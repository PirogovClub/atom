package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.edge.EdgeDriver;

public class EdgeDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());


	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		if (Property.getVariable("cukes.webdrivermanager") != null && Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
    		if (Property.getVariable("cukes.edgeDriver")!=null) {
				WebDriverManager.edgedriver().driverVersion(Property.getVariable("cukes.edgeDriver")).setup();
			}else {
				WebDriverManager.edgedriver().setup();
			}
    	}else {
		System.setProperty("webdriver.edge.driver", getDriverPath("MicrosoftWebDriver"));
    	}
		driver = new EdgeDriver(cap.getCap());
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 