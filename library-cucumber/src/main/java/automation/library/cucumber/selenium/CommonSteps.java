package automation.library.cucumber.selenium;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.cucumber.core.Constants;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static automation.library.common.Property.getDomainName;
import static automation.library.selenium.core.Constants.SELENIUMRUNTIMEPATH;

/**
 * Common Step definition class. This has some basic steps defs to launch the open browser, launch rul navigate forward
 * and back and launch mobile application
 */
@SuppressWarnings("deprecation")
public class CommonSteps extends BaseSteps {
    protected Logger log = LogManager.getLogger(this.getClass().getName());


    @When("^the browser is opened$")
    public void theBrowserIsOpened() {
        getDriver().manage().window().maximize();
    }


    @When("^the application \"(.*)\"$")
    public void theApplicaiton(String app) {
        getDriver().manage().window().maximize();
        log.debug("browser launched");
        String url = Property.getProperty(
                Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties",
                app);
        log.debug("Navigating to url: " + url);
        getDriver().get(url);

        String setCookies = Property.getProperty(SELENIUMRUNTIMEPATH, "add_cookies");
        if ((setCookies!=null)&&(setCookies.equals("true"))) {
            SimpleDateFormat dateformat3 = new SimpleDateFormat("MM/dd/yyyy");
            Date date1 = null;
            try {
                date1 = dateformat3.parse("12/30/2039");
            } catch (ParseException e) {
                e.printStackTrace();
            }


            for (Map.Entry mapElement : Property.getCookies(SELENIUMRUNTIMEPATH).entrySet()) {
                log.debug("adding " + mapElement.getKey() + " cookie with " + mapElement.getValue() + " value");
                getDriver().manage().addCookie(
                        new Cookie(
                                (String) mapElement.getKey(),
                                (String) mapElement.getValue(),
                                getDomainName(url),
                                "/",
                                date1
                        ));
            }
            //hit url with cookies now
            getDriver().get(url);
        }

    }

    @When("^the mobile application \"(.*)\"$")
    public void theMobileApplicaiton(String app) {
        String[] target = (Property.getProperty(
                Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties",
                app)).split("::");
        TestContext.getInstance().putFwSpecificData("fw.appName", target[0]);
        TestContext.getInstance().putFwSpecificData("fw.appPackage", target[1]);
        TestContext.getInstance().putFwSpecificData("fw.appActivity", target[2]);
        getDriver();
    }

    @When("^the url \"(.*)\"$")
    public void theUrl(String url) {
        getDriver().manage().window().maximize();
        log.debug("browser launched");
        log.debug("Navigating to url: " + url);
        getDriver().get(url);
    }

    @When("^browser is navigated back$")
    public void browserIsNavigatedBack() {
        getDriver().navigate().back();
    }

    @When("^browser is navigated forward$")
    public void browserIsNavigatedForward() {
        getDriver().navigate().forward();
    }
}