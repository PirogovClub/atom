package automation.library.reporting.service;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EgoistExtent {
    @Getter
    private static List<EgoistExtent> threads = new ArrayList<>();
    @Getter
    private String reportFileName;

    private EgoistExtent(long threadID, String egoistReportFileName) {

        this.threadToEnvID = threadID;
        report = new ExtentReports();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        reportFileName = "RunReports/" + formatter.format(new Date()) + "_" + egoistReportFileName + threadID + "_.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportFileName);
        report.attachReporter(spark);
    }

    @Getter
    private ExtentReports report;

    private long threadToEnvID;

    public static synchronized EgoistExtent getInstance() {
        return getInstance("");
    }

    public static synchronized EgoistExtent getInstance(String egoistReportFileName) {
        long currentRunningThreadID = Thread.currentThread().getId();
        for (EgoistExtent thread : threads) {
            if (thread.threadToEnvID == currentRunningThreadID) {
                return thread;
            }
        }
        EgoistExtent temp = new EgoistExtent(currentRunningThreadID, egoistReportFileName);
        threads.add(temp);
        return temp;
    }


}
