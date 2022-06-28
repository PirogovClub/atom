package automation.library.selenium.exec.driver.factory;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.selenium.exec.Constants;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Class to set the desired capabilities
 */
public class Capabilities {

    protected Logger log = LogManager.getLogger(this.getClass().getName());

    private DesiredCapabilities dc;

    /**
     * constructor to set the desired capabilities
     */
    public Capabilities() {
        Map<String, String> map = DriverContext.getInstance().getTechStack();

        dc = new DesiredCapabilities();

        if (!map.get("seleniumServer").equalsIgnoreCase("grid")) {
            dc.setCapability("name", TestContext.getInstance().getFwSpecificData("fw.testDescription"));
        }

        if (TestContext.getInstance().fwSpecificData().containsKey("fw.projectName"))
            dc.setCapability("project", TestContext.getInstance().getFwSpecificData("fw.projectName"));

        if (TestContext.getInstance().fwSpecificData().containsKey("fw.buildNumber"))
            dc.setCapability("build", TestContext.getInstance().getFwSpecificData("fw.buildNumber"));

        //IOS does not require appPackage and appActivity capabilities.
        if(!((map.containsKey("platformName") && map.get("platformName").toLowerCase().contains("ios")) ||
                (map.containsKey("platform") && map.get("platform").toLowerCase().contains("ios")))){
            if (TestContext.getInstance().getFwSpecificData("fw.appPackage") != null)
                dc.setCapability("appPackage", TestContext.getInstance().getFwSpecificData("fw.appPackage"));

            if (TestContext.getInstance().getFwSpecificData("fw.appActivity") != null)
                dc.setCapability("appActivity", TestContext.getInstance().getFwSpecificData("fw.appActivity"));
        }

        for (Map.Entry<String, String> pair : map.entrySet()) {
            if (!pair.getKey().equalsIgnoreCase("seleniumServer") && !pair.getKey().equalsIgnoreCase("description"))
                dc.setCapability(pair.getKey(), pair.getValue());
        }

        setProxyCap();

        //TEMP
        PropertiesConfiguration props = Property.getProperties(Constants.SELENIUMRUNTIMEPATH);
        //setting any project specific capabilities
        for (String variable : props.getStringArray("desiredCapabilities." + DriverContext.getInstance().getBrowserName().replaceAll("\\s", ""))) {
            String[] par = variable.split("==");
            if (par[1].trim().equalsIgnoreCase("true") || par[1].trim().equalsIgnoreCase("false"))
                dc.setCapability(par[0].trim(), Boolean.parseBoolean(par[1].trim()));
            else
                dc.setCapability(par[0].trim(), par[1].trim());
        }
    }

    public DesiredCapabilities getCap() {
        return dc;
    }

    private void setProxyCap(){
        if(Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
            Proxy seleniumProxy = null;

            try {
                Class<?> har2jmxCapabilitiesClass = Class.forName("automation.library.conversion2jmx.selenium" + "." + "Har2JmxCapabilities");

                Method method = har2jmxCapabilitiesClass.getMethod("setProxyCap");
                Object newInstance = har2jmxCapabilitiesClass.getDeclaredConstructor().newInstance();
                seleniumProxy = (Proxy)method.invoke(newInstance);
                dc.setCapability(CapabilityType.PROXY, seleniumProxy);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Unable to find automation.library.conversion2jmx.selenium.Har2JmxCapabilities");
                log.error("The library-conversion2jmx is needed in order to allow HAR recording");
                throw new RuntimeException(e);
            }
        }
    }

}
