package automation.library.selenium.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JsLoadWait {
    static Logger log = LogManager.getLogger(JsLoadWait.class.getName());
    /**
     * Wait for page to load based on document.readyState=complete
     */
    public static void domLoaded(WebDriver driver, WebDriverWait wait) {

        //log.debug("checking that the DOM is loaded");
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean domReady = js.executeScript("return document.readyState").equals("complete");

        if (!domReady) {
            wait.until((ExpectedCondition<Boolean>) d -> (js.executeScript("return document.readyState").equals("complete")));
        }
    }

    /**
     * Wait for JQuery to load
     */
    public static void jqueryLoaded(WebDriver driver,WebDriverWait wait) {
        //log.debug("checking that any JQuery operations complete");
        final JavascriptExecutor js = (JavascriptExecutor) driver;

        if ((Boolean) js.executeScript("return typeof jQuery != 'undefined'")) {
            boolean jqueryReady = (Boolean) js.executeScript("return jQuery.active==0");

            if (!jqueryReady) {
                wait.until((ExpectedCondition<Boolean>) d -> (Boolean) js.executeScript("return window.jQuery.active === 0"));
            }
        }
    }

    /**
     * Wait for AngularJs to load
     */
    public static void angularLoaded(WebDriver driver,WebDriverWait wait) {
        //log.debug("checking that any AngularJS operations complete");
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        if ((Boolean) js.executeScript("return (window.angular!=null);")) {
            Boolean angularInjectorUndefined = (Boolean) js.executeScript("return angular.element(document).find('body').injector() === undefined");

            if (!angularInjectorUndefined) {
                Boolean angularReady = (Boolean) js.executeScript("return angular.element(document).find('body').injector().get('$http').pendingRequests.length === 0");

                if (!angularReady) {
                    wait.until((ExpectedCondition<Boolean>) d -> js.executeScript("return angular.element(document).find('body').injector().get('$http').pendingRequests.length === 0").equals(true));
                }
            } else {
                log.debug("no AngularJS injector defined so cannot wait");
            }
        }
    }
}
