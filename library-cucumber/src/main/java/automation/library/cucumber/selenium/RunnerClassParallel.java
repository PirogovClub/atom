package automation.library.cucumber.selenium;

import automation.library.common.TestContext;
import automation.library.cucumber.listener.CucumberEventTypes;
import automation.library.cucumber.testdataprovider.EmptyDataProvider;
import automation.library.cucumber.testdataprovider.TestDataList;
import automation.library.selenium.exec.BaseTest;
import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apiguardian.api.API;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@CucumberOptions(
        strict = true,
        glue = {"automation.library"}
)

/**
 * The runner class to includes the tech stack with the scenario in test.
 * instead of extending io.cucumber.testng.AbstractTestNGCucumberTests, annotations is used to include the browser
 * details for the scenario found in the features as separated test
 *
 * Extending this class will run the test parallel
 */
@API(status = API.Status.STABLE)
public class RunnerClassParallel extends BaseTest {
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    private TestNGCucumberRunner testNGCucumberRunner;
    private TestDataList testDataList;

    public RunnerClassParallel(TestDataList testDataList) {
        this.testDataList = testDataList;
    }

    public RunnerClassParallel() {
        this(new EmptyDataProvider());
    }


    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }



    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void runScenario(
            PickleEventWrapper pickleWrapper,
            CucumberFeatureWrapper featureWrapper,
            Map<String, String> map,
            Object data) throws Throwable {
        // the 'featureWrapper' parameter solely exists to display the feature file in a test report
        // the 'map' parameter is for setting the driver context
        testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());
    }

    /**
     * Returns two dimensional array of PickleEventWrapper scenarios
     * with their associated CucumberFeatureWrapper feature.
     * and required browser tech stack for the test execution
     *
     * @return a two dimensional array of scenarios features.
     */
    @DataProvider(parallel = true)
    public Object[][] scenarios() {

        return new DataProviderComposer(testNGCucumberRunner, testDataList).scenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner == null) {
            return;
        }
        testNGCucumberRunner.finish();
    }


}
