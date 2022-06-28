package automation.library.selenium.core;

import automation.library.common.AtomException;
import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.common.listeners.AtomEventManager;
import automation.library.common.listeners.AtomEventTargetImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static automation.library.selenium.core.Constants.SCREEN_SHOT_PARAM_ABSOLUTE_PATH;
import static automation.library.selenium.core.Constants.SCREEN_SHOT_PARAM_RELATIVE_PATH;
import static automation.library.selenium.listener.SeleniumAtomEvents.SCREEN_SHOT_TAKEN;

/**
 * Base class providing set of common selenium methods
 */

public class Screenshot {

    /**
     * capture displayed area or scrolling screenshot and return a file object.
     * to capture scrolling screenshot property scrollingScreenshot = true has to be set in runtime.properties file
     */
    public static File grabScreenshot(WebDriver driver) {
        String screenshotType = Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("scrollingScreenshot");

        if (screenshotType != null) {
            return (screenshotType.equals("true") ? grabScrollingScreenshot(driver) : grabDisplayedAreaScreenShot(driver));
        } else {
            return grabDisplayedAreaScreenShot(driver);
        }
    }

    public static Map<String,String> grabScreenshotToLocalFolder(WebDriver driver) {

        File img = grabScreenshot(driver);





        if(img!=null){
            File file = saveScreenshot(img, getScreenshotPath());
            String relativePath = "." + File.separator + "Screenshots" + File.separator + file.getName();
            String absolutePath = file.getAbsolutePath();
            var mapToReturn = new HashMap<String,String>();
            mapToReturn.put(SCREEN_SHOT_PARAM_ABSOLUTE_PATH,  absolutePath);
            mapToReturn.put(SCREEN_SHOT_PARAM_RELATIVE_PATH,  relativePath);
            getEventManager().notify(SCREEN_SHOT_TAKEN, new AtomEventTargetImpl(mapToReturn));
            return mapToReturn;

        } else {
            throw new AtomException("Can't take a screenshot");
        }
    }

    public static String getScreenshotPath() {
        return getReportPath() + "Screenshots" + File.separator;
    }

    public static String getReportPath() {
        String defaultReportPath = System.getProperty("user.dir") + File.separator + "RunReports" + File.separator;
        String reportPath = Property.getVariable("reportPath");
        return (reportPath == null ? defaultReportPath : reportPath);
    }


    /**
     * capture screenshot for the displayed area and return a file object
     */
    public static File grabDisplayedAreaScreenShot(WebDriver driver) {
        try {
            Thread.sleep(Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getInt("screenshotDelay", 0));
        } catch (InterruptedException | NumberFormatException e) {
            e.printStackTrace();
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

    }

    /**
     * capture scrolling screenshot and return a file object
     */
    public static File grabScrollingScreenshot(WebDriver driver) {
        try {
            Thread.sleep(Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getInt("screenshotDelay", 0));
        } catch (InterruptedException | NumberFormatException e) {
            e.printStackTrace();
        }

        ru.yandex.qatools.ashot.Screenshot screenshot;

        if (System.getProperties().get("os.name").toString().contains("Mac")) {
            screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(100, 0, 0, 2)).takeScreenshot(driver);
        } else {
            screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        }

        File file = new File("image.png");

        try {
            ImageIO.write(screenshot.getImage(), "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * grab screenshot snippet
     */
    public static File snipScreenshot(File screenshot, By by, Dimension dim, Point point) {

        try {
            BufferedImage buffer = ImageIO.read(screenshot);
            // Crop the entire page screenshot to get only element screenshot
            BufferedImage snippet = buffer.getSubimage(0, point.getY(), point.getX() + dim.width, dim.height);
            ImageIO.write(snippet, "png", screenshot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenshot;
    }

    /**
     * capture screenshot and save to specified location
     */
    public static File saveScreenshot(File screenshot, String filePath) {
        UUID uuid = UUID.randomUUID();
        File file = new File(filePath + uuid + ".jpg");

        try {
            Files.createDirectories(file.getParentFile().toPath());
            final FileInputStream fileInputStream = new FileInputStream(screenshot);
            final BufferedImage image = ImageIO.read(fileInputStream);
            fileInputStream.close(); // ImageIO.read does not close the input stream

            final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

            final FileOutputStream fileOutputStream = new FileOutputStream(file);

            final boolean canWrite = ImageIO.write(convertedImage, "jpg", fileOutputStream);
            fileOutputStream.close(); // ImageIO.write does not close the output stream

            if (!canWrite) {
                throw new IllegalStateException("Failed to write image.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileUtils.forceDeleteOnExit(screenshot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * capture screenshot and save to specified location
     */
    public static File saveScreenshot(File screenshot, String filePath, String fileName) {
        File file = new File(filePath + fileName + ".png");
        try {
            FileUtils.moveFile(screenshot, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Compare the screenshot
     */
    public static Boolean compareScreenshot(File fileExpected, File fileActual) throws IOException {

        BufferedImage bufileActual = ImageIO.read(fileActual);
        BufferedImage bufileExpected = ImageIO.read(fileExpected);

        DataBuffer dafileActual = bufileActual.getData().getDataBuffer();
        DataBuffer dafileExpected = bufileExpected.getData().getDataBuffer();

        int sizefileActual = dafileActual.getSize();

        boolean matchFlag = true;

        for (int j = 0; j < sizefileActual; j++) {
            if (dafileActual.getElem(j) != dafileExpected.getElem(j)) {
                matchFlag = false;
                break;
            }
        }

        return matchFlag;
    }

    private static AtomEventManager getEventManager() {
        return TestContext.getInstance().getAtomEventManager();
    }

}
