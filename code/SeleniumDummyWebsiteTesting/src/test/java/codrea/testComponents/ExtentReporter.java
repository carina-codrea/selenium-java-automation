package codrea.testComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporter {
    public static ExtentReports getExtentReportsObject(String suiteName){
        ExtentSparkReporter reporter = new ExtentSparkReporter(System.getProperty("user.dir") +"//reports//" + suiteName + ".html");
        reporter.config().setDocumentTitle("Rahul Shetty Academy Dummy Web Application");
        reporter.config().setReportName("Validations");

        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Tester","Codrea Carina");

        return  extentReports;
    }
}
