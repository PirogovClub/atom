package automation.library.cucumber.selenium;

import automation.library.selenium.exec.driver.factory.DriverContext;
import automation.library.selenium.exec.driver.factory.DriverFactory;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;

/**
 * Hooks class for the screenshot in case of failure
 */

public class Hooks {

    @After(order = 30)
    public void checkScenarioStatus(Scenario scenario) {
        if (DriverContext.getInstance().getTechStack() != null) {
            DriverFactory.getInstance().driverManager().updateResults(scenario.isFailed() ? "failed" : "passed");
            if (!DriverContext.getInstance().getKeepBrowserOpen()) {
                DriverFactory.getInstance().quit();
            }
        }
    }
}
