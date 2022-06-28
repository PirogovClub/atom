package automation.library.cucumber.api;

import io.cucumber.testng.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(
        strict = true,
        glue = {"automation.library"}
)

/**
 * Test Runner Class for API Testing, extends the Cucumber TestNG runner will sequential execution
 */
public class RunnerClassAPISequential extends AbstractTestNGCucumberTests {}

