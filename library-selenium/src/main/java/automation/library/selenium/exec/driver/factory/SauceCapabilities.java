package automation.library.selenium.exec.driver.factory;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.selenium.exec.Constants;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;

import java.util.Map;

/**
 * Class to set the desired capabilities
 */
public class SauceCapabilities {

    private MutableCapabilities dc;
    private SafariOptions browserOptions;
    private FirefoxOptions firefoxOptions;

    /**
     * constructor to set the desired capabilities
     */
    public SauceCapabilities() {
        Map<String, String> map = DriverContext.getInstance().getTechStack();

        dc = new MutableCapabilities();
        browserOptions = new SafariOptions();

        if (!map.get("seleniumServer").equalsIgnoreCase("grid")) {
            dc.setCapability("name", TestContext.getInstance().getFwSpecificData("fw.testDescription"));
        }

        if (TestContext.getInstance().fwSpecificData().containsKey("fw.projectName"))
            dc.setCapability("project", TestContext.getInstance().getFwSpecificData("fw.projectName"));

        if (TestContext.getInstance().fwSpecificData().containsKey("fw.buildNumber"))
            dc.setCapability("build", TestContext.getInstance().getFwSpecificData("fw.buildNumber"));

        if (!Property.getVariable("cukes.techstack").contains("SAUCE_MOBILE_iOS")) {
            if (TestContext.getInstance().getFwSpecificData("fw.appPackage") != null)
                dc.setCapability("appPackage", TestContext.getInstance().getFwSpecificData("fw.appPackage"));

            if (TestContext.getInstance().getFwSpecificData("fw.appActivity") != null)
                dc.setCapability("appActivity", TestContext.getInstance().getFwSpecificData("fw.appActivity"));
        }

        String browser = DriverContext.getInstance().getBrowserName();

        if(browser.equalsIgnoreCase("safari")){
            browserOptions = new SafariOptions();
            for (Map.Entry<String, String> pair : map.entrySet()) {
                if (!pair.getKey().equalsIgnoreCase("seleniumServer") && !pair.getKey().equalsIgnoreCase("description"))
                    browserOptions.setCapability(pair.getKey(), pair.getValue());
            }

            browserOptions.setCapability("sauce:options",dc);
        }
        else if(browser.equalsIgnoreCase("firefox")){
            firefoxOptions = new FirefoxOptions();
            for (Map.Entry<String, String> pair : map.entrySet()) {
                if (!pair.getKey().equalsIgnoreCase("seleniumServer") && !pair.getKey().equalsIgnoreCase("description"))
                    firefoxOptions.setCapability(pair.getKey(), pair.getValue());
            }

            firefoxOptions.setCapability("sauce:options",dc);
        }
        else{
            for (Map.Entry<String, String> pair : map.entrySet()) {
                if (!pair.getKey().equalsIgnoreCase("seleniumServer") && !pair.getKey().equalsIgnoreCase("description"))
                    dc.setCapability(pair.getKey(), pair.getValue());
            }
        }
    }

    public MutableCapabilities getCap() {
        return dc;
    }

    public SafariOptions getBrowserOptions(){
        return browserOptions;
    }

    public FirefoxOptions getFirefoxOptions(){
        return firefoxOptions;
    }

}
