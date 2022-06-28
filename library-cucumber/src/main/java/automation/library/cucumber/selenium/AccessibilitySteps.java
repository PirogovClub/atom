/**
 * Copyright (C) 2015 Deque Systems Inc.,
 *
 * Your use of this Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This entire copyright notice must appear in every copy of this file you
 * distribute or in any file that contains substantial portions of this source
 * code.
 */
package automation.library.cucumber.selenium;

import automation.library.common.Property;
import com.deque.axe.AXE;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Helper class to run the accessibility test
 */
public class AccessibilitySteps extends BaseSteps {

    private URL scriptUrl = getClass().getClassLoader().getResource("accessibility/axe.min.js");


    public String getAccessibilityReportPath() {
        String defaultReportPath = System.getProperty("user.dir") + File.separator + "AccessibilityReports" + File.separator;
        String reportPath = Property.getVariable("accessibilityReportPath");
        return (reportPath == null ? defaultReportPath : reportPath);
    }

    public File generateReportFile() {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new java.util.Date());

        File file = new File(getAccessibilityReportPath() + "AccessibilityViolations_ " + timeStamp + ".txt");

        try {
            new File(getAccessibilityReportPath()).mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public void determineViolations(JSONArray violations) {
        if (violations.length() == 0) {
            Assert.assertTrue(true, "No violations found");
        } else {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(generateReportFile()))) {
                bw.write(AXE.report(violations));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void determineViolations(String pageUrl, JSONArray violations) {
        if (violations.length() == 0) {
            Assert.assertTrue(true, "No violations found");
        } else {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(generateReportFile()))) {
                bw.write(pageUrl);
                bw.write("\n\n");
                bw.write(AXE.report(violations));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Basic test
     */
    public void testAccessibility() {
        String pageUrl = getDriver().getCurrentUrl();
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl).analyze();
        JSONArray violations = responseJSON.getJSONArray("violations");
        determineViolations(pageUrl, violations);
    }

    /**
     * Basic test
     * @param pageUrl url to check
     */
    public void testAccessibility(String pageUrl) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl).analyze();
        JSONArray violations = responseJSON.getJSONArray("violations");
        determineViolations(pageUrl, violations);
    }

    /**
     * Test with options
     * @param pageUrl url of the page to be tested
     */
    public void testAccessibilityWithOptions(String pageUrl) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl)
                .options("{ rules: { 'accesskeys': { enabled: false } } }")
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        determineViolations(violations);
    }

    /**
     * Test includes and excludes
     * @param pageUrl page url to check
     * @param includedElem include element
     * @param excludedElem exlucde element
     */
    public void testAccessibilityWithIncludesAndExcludes(String pageUrl, String includedElem, String excludedElem) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl)
                .include(includedElem)
                .exclude(excludedElem)
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        determineViolations(violations);
    }

    /**
     * Test a WebElement
     * @param pageUrl page url
     * @param tag tag
     */
    public void testAccessibilityWithWebElement(String pageUrl, String tag) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl)
                .analyze(getDriver().findElement(By.tagName(tag)));

        JSONArray violations = responseJSON.getJSONArray("violations");

        determineViolations(violations);
    }
}