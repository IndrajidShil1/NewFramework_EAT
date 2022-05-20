package base;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reports.ExtentManager;
import reports.ExtentTestManager;
import utility.Utility;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CommonAPI {
    Properties prop = Utility.loadProperties();

    String implicitWait = prop.getProperty("implicit.wait", "5");
    String browserMaximize = prop.getProperty("browser.maximize", "false");
    String takeScreenshot = prop.getProperty("take.screenshot", "false");
    String browserstackUsername = prop.getProperty("browserstack.username");
    String browserstackPassword = prop.getProperty("browserstack.password");
    String saucelabsUsername = null;
    String saucelabsPassword = null;

    public WebDriver driver;

    public static com.relevantcodes.extentreports.ExtentReports extent;

    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void startExtent(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName().toLowerCase();
        ExtentTestManager.startTest(method.getName());
        ExtentTestManager.getTest().assignCategory(className);
    }
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));

        for (String group : result.getMethod().getGroups()) {
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        }
        ExtentTestManager.endTest();
        if (takeScreenshot.equalsIgnoreCase("true")){
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshot(result.getName());
            }
        }
        extent.flush();
        driver.quit();
    }

    @AfterSuite
    public void generateReport() {
        extent.close();
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public WebDriver getLocalDriver(String browserName, String os){
        if (browserName.equalsIgnoreCase("chrome")){
            if (os.equalsIgnoreCase("OS X")){
                System.setProperty("webdriver.chrome.driver", Utility.root+"/src/driver/chromedriver");
            }else if(os.equalsIgnoreCase("windows")){
                System.setProperty("webdriver.chrome.driver", Utility.root+"/src/driver/chromedriverexe");
            }
            driver = new ChromeDriver();
        }else if(browserName.equalsIgnoreCase("firefox")){
            if (os.equalsIgnoreCase("OS X")){
                System.setProperty("webdriver.gecko.driver", Utility.root+"/src/driver/geckodriver");
            }else if (os.equalsIgnoreCase("windows")){
                System.setProperty("webdriver.gecko.driver", Utility.root+"/src/driver/geckodriver.exe");
            }
            driver = new FirefoxDriver();
        }
        return driver;
    }

    public WebDriver getCloudDriver(String envName, String EnvUsername, String envAccessKey, String os, String os_version, String browserName, String browserVersion) throws MalformedURLException, MalformedURLException {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browser", browserName);
        cap.setCapability("browser_version", browserVersion);
        cap.setCapability("os", os);
        cap.setCapability("os_version", os_version);
        if(envName.equalsIgnoreCase("saucelabs")){
            driver = new RemoteWebDriver(new URL("http://"+ EnvUsername + ":" + envAccessKey + "@ondemand.saucelabs.com:80/wd/hub"), cap);
        }else if(envName.equalsIgnoreCase("browserstack")){
            cap.setCapability("resolution", "1024x768");
            driver = new RemoteWebDriver(new URL("http://"+ EnvUsername + ":" + envAccessKey + "@hub-cloud.browserstack.com:80/wd/hub"), cap);
        }
        return driver;
    }

    @Parameters({"useCloudEnv","envName","os","os_version","browserName","browserVersion","url"})
    @BeforeMethod
    public void beforeTest(@Optional("false") Boolean useCloudEnv, @Optional("browserstack")String envName,
                           @Optional("OS X")String os, @Optional("Monterey")String os_version,
                           @Optional("chrome")String browserName, @Optional("99")String browserVersion,
                           @Optional("http://www.google.com")String url) throws MalformedURLException {
        if (useCloudEnv){
            if (envName.equalsIgnoreCase("browserstack")){
                getCloudDriver(envName, Utility.decode(browserstackUsername), Utility.decode(browserstackPassword), os, os_version, browserName, browserVersion);
            } else if (envName.equalsIgnoreCase("saucelabs")){
                getCloudDriver(envName, Utility.decode(saucelabsUsername), Utility.decode(saucelabsPassword), os, os_version, browserName, browserVersion);
            }
        }else {
            getLocalDriver(browserName, os);
        }
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(implicitWait), TimeUnit.SECONDS);
        if (browserMaximize.equalsIgnoreCase("true")){
            driver.manage().window().maximize();
        }

        driver.get(url);
    }


    public WebDriver getDriver() {
        return driver;
    }

    public String getPageTitle(){
        return driver.getTitle();
    }

    public String getElementText(WebElement element){
        return element.getText();
    }

    public void click(WebElement element){
        element.click();
    }

    public void clear(WebElement element){
        element.clear();
    }

    public void type(WebElement element, String text){
        element.sendKeys(text);
    }

    public void typeAndEnter(WebElement element, String text){
        element.sendKeys(text, Keys.ENTER);
    }

    public void selectFromDropdown(WebElement dropdown, String option){
        try{
            Select select =new Select(dropdown);
            try{
                select.selectByVisibleText(option);
            }catch (Exception e){
                select.selectByValue(option);
            }
        }catch (Exception e){
            Select select =new Select(dropdown);
            try{
                select.selectByVisibleText(option);
            }catch (Exception e1){
                select.selectByValue(option);
            }
        }
    }

    public void selectOptionFromDropdownList(WebElement dropdown, String option){
        try {
            Select select = new Select(dropdown);
            List<WebElement> list = select.getOptions();
            for (WebElement element: list) {
                if (element.getText().equalsIgnoreCase(option)){
                    element.click();
                }
            }
        }catch (Exception e){
            Select select = new Select(dropdown);
            List<WebElement> list = select.getOptions();
            for (WebElement element: list) {
                if (element.getText().equalsIgnoreCase(option)){
                    element.click();
                }
            }
        }

    }

    public void hoverOver(WebDriver driver, WebElement element){
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    public boolean isPresent(WebElement element){
        return element.isDisplayed();
    }

    public void captureScreenshot() {
        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file,new File("screenshots/screenshot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeScreenshot(String screenshotName){
        DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
        Date date = new Date();
        df.format(date);

        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(System.getProperty("user.dir")+File.separator+ "screenshots"+File.separator+screenshotName+" "+df.format(date)+".png"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            String path = System.getProperty("user.dir")+ "/screenshots/"+screenshotName+" "+df.format(date)+".png";
            System.out.println(path);
            System.out.println("Exception while taking screenshot "+e.getMessage());;
        }
    }

}
