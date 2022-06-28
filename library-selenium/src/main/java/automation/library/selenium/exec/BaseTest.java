package automation.library.selenium.exec;

import automation.library.common.ExcelHelper;
import automation.library.common.JsonHelper;
import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.common.extendentassert.SoftAssertWithNotifications;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import automation.library.selenium.exec.driver.factory.DriverContext;
import automation.library.selenium.exec.driver.factory.DriverFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static automation.library.selenium.exec.Constants.*;

/**
 * Class to hold start up (set test context) and tear down (shut down browser) for selenium test. This class should be
 * extended by each test class in the test project
 */
public class BaseTest {


    protected Logger log = LogManager.getLogger(this.getClass().getName());
    protected BasePO po;
    protected TestContext context = TestContext.getInstance();


    protected  Map<String, Object> getTestData(){
        return (Map<String, Object>) TestContext.getInstance().testdata().get(TEST_DATA_ARG_NAME);
    }

    /**
     * return web driver for the current thread - can be used when running using TestNG
     */
    public WebDriver getDriver() {
        //log.debug("obtaining the driver for current thread");
        return DriverFactory.getInstance().getDriver();
    }

    /**
     * return web driver wait for the current thread - can be used when running using TestNG
     */
    public WebDriverWait getWait() {
        //log.debug("obtaining the wait for current thread");
        return DriverFactory.getInstance().getWait();
    }

    /**
     * return BasePO instance - can be used when running using TestNG
     */
    public BasePO getPO() {
        log.debug("obtaining an instance of the base page object");
        if (this.po == null) {
            this.po = new BasePO();
        }
        return po;
    }

    public void setPO() {
        log.debug("obtaining an instance of the base page object");
        this.po = new BasePO();
    }

    /**
     * SoftAssert instance from TestContext to be used - can be used when running using TestNG
     */
    protected SoftAssertWithNotifications sa() {
        return TestContext.getInstance().sa();
    }

    /**
     * Read the 'tech stack' for a given test run and enable parallel execution from json file
     */
    @DataProvider(name = "techStackJSON", parallel = true)
    public Object[][] techStackJSON() throws Exception {
        log.debug("spinning up parallel execution threads for multi browser testing");
        JSONArray jsonArr = JsonHelper.getJSONArray(Constants.SELENIUMSTACKSPATH);
        Object[][] obj = new Object[jsonArr.length()][1];
        Gson gson = new GsonBuilder().create();

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            @SuppressWarnings("unchecked")
            Map<String, String> map = gson.fromJson(jsonObj.toString(), Map.class);
            obj[i][0] = map;
        }

        return obj;
    }

    /**
     * Read the 'tech stack' for a given test run and enable parallel execution from excel file
     */
    @DataProvider(name = "techStackExcel", parallel = true)
    public Object[][] techStackExcel() throws Exception {
        log.debug("spinning up parallel execution threads for multi browser testing");

        ArrayList<ArrayList<Object>> browsers = ExcelHelper.getDataAsArrayList(Constants.TESTCASEPATH, "browsers", new String[0]);
        Object[][] obj = new Object[browsers.size() - 1][1];

        for (int i = 1; i < browsers.size(); ++i) {
            Map<String, String> map = new HashMap();
            for (int j = 0; j < browsers.get(0).size(); j++) {
                map.put(browsers.get(0).get(j).toString(), browsers.get(i).get(j).toString());
            }
            obj[i - 1][0] = map;
        }

        return obj;
    }

    /**
     * set the test context information
     */
    @BeforeMethod
    public void startUp(Method method, Object[] args) {
        Test test = method.getAnnotation(Test.class);

        Map<String, String> map = (Map<String, String>) args[STACK_MAP_ID_IN_ARGS];

        if (!TestContext.getInstance().fwSpecificData().containsKey("fw.cucumberTest"))
            TestContext.getInstance().putFwSpecificData("fw.testDescription", test.description() + " (" + map.get("description") + ")");
        if (Property.getVariable("PROJECT_NAME") != null && !Property.getVariable("PROJECT_NAME").isEmpty())
            TestContext.getInstance().putFwSpecificData("fw.projectName", Property.getVariable("PROJECT_NAME"));
        if (Property.getVariable("BUILD_NUMBER") != null && !Property.getVariable("BUILD_NUMBER").isEmpty())
            TestContext.getInstance().putFwSpecificData("fw.buildNumber", Property.getVariable("BUILD_NUMBER"));
        DriverContext.getInstance().setDriverContext(map);

        //Populating testdata with data from args
        if (args.length > TESTDATA_MAP_ID_IN_ARGS) {
            if (args[TESTDATA_MAP_ID_IN_ARGS] != null) {
                TestContext.getInstance().testdata().put(Constants.TEST_DATA_ARG_NAME, args[TESTDATA_MAP_ID_IN_ARGS]);
            }
        }
    }

    /**
     * if cucumber test the update the status and removes the current thread's value for this thread-local
     */
    @AfterMethod(groups = {"quitDriver"})
    public void closeDown(ITestResult result) {
        if (!TestContext.getInstance().fwSpecificData().containsKey("fw.cucumberTest")) {
            DriverFactory.getInstance().driverManager().updateResults(result.isSuccess() ? "passed" : "failed");
            DriverFactory.getInstance().quit();
        }

        finaliseSelenium2JmxRecording();
    }

    private void finaliseSelenium2JmxRecording() {
        if (Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
            try {
                Class<?> har2jmxHelperClass = Class.forName("automation.library.conversion2jmx.selenium" + "." + "Har2JmxBaseTest");

                Method method = har2jmxHelperClass.getMethod("finaliseRecording");
                Object newInstance = har2jmxHelperClass.getDeclaredConstructor().newInstance();
                method.invoke(newInstance);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Unable to find automation.library.conversion2jmx.selenium.Har2JmxBaseTest");
                log.error("The library-conversion2jmx is needed in order to allow HAR recording");
                throw new RuntimeException(e);
            }
        }
    }
}
