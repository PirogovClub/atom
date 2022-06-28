package automation.library.selenium.core;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.common.listeners.*;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.Getter;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.events.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static automation.library.selenium.core.Locator.Loc;
import static automation.library.selenium.core.Locator.getLocator;
import static automation.library.selenium.listener.SeleniumAtomEvents.*;


/**
 * Utility class providing set of selenium wrapper methods
 */
public class Element {
    @Getter
    private StopWatch stopWatch;
    private By by;
    private WebElement element;
    private WebDriver driver;
    private WebDriverWait wait;
    private String elementReportCaption = "";
    private String impactingData = "";
    protected Logger log = LogManager.getLogger(this.getClass().getName());


    public Element(WebDriver driver, WebElement e) {
        stopWatch = new StopWatch();
        stopWatch.start();
        this.driver = driver;
        wait = new WebDriverWait(driver, getWaitDuration());
        this.element = e;
    }


    public Element(WebDriver driver, WebElement e, By by) {
        stopWatch = new StopWatch();
        stopWatch.start();
        this.driver = driver;
        wait = new WebDriverWait(driver, getWaitDuration());
        this.element = e;
        this.by = by;
    }

    public Element(WebDriver driver, By by, int... delay) {
        stopWatch = new StopWatch();
        stopWatch.start();
        this.driver = driver;
        this.by = by;
        try {
            wait = new WebDriverWait(driver, delay.length > 0 ? delay[0] : getWaitDuration());
            this.element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            stopWatch.stop();
            log.debug("element located successfully:" + by.toString() + " took " + stopWatch.getTime() + " ms.");
        } catch (Exception e) {
            this.element = null;
            stopWatch.stop();
            log.debug("element not located:" + by.toString() + " took " + stopWatch.getTime() + " ms.");
            log.debug(e.getMessage());
        }
    }

    public Element(WebDriver driver, ExpectedCondition<?> exp, int... delay) {
        stopWatch = new StopWatch();
        stopWatch.start();
        this.driver = driver;
        this.by = null;
        try {
            wait = new WebDriverWait(driver, delay.length > 0 ? delay[0] : getWaitDuration());
            this.element = (WebElement) wait.until(exp);
            stopWatch.stop();
        } catch (Exception e) {
            this.element = null;
            stopWatch.stop();
            log.debug("element not located:" + by.toString() + " took " + stopWatch.getTime() + "  ms.");
            log.debug(e.getMessage());
        }
    }

    public Element withReportCaption(String captionToSet) {
        this.elementReportCaption = captionToSet;
        return this;
    }

    public Element withImpactingData(String impactingData) {
        this.impactingData = impactingData;
        return this;
    }

    public By by() {
        return by;
    }

    public String getStringOfBy() {
        if (this.by == null) {
            return "Element locator is not define (null)";
        } else {
            return this.by.toString();
        }

    }

    public WebElement element() {
        return element;
    }

    public MobileElement mobElement() {
        return (MobileElement) element;
    }

    /**
     * searches again for the element using the by
     *
     * @param retries number of retries
     * @return Element
     */
    public Element refind(int... retries) {
        log.info("Attempting to refind the element: " + by.toString());
        int attempts = 0;
        Boolean retry = true;
        int maxRetry = retries.length > 0 ? retries[0] : getFindRetries();
        while (attempts < maxRetry && retry) {
            try {
                log.debug("retry number " + attempts);
                this.element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
                this.element.getTagName();
                retry = false;
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    log.debug(e1.getMessage());
                }
            }
            attempts++;
        }
        return this;
    }


    /**
     * Returns a nested element
     *
     * @param by locator
     * @return ELement
     */
    public Element $(By by) {
        return new Element(driver, (WebElement) wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    public Element $(Loc type, String locator, Object... variables) {
        By by = getLocator(type, locator, variables);
        return new Element(driver, (WebElement) wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    /**
     * Returns list of nested elements
     *
     * @param by locator
     * @return Elements
     */
    public List<Element> $$(By by) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, by));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    public List<Element> $$(Loc type, String locator, Object... variables) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, getLocator(type, locator, variables)));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    /**
     * Returns a nested element
     *
     * @param by locator
     * @return Element
     */
    public Element findElement(By by) {
        return new Element(driver, (WebElement) wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    public Element findElement(Loc type, String locator, Object... variables) {
        By by = getLocator(type, locator, variables);
        return new Element(driver, (WebElement) wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.element, by)), by);
    }

    /**
     * Returns list of nested elements
     *
     * @param by locator
     * @return list of Element
     */
    public List<Element> findElements(By by) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, by));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    public List<Element> findElements(Loc type, String locator, Object... variables) {
        List<WebElement> els = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.element, getLocator(type, locator, variables)));
        List<Element> list = new ArrayList<Element>();
        for (WebElement el : els) {
            list.add(new Element(driver, el));
        }
        return list;
    }

    /**
     * wait for the element to become visible
     *
     * @param retries number of retries
     * @return Element
     */
    public Element visible(int... retries) {
        this.element = wait.until(ExpectedConditions.visibilityOf(this.element));
        return this;
    }


    /**
     * wait for the element to become clickable
     */
    public Element clickable(int... retries) {
        try {
            this.element = wait.until(ExpectedConditions.elementToBeClickable(this.element));
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.clickable(0);
            } else {
                if (this.element == null) {
                    throw new RuntimeException("Element with locator " + this.by() + " do not exist");
                }
                throw e;

            }
        }
        return this;
    }

    public String getText(int... retries) {
        String str = null;
        try {
            str = this.element.getText();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.getText(0);
            } else {
                throw e;
            }
        }
        return str;
    }

    public String getValue(int... retries) {
        String str = null;
        try {
            str = this.element.getAttribute("value");
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.getValue(0);
            } else {
                throw e;
            }
        }
        return str;
    }

    public String getAttribute(String attr, int... retries) {
        try {
            return this.element.getAttribute(attr);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                return this.getAttribute(attr, 0);
            } else {
                throw e;
            }
        }
    }

    public Map<String, String> getAttributes(int... retries) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var items = {}; " +
                "for (index = 0; index < arguments[0].attributes.length; ++index) " +
                "{ " +
                "items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value " +
                "}; " +
                "return items;";

        Map<String, String> list;

        try {
            list = (Map<String, String>) js.executeScript(script, this.element);
            return list;
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                list = (Map<String, String>) js.executeScript(script, this.element);
                return list;
            } else {
                throw e;
            }
        }
    }

    public Element clear(int... retries) {
        try {
            this.element().clear();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.clear(0);
            } else {
                throw e;
            }
        }
        return this;
    }

    public Element clearAndSendKeys(String val, int... retries) {
        return this.clear(retries).sendKeys(val, retries);
    }

    public Element clearWaitAndSendKeys(String val, int... retries) {
        return this.clear(retries).waitPageToLoad().sendKeys(val, retries);
    }


    public Element sendKeys(String val, int... retries) {
        try {
            try {
                this.element().sendKeys(val);
            } catch (Exception e) {
                if (!(retries.length > 0 && retries[0] == 0)) {
                    this.refind(retries);
                    this.sendKeys(val, 0);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            if (checkSendKeysJS()) {
                sendKeysJS(val);
            } else {
                throw e;
            }
        }
        getEventManager().notify(ELEMENT_SEND_KEYS, new AtomEventTargetImpl(this));
        return this;
    }

    public Element click(int... retries) {

        this.clickable();

        try {
            try {
                this.element().click();
            } catch (Exception e) {
                if (!(retries.length > 0 && retries[0] == 0)) {
                    this.refind(retries);
                    this.click(0);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            if (checkClickJS()) {
                clickJS();
            } else {
                throw e;
            }
        }
        getEventManager().notify(ELEMENT_CLICKED_OK, new AtomEventTargetImpl(this));
        return this;
    }


    /**
     * Set checkbox to check or uncheck no matter what initial state was
     *
     * @param setToCheck - if "true" would be left as checked, if "false" left as unchecked
     * @param appName    - Application name for determination witch parameter look to check if selected or not
     * @return Element in set condition
     */
    public Element setCheckedStatus(Boolean setToCheck, AppNames appName) {

        Element elementToReturn = this;
        boolean isSelected;

        switch (appName) {
            case eapp:
            default:
                isSelected = this.getAttribute("class").contains("radioChecked");
        }

        if (setToCheck) {
            if (!isSelected) {
                elementToReturn = this.clickable().click();
            }
        } else {
            if (isSelected) {
                elementToReturn = this.clickable().click();
            }
        }
        return elementToReturn;

    }

    /**
     * Set checkbox to check or uncheck no matter what initial state was In Eapp application
     *
     * @param setToCheck - if "true" would be left as checked, if "false" left as unchecked
     * @return Element in set condition
     */

    public Element setCheckedStatusEapp(Boolean setToCheck) {
        return setCheckedStatus(setToCheck, AppNames.eapp);
    }


    /**
     * send text using character chord to overwrite field
     */
    public Element sendKeysChord(String val, int... retries) {
        try {
            this.element.sendKeys(Keys.chord(Keys.CONTROL, "a"), val);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.sendKeysChord(val, 0);
            } else {
                throw e;
            }
        }
        return this;
    }

    /**
     * send text using character chord to overwrite field
     */
    public Element sendKeysChord(Keys key, int... retries) {
        try {
            this.element.sendKeys(Keys.chord(Keys.CONTROL, "a"), key);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.sendKeysChord(key, 0);
            } else {
                throw e;
            }
        }
        return this;
    }

    /**
     * mimic hitting the enter key
     */
    public Element sendEnter(int... retries) {
        try {
            this.element.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.sendEnter(0);
            } else {
                throw e;
            }
        }
        return this;
    }

    /**
     * select action for checkboxes / radio buttons
     */
    public Element select(Boolean val, int... retries) {
        try {
            if (this.element.isSelected() != val)
                this.element.click();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.select(val, 0);
            } else {
                throw e;
            }
        }
        return this;
    }

    /**
     * select action for checkboxes / radio buttons
     */
    public Element select(int... retries) {
        try {
            if (!this.element.isSelected())
                this.element.click();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.select(0);
            } else {
                throw e;
            }
        }
        return this;
    }

    /**
     * deselect action for checkboxes / radio buttons
     */
    public Element unselect(int... retries) {
        try {
            if (this.element.isSelected())
                this.element.click();
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.unselect(0);
            } else {
                throw e;
            }
        }
        return this;
    }

    /**
     * Return select object for a WebElement
     */
    public Select dropdown(int... retries) {
        Select sel = null;
        try {
            sel = new Select(this.element);
        } catch (Exception e) {
            if (!(retries.length > 0 && retries[0] == 0)) {
                this.refind(retries);
                this.dropdown(0);
            } else {
                throw e;
            }
        }
        return sel;
    }

    /**
     * Performs mouse click action on the element using javascript where native click does not work
     */
    public Element clickJS() {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", this.element);
        return this;
    }

    /**
     * send text using javascript
     */
    public Element sendKeysJS(String val) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].setAttribute('value', '" + val + "')", this.element);
        return this;
    }

    public Boolean isVisible() {
        return (this.visible() != null);
    }


    /**
     * Return all options within a dropdown as string array
     */
    public List<String> getDropdownOptionsValues() {
        List<String> optionsText = new ArrayList<String>();
        List<WebElement> options = this.dropdown().getOptions();
        for (WebElement option : options) {
            optionsText.add(option.getAttribute("value"));
        }
        return optionsText;
    }

    /**
     * Return all options within a dropdown as string array
     */
    public List<String> getDropdownOptionsText() {
        List<String> optionsText = new ArrayList<String>();
        List<WebElement> options = this.dropdown().getOptions();
        for (WebElement option : options) {
            optionsText.add(option.getText());
        }
        return optionsText;
    }

    /**
     * Return all options groups within a dropdown as string array
     */
    public List<String> getDropdownOptGroupsText() {
        List<String> optGroupsText = new ArrayList<String>();
        List<WebElement> optGroups = this.element().findElements(By.tagName("optgroup"));
        for (WebElement optGroup : optGroups) {
            optGroupsText.add(optGroup.getText());
        }
        return optGroupsText;
    }

    /**
     * Return all options groups within a dropdown as list of elements
     */
    public List<WebElement> getDropdownOptGroupsElements() {
        List<WebElement> optGroups = this.element().findElements(By.tagName("optgroup"));
        return optGroups;
    }

    /**
     * Return all options within an option group of a dropdown as string array
     */
    public List<String> getDropdownOptionsTextWithinGroup(String group) {
        List<String> optionsText = new ArrayList<String>();
        List<WebElement> options = this.element().findElements(By.xpath("//optgroup[@label=" + group + "]/option"));
        for (WebElement option : options) {
            optionsText.add(option.getText());
        }
        return optionsText;
    }

    /**
     * Return all options within an option group of a dropdown as list of elements
     */
    public List<WebElement> getDropdownOptionsElementsWithinGroup(String group) {
        List<WebElement> options = this.element().findElements(By.xpath("//optgroup[@label=" + group + "]/option"));
        return options;
    }


    /**
     * Return all inner text within a list of elements as string array
     */
    public List<String> getAllText(List<Element> els) {
        List<String> elementsText = new ArrayList<String>();
        for (Element el : els) {
            elementsText.add(el.element().getText());
        }
        return elementsText;
    }

    /**
     * Performs mouse action move to element on the screen
     */
    public Element move() {
        Actions action = new Actions(driver);
        action.moveToElement(this.element).build().perform();
        return this;
    }

    public Element moveTo(int xOffset, int yOffset) {
        Actions action = new Actions(driver);
        action.moveToElement(this.element,xOffset,  yOffset).build().perform();
        return this;
    }

    /**
     * moveToElement* method moves to the middle of element
     * @param xOffset : X offset from the middle
     * @param yOffset : Y offset from the middle
     * @return
     */
    public Element moveToAndClick(int xOffset, int yOffset) {
        Actions action = new Actions(driver);
        action.moveToElement(this.element,xOffset,  yOffset).click().build().perform();
        return this;
    }

    /**
     * Performs mouse action move to a parent element on the screen, locate child element and click
     */
    public Element moveAndClick(WebElement elChild) {
        return moveAndClick(new Element(driver, elChild));
    }

    public Element moveAndClick(Element elChild) {
        Actions action = new Actions(driver);
        action.moveToElement(this.element).build().perform();
        elChild.click();
        return this;
    }

    public Element moveAndClick(int xOffset, int yOffset, Element elChild) {
        Actions action = new Actions(driver);
        action.moveToElement(this.element, xOffset,  yOffset).build().perform();
        elChild.click();
        return this;
    }

    /**
     * Performs mouse action click and hold
     */
    public Element clickAndHold() {
        Actions action = new Actions(driver);
        action.clickAndHold(this.element).build().perform();
        return this;
    }

    /**
     * Performs mouse action release button
     */
    public Element release() {
        Actions action = new Actions(driver);
        action.release().build().perform();
        return this;
    }

    /**
     * Performs mouse action release button
     */
    public Element dblClick() {
        Actions action = new Actions(driver);
        action.doubleClick(this.element).build().perform();
        return this;
    }

    /**
     * Highlights an element with a blue border.....useful when debugging/taking screenshots
     */
    public Element highlight() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "arguments[0].style.border";
        String border = "3px solid blue";
        js.executeScript(script + "='" + border + "'", this.element);
        return this;
    }

    /**
     * Returns duration for specified waits
     */
    public static int getWaitDuration() {
        final int defaultWait = Constants.DEFAULT_WAIT;
        int duration;
        try {
            duration = Property.getProperties(automation.library.selenium.exec.Constants.SELENIUMRUNTIMEPATH).getInt(Constants.DEFAULT_WAIT_KEY);
        } catch (Exception e) {
            duration = defaultWait;
        }
        return duration;
    }

    public static int getFindRetries() {
        final int defaultFindRetries = 1;
        int refind;
        try {
            refind = Property.getProperties(automation.library.selenium.exec.Constants.SELENIUMRUNTIMEPATH).getInt(Constants.DEFAULT_RETRY_KEY);
        } catch (Exception e) {
            refind = defaultFindRetries;
        }
        return refind;
    }

    /**
     * Returns true if clickUsesJavaScript.browsername = true in "rc/test/resources/config/selenium/runtime.properties"
     */
    public Boolean checkClickJS() {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase().replaceAll("\\s", "");

        return Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getBoolean("clickUsesJavaScript." + browserName, false);
    }

    /**
     * Returns true if sendKeysUsesJavaScript.browsername = true in "rc/test/resources/config/selenium/runtime.properties"
     */
    public Boolean checkSendKeysJS() {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase().replaceAll("\\s", "");

        return Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getBoolean("sendKeysUsesJavaScript." + browserName, false);
    }

    /**
     * Scrolls to element to avoid issues with element location being unclickable
     */
    public Element scroll() {

        if (!(driver instanceof AndroidDriver) && !(driver instanceof IOSDriver)) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element());
            } catch (Exception e) {
                log.warn("scrolling to element failed", e);
            }
        }
        return this;
    }

    /**
     * In developing mode can be used to slowdown execution, delay to be set in
     * BASEPATH + "config/selenium/" + "devMode.properties"
     * <p>
     * if Now value in the property file would have wait for 0 milliseconds
     *
     * @return Element
     */
    public Element devSleep() {
        return devSleep(Property.getProperties(Constants.DEV_MOD_PATH).getInt("devModeDelay", 0));
    }

    /**
     * In developing mode can be used to slowdown execution,
     *
     * @param millisecond - delays in milliseconds
     * @return Element
     */
    public Element devSleep(int millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            log.error(e.getStackTrace());
        }
        return this;
    }


    /**
     * Wait for page to load
     */
    public Element waitPageToLoad() {
        JsLoadWait.domLoaded(driver, wait);
        JsLoadWait.jqueryLoaded(driver, wait);
        JsLoadWait.angularLoaded(driver, wait);
        return this;
    }


    /**
     * Get count of number of table rows
     */
    public int getTableRowCount() {
        return getDataRowCount() + getHeadRowCount();
    }

    /**
     * Get count of number of table header rows
     */
    public int getHeadRowCount() {
        return this.findElements(By.tagName("th")).size();
    }

    /**
     * Get count of number of table data rows
     */
    public int getDataRowCount() {
        return this.findElements(By.tagName("tr")).size();
    }

    /**
     * Get count of number of table header columns
     */
    public int getHeadColumnCount(int rowIndex) {
        return getHeadRowElements(rowIndex).size();
    }

    /**
     * Get count of number of table data columns
     */
    public int getDataColumnCount(int rowIndex) {
        return getDataRowElements(rowIndex).size();
    }

    /**
     * Get the list of header columns within a given row of a table
     */
    public List<Element> getHeadRowElements(int rowIndex) {
        return this.findElements(By.tagName("tr")).get(rowIndex).findElements(By.tagName("th"));
    }

    /**
     * Get the list of data columns within a given row of a table
     */
    public List<Element> getDataRowElements(int rowIndex) {
        return this.findElements(By.tagName("tr")).get(rowIndex).findElements(By.tagName("td"));
    }

    /**
     * Get all rows of a table
     */
    public List<Element> getAllRows() {
        return this.findElements(By.tagName("tr"));
    }

    /**
     * Get table row based on row index
     */
    public Element getRow(int row) {
        return this.findElements(By.tagName("tr")).get(row);
    }

    /**
     * Get table data cell based on row index and column index
     */
    public Element getDataCellElement(int rowIndex, int columnIndex) {
        return getDataRowElements(rowIndex).get(columnIndex);
    }

    /**
     * Get table header cell based on row index and column index
     */
    public Element getHeadCellElement(int rowIndex, int columnIndex) {
        return getHeadRowElements(rowIndex).get(columnIndex);
    }

    /**
     * Get table row based on matching a specified value against a value held in a given column
     */
    public Element tableGetRow(String val, int matchCol) {
        List<Element> rows = this.findElements(By.tagName("tr"));
        Element el = null;
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).findElements(By.tagName("td")).get(matchCol).getText().equalsIgnoreCase(val)) {
                el = rows.get(i);
                break;
            }
        }
        return el;
    }

    /**
     * Get all data held in a table and return as a string array
     */
    public ArrayList<ArrayList<String>> getTableAsArray() {
        ArrayList<ArrayList<String>> tabledata = new ArrayList<ArrayList<String>>();
        List<WebElement> rows = this.element().findElements(By.tagName("tr"));
        int numrows = rows.size();
        for (int i = 0; i < numrows; i++) {
            WebElement row = rows.get(i);
            List<WebElement> cols = row.findElements(By.tagName("td"));
            if (cols.size() > 0) {
                ArrayList<String> rowdata = new ArrayList<String>();
                for (int j = 0; j < cols.size(); j++) {
                    rowdata.add(cols.get(j).getText().trim());
                }
                tabledata.add(rowdata);
            }
        }
        return tabledata;

    }

    public String getElementReportCaption() {
        return elementReportCaption;
    }

    public String getImpactingData() {
        return impactingData;
    }

    private AtomEventManager getEventManager() {
        return TestContext.getInstance().getAtomEventManager();
    }

}
