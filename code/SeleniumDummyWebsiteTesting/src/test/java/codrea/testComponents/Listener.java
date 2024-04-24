package codrea.testComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listener extends BaseTest implements ITestListener{
    private ExtentReports extentReports;
    private ExtentTest extentTest;
    private final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    @Override
    public void onTestStart(ITestResult result) {

        extentTest = extentReports.createTest(result.getMethod().getMethodName());
        extentTestThreadLocal.set(extentTest);

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTestThreadLocal.get().log(Status.PASS,"Test Passed!");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTestThreadLocal.get().fail(result.getThrowable());

        try {
            driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }


        String filePath = getScreenshot(driver,result.getMethod().getMethodName());

        extentTest.addScreenCaptureFromPath(filePath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTestThreadLocal.get().log(Status.SKIP,"Test skipped!");
    }

    @Override
    public void onStart(ITestContext context) {
       extentReports = ExtentReporter.getExtentReportsObject(context.getSuite().getName());
       ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
    }


}
