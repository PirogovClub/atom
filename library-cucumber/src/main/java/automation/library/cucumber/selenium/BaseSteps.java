package automation.library.cucumber.selenium;

import automation.library.common.extendentassert.SoftAssertWithNotifications;
import automation.library.cucumber.listener.CucumberEventTypes;
import automation.library.selenium.exec.BasePO;
import io.cucumber.java8.En;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import automation.library.selenium.core.Constants;
import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.selenium.exec.driver.factory.DriverFactory;

/**
 * Class which should be extended to the custom step definition class, allow to access the driver and wait objects
 */
public class BaseSteps implements En {

	protected Logger log = LogManager.getLogger(this.getClass().getName());
	protected BasePO po;


	
	public BaseSteps(){
	}

	public BasePO getPO() {
		log.debug("obtaining an instance of the base page objects");
		if (po == null)
			po = new BasePO();

		return po;
	}

	public WebDriver getDriver() {
		log.debug("obtaining the driver for current thread");
		return DriverFactory.getInstance().getDriver();
	}

	public WebDriverWait getWait() {
		log.debug("obtaining the wait for current thread");
		return DriverFactory.getInstance().getWait();
	}

	/** Returns duration for specified waits */
	@SuppressWarnings("unused")
	private int getWaitDuration(){
		final int defaultWait = 10;
		int duration;
		try {
			duration = Integer.parseInt(Property.getProperty(Constants.SELENIUMRUNTIMEPATH,"defaultWait"));
			log.debug("selenium driver wait time set from environment properties");
		} catch (Exception e) {
			duration = defaultWait;
			log.debug("selenium driver wait time not available from environment properties...default applied="+defaultWait);
		}
		return duration; 
	}

	public SoftAssertWithNotifications sa() {
		return TestContext.getInstance().sa();
	}
}
