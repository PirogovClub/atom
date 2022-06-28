package automation.library.common;



import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {
    public static void createOrCleanDirectory(String qtestReportsFolder) throws IOException {
        Files.createDirectories(Paths.get(System.getProperty("user.dir") + File.separator + qtestReportsFolder));
        FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + File.separator + qtestReportsFolder));
    }
}
