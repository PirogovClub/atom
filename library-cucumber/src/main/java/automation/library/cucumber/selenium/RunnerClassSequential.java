package automation.library.cucumber.selenium;

import automation.library.cucumber.testdataprovider.EmptyDataProvider;
import automation.library.cucumber.testdataprovider.TestDataList;
import automation.library.selenium.exec.BaseTest;
import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apiguardian.api.API;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
 * Extending this class will run the test sequentially.
 */
@API(status = API.Status.STABLE)
public class RunnerClassSequential extends BaseTest {

    private TestNGCucumberRunner testNGCucumberRunner;
    private TestDataList testDataList;

    public RunnerClassSequential(TestDataList testDataList) {
        this.testDataList = testDataList;
    }
    public RunnerClassSequential() {
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
     * Returns two dimensional array of PickleEventWrapper scenarios with their associated CucumberFeatureWrapper feature.
     * and required browser tech stack for the test execution.
     * The two object is combined here and used further. Each scenario has its browser details.
     *
     * @return a two dimensional array of scenarios features.
     */
    @DataProvider
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
