package automation.library.cucumber.api;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.apiguardian.api.API;
import org.testng.annotations.DataProvider;

/**
 * Runs each cucumber scenario found in the features as separated test
 */
@API(status = API.Status.STABLE)
public abstract class RunnerClassAPIParallel extends AbstractTestNGCucumberTests {

    /**
     * Returns two dimensional array of PickleEventWrapper scenarios
     * with their associated CucumberFeatureWrapper feature.
     * Executes the test in parallel.
     * @return a two dimensional array of scenarios features.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
