package automation.library.reporting;

import automation.library.common.AtomException;
import automation.library.common.Property;
import automation.library.common.listeners.*;

import automation.library.reporting.adapter.ExtentCucumberAdapter;
import automation.library.reporting.eventhandlers.ElementHandler;
import automation.library.reporting.eventhandlers.IAssertHandler;
import automation.library.reporting.service.ExtentService;
import automation.library.selenium.core.Element;
import automation.library.selenium.core.Screenshot;
import automation.library.selenium.exec.driver.factory.DriverFactory;
import automation.library.selenium.listener.SeleniumAtomEvents;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.IAssert;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static automation.library.selenium.core.Constants.SCREEN_SHOT_PARAM_RELATIVE_PATH;

/**
 * This class houses few utilities required for the report
 */
@Log4j2
public class Reporter implements AtomEventListener {
    public static final String ARROW_POINTING_DOWNWARDS_THEN_CURVING_RIGHTWARDS = "&#10551;";
    public static final String CROSS_MARK = "&#10060;";
    public static final String SLIGHTLY_SMILING_FACE = "&#128578;";
    private static Map<String, Boolean> systemInfoKeyMap = new HashMap<>();
    private static Reporter instance = null;


    public static List<String> getSupportedStringEvents() {
        return supportedStringEvents;
    }

    private static List<String> supportedStringEvents = new ArrayList<>(
            List.of(
                    CommonAtomEventsString.FORMAT_HELPER_SSN_GENERATED,
                    CommonAtomEventsString.ASSERT_ASSERTION_SUCCESS,
                    CommonAtomEventsString.ASSERT_ASSERTION_FAIL,
                    CommonAtomEventsString.ASSERT_ASSERTION_ERROR
            ));

    public static AtomEventSimple[] getSupportedEvents() {
        List<AtomEventSimple> toReturn = new ArrayList<>();
        toReturn.addAll(Arrays.asList(
                CommonAtomEvents.values()));
        toReturn.addAll(Arrays.asList(
                SeleniumAtomEvents.values()));

        return toReturn.toArray(new AtomEventSimple[0]);
    }

    @Deprecated
    private static final AtomEventSimple[] supportedEvents = {
            CommonAtomEvents.FORMAT_HELPER_SSN_GENERATED,
            CommonAtomEvents.ASSERT_ASSERTION_SUCCESS,
            CommonAtomEvents.ASSERT_ASSERTION_FAIL,
            CommonAtomEvents.ASSERT_ASSERTION_ERROR,
            SeleniumAtomEvents.ELEMENT_CLICKED_OK

    };


    private Reporter() {
        // Defeat instantiation
    }

    public static String getHtmlFilePath() {
        return "";
    }

    /**
     * Gets the {@link ExtentReports} instance created through the listener
     *
     * @return The {@link ExtentReports} instance
     */
    public static ExtentReports getExtentReport() {

        return ExtentService.getInstance();
//        return ExtentCucumberAdapter.getExtentReport();
    }


    /**
     * Loads the XML config file
     *
     * @param file The file path of the XML
     */
/*    public static void loadXMLConfig(File file) {
        getExtentHtmlReport().loadXMLConfig(file);
    }*/

    /**
     * Adds an info message to the current step
     *
     * @param message The message to be logged to the current step
     */
    public static void addInfoToStepLog(String message) {
        ExtentCucumberAdapter.addInfoToStepLog(message);
    }


    public static void addFailToStepLog(String message) {
        ExtentCucumberAdapter.addFailToStepLog(message);
    }

    /**
     * Adds an info message to the current scenario
     *
     * @param message The message to be logged to the current scenario
     */
    public static void addScenarioLog(String message) {
        getCurrentScenario().info(message);
    }

    /**
     * Adds the screenshot from the given path to the current step
     *
     * @param imagePath The image path
     */
    public static void addScreenCaptureFromPathToStepLog(String imagePath) {
        try {
            addScreenCaptureFromPathToStepLog(imagePath, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the screenshot from the given path with the given title to the current step
     *
     * @param imagePath The image path
     * @param title     The title for the image
     * @throws IOException Exception if imagePath is erroneous
     */
    public static void addScreenCaptureFromPathToStepLog(String imagePath, String title) throws IOException {
        //getCurrentStep().addScreenCaptureFromPath(imagePath, title);
        final Media media = MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build();
        media.setTitle(title);
        //getCurrentStep().log(Status.INFO, media);
        ExtentCucumberAdapter.addScreenCaptureFromPathToStepLog(media, title);
        // ExtentCucumberAdapter.addTestStepScreenCaptureFromPath(imagePath,title);
    }

    public static void addScreenCaptureFromPathToStep(String imagePath, String title) throws IOException {
        // getCurrentStep().addScreenCaptureFromPath(imagePath, title);
        //final Media media = MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build();
        //media.setTitle(title);
        //getCurrentStep().log(Status.INFO, media);
        ExtentCucumberAdapter.addTestStepScreenCaptureFromPath(imagePath, title);

    }


    /**
     * Sets the system information with the given key value pair
     *
     * @param key   The name of the key
     * @param value The value of the given key
     */
    public static void setSystemInfo(String key, String value) {
        if (systemInfoKeyMap.isEmpty() || !systemInfoKeyMap.containsKey(key)) {
            systemInfoKeyMap.put(key, false);
        }
        if (systemInfoKeyMap.get(key)) {
            return;
        }
        getExtentReport().setSystemInfo(key, value);
        systemInfoKeyMap.put(key, true);
    }


    /**
     * Sets the author name for the current scenario
     *
     * @param authorName The author name of the current scenario
     */
    public static void assignAuthor(String... authorName) {
        getCurrentScenario().assignAuthor(authorName);
    }

    @Deprecated
    public static ExtentTest getCurrentStep() {                                //[scol] amended
        throw new AtomException(" Deprecated, unexpected use of reporter Step");
        //return ExtentCucumberAdapter.getCurrentStep();
    }

    public static ExtentTest getCurrentScenario() {                            //[scol] amended
        return ExtentCucumberAdapter.getScenarioOutlineThreadLocal().get();
    }


    public static void addRunningScreenShot(WebDriver driver) {
        addRunningScreenShot(driver, "");
    }

    public static void addRunningScreenShot() {
        addRunningScreenShot(DriverFactory.getInstance().getDriver(), "");
    }

    /**
     * @param title String to be added as title to the screenshot
     */
    public static void addRunningScreenShot(String title) {
        addRunningScreenShot(DriverFactory.getInstance().getDriver(), title);
    }

    public static void addRunningScreenShotToLog(String title) {
        try {
            addScreenCaptureFromPathToStepLog(Screenshot.grabScreenshotToLocalFolder(DriverFactory.getInstance().getDriver()).get(SCREEN_SHOT_PARAM_RELATIVE_PATH), title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRunningScreenShot(WebDriver driver, String title) {
        try {
            addScreenCaptureFromPathToStepLog(Screenshot.grabScreenshotToLocalFolder(driver).get(SCREEN_SHOT_PARAM_RELATIVE_PATH), title);
            //addScreenCaptureFromPathToStep(Screenshot.grabScreenshotToLocalFolder(driver).get(SCREEN_SHOT_PARAM_RELATIVE_PATH), title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //[scol] added
    public static String getReportPath() {
        String defaultReportPath = System.getProperty("user.dir") + File.separator + "RunReports" + File.separator;
        String reportPath = Property.getVariable("reportPath");
        return (reportPath == null ? defaultReportPath : reportPath);
    }


    //[scol] added
    public static String getReportName() {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "RunReport_" + formatter.format(new Date()) + ".html";
    }

    //[scol] added
    public static String getScreenshotPath() {
        return getReportPath() + "Screenshots" + File.separator;
    }

    @Override
    @Deprecated
    public void eventDispatched(String event, AtomEventTarget source) {
        log.debug("get notification from " + source.getObjectToStore() + " on event " + event);
        Object targetObject = source.getObjectToStore();
        if (targetObject instanceof Element) {
            Element element = (Element) targetObject;
            final String elementReportCaption =
                    element.getElementReportCaption().equals("")
                            ? element.getStringOfBy()
                            : element.getElementReportCaption();

            final String impactingData = element.getImpactingData().equals("")
                    ? "No driving data"
                    : element.getImpactingData();

            addInfoToStepLog(ARROW_POINTING_DOWNWARDS_THEN_CURVING_RIGHTWARDS + "'" + event + "'"
                    + " on "
                    + "'" + elementReportCaption + "'"
                    + " with "
                    + "'" + impactingData + "'"
                    + " data");
        } else if (targetObject instanceof IAssert) {
            IAssert iAssert = (IAssert) targetObject;
            String message = (iAssert.getMessage() == null ? "" : " with message [" + iAssert.getMessage() + "]");
            String statusString = "[" + event + "]"
                    + message
                    + " expected "
                    + "[" + iAssert.getExpected() + "]"
                    + " get"
                    + "[" + iAssert.getActual() + "]";
            switch (event) {
                case CommonAtomEventsString.ASSERT_ASSERTION_ERROR:
                case CommonAtomEventsString.ASSERT_ASSERTION_FAIL:
                    statusString = CROSS_MARK + statusString;
                    addFailToStepLog(statusString);
                    break;
                case CommonAtomEventsString.ASSERT_ASSERTION_SUCCESS:
                    statusString = SLIGHTLY_SMILING_FACE + statusString;
                    addInfoToStepLog(statusString);
                    break;

            }
        } else if (targetObject instanceof AssertionError) {
            AssertionError assertionError = (AssertionError) targetObject;
           /*
            getCurrentStep().info("'" + event + "'"
                    + " with "
                    + "'" + assertionError.getMessage() + "'"
            );*/
        }
    }

    @Override
    public void eventDispatched(AtomEventSimple event, AtomEventTarget source) {
        log.debug("get notification from " + source.getObjectToStore() + " on event " + event);
        Object targetObject = source.getObjectToStore();
        if (targetObject instanceof Element) {
            //Element
            ElementHandler.handleEvent(event, (Element) targetObject);
        } else if (targetObject instanceof IAssert) {
            //IAssert
            IAssertHandler.handleEvent(event, (IAssert) targetObject);
        } else if (targetObject instanceof AssertionError) {
            //AssertionError
            AssertionError assertionError = (AssertionError) targetObject;
        }

    }


    /**
     * Adds an info message to the current step
     *
     * @param element that is used to get text from
     */
    public static void addElementLog(Element element) {

    }

    public static void addStepLog(String s) {
        addInfoToStepLog(s);
    }

    public static Reporter getInstance() {
        if (instance == null) {
            instance = new Reporter();
        }
        return instance;
    }
}