package hooks;

import automation.library.common.TestContext;
import automation.library.common.listeners.AtomEventManager;
import automation.library.common.listeners.CommonAtomEvents;
import automation.library.cucumber.listener.CucumberAtomEvents;
import automation.library.reporting.Reporter;
import automation.library.selenium.listener.SeleniumAtomEvents;
import io.cucumber.java.Before;

public class Hooks {

    @Before
    public void startUp() {
        TestContext.getInstance()
                .resetEventManager()
                .addEvents(CommonAtomEvents.values())
                .addEvents(CucumberAtomEvents.values())
                .addEvents(SeleniumAtomEvents.values());

        getEventManager().subscribe(Reporter.getSupportedEvents(),Reporter.getInstance());
    }

    private AtomEventManager getEventManager() {
        return TestContext.getInstance().getAtomEventManager();
    }

//    @After(order=40)
//    public void closeDown(Scenario scenario) throws InterruptedException {
//    }

}
