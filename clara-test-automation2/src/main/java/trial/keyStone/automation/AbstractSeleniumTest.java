package trial.keyStone.automation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.paulhammant.ngwebdriver.NgWebDriver;

//@Listeners({ com.bravadohealth.pageObjects.CustomListener.class })
public abstract class AbstractSeleniumTest implements WebOperation, ITest {

	public static final String DEFAULT = "default";
	public static String failureClass="None";
	public static String failureMethod="None";
	
	private String testName = "seleniumTest";

	protected Integer       globalWaitTime = 60;
	protected ThreadLocal<Map<String, String>>       data                       = new ThreadLocal<Map<String, String>>();
	protected ThreadLocal<WebDriver>                 driver                     = new ThreadLocal<WebDriver>();
	protected ThreadLocal<ExcelData>                 excelData                  = new ThreadLocal<ExcelData>();
	protected ThreadLocal<PageDataProvider>          pageDataProvides           = new ThreadLocal<PageDataProvider>();
	protected AtomicInteger logCounter     = new AtomicInteger(0);
	protected ThreadLocal<String>                    logFiles                   = new ThreadLocal<String>();
	protected ThreadLocal<SoftAssert> softAssert = new ThreadLocal<SoftAssert>();
	protected ThreadLocal<ArrayList<AuditAction>> Alogs = new ThreadLocal <ArrayList<AuditAction>>();
	public HashMap<String,String> hMap=new HashMap<String,String>();
	protected Logger               logger      = new DelegatingLogger(LoggerFactory.getLogger(getClass())) {

		@Override
		protected void beforeLogging() {

			MDC.put("scenarioFile", getScenarioLogFile());

		}
	};

	public Logger logger() {

		return logger;
	}

	public HashMap<String,String> gethMap()
	{
		return hMap;
	}

	protected String getScenarioLogFile()
	{
		String logFile = logFiles.get();
		if (logFile == null)
		{
			logFile = getClass().getSimpleName() + logCounter.incrementAndGet() + ".log";
			logFiles.set(logFile);
			System.out.println(logFiles.get());
		}
		return logFile;
	}
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(Method method, Object[] testData) throws Exception
	{
		ExcelData exceldata = (ExcelData) testData[0];
		
		testName = exceldata.getId();
		driver.set(getDriver());
	}

	public WebDriver driver()
	{
		return driver.get();
	}

	public WebDriver nativedriver()
	{
		return ((ClaraWebDriver) driver.get()).getNativeDriver();
	}
	
	public WebDriver getDriver() {
		
		return WebDriverManager.getDriver();
	}

	
	public abstract String getExcelPath();

	protected abstract void executeTest() throws Exception;


	@DataProvider	//(parallel = true)
	public Object[][] excelDataProvider(ITestContext context)
	{
		logger.info("Inside excelDataProvider for " + getClass().getName());
		ExcelDataReader dataProvider = new ExcelDataReader(getExcelPath());
		ExcelData[] excelDataArray = dataProvider.readExcelData(context);
		logger.info("Count After readExcel = " + excelDataArray.length);
		excelDataArray = filterSkippedData(excelDataArray);
		logger.info("Count After filterSkippedData = " + excelDataArray.length);
		excelDataArray = filterSuiteData(excelDataArray, context.getSuite().getXmlSuite().getName());
		logger().info("Count After filterSuiteData = " + excelDataArray.length);
		excelDataArray = filterSpecifiedData(excelDataArray);
		logger.info("Count After filterSpecifiedData = " + excelDataArray.length);
		if (excelDataArray.length == 0)
		{
			throw new RuntimeException("Could not find any data in the excel");
		}
		Object[][] data = new Object[excelDataArray.length][1];
		for (int index = 0; index < excelDataArray.length; index++)
		{
			data[index][0] = excelDataArray[index];
		}
		return data;
	}
	
	public String getTestName()
	{
		return testName;
		
	}
	
	private ExcelData[] filterSuiteData(ExcelData[] excelDataArray, String suiteName)
	{
		//boolean ignoreSuite = Boolean.valueOf(System.getProperty("ignoreSuite", "false"));
		List<ExcelData> filteredList = new ArrayList<ExcelData>();
		for (ExcelData data : excelDataArray)
		{
			if (data.getSuites().contains(suiteName))
			{
				filteredList.add(data);
			}
		}
		return filteredList.toArray(new ExcelData[] { });
	}
	private ExcelData[] filterSkippedData(ExcelData[] excelDataArray)
	{
		List<ExcelData> filteredList = new ArrayList<ExcelData>();
		for (ExcelData data : excelDataArray)
		{
			if (!data.skip())
			{
				filteredList.add(data);
			}
		}
		return filteredList.toArray(new ExcelData[] { });
	}

	private ExcelData[] filterSpecifiedData(ExcelData[] excelDataArray)
	{
		String testsSpecified = System.getProperty("run.specified.tests", "");
		if (!testsSpecified.isEmpty())
		{
			List<ExcelData> filteredList = new ArrayList<ExcelData>();
			Map<String, List<String>> specifiedTests = readSpecifiedTests(testsSpecified);
			if (specifiedTests != null && specifiedTests.get(getClass().getName()) != null)
			{
				List<String> specifiedList = specifiedTests.get(getClass().getName());
				for (String specifiedTest : specifiedList)
				{
					for (ExcelData data : excelDataArray)
					{
						if (specifiedTest.equals(data.getId()))
						{
							filteredList.add(data);
						}
					}
				}
			}
			return filteredList.toArray(new ExcelData[] { });
		}
		else
		{
			return excelDataArray;
		}
	}
	private static Map<String, List<String>> readSpecifiedTests(String testsSpecified)
	{
		Map<String, List<String>> specifiedTests = new HashMap<String, List<String>>();

		String singleClass = System.getProperty("run.single.class", "");
		if (!singleClass.isEmpty())
		{
			String[] scenarios = testsSpecified.split(Pattern.quote(","));
			List<String> scenarioList = new ArrayList<String>();
			for (String scenario : scenarios)
			{
				if (!scenario.trim().isEmpty())
				{
					scenarioList.add(scenario.trim());
				}
			}
			specifiedTests.put(singleClass, scenarioList);
			return specifiedTests;
		}

		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(testsSpecified));
			String line = null;
			List<String> currentList = null;
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("-"))
				{
					currentList = new ArrayList<String>();
					String clazz = line.substring(1).trim();
					specifiedTests.put(clazz, currentList);
				}
				else
				{
					currentList.add(line.trim());
				}
			}
		}
		catch (Exception e)
		{
			specifiedTests = null;
			/*logger().info("Could not read file " + testsSpecified);
			logger().warn(e.getMessage(), e);*/
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (Exception e)
			{
				// ignore..
			}
		}
		return specifiedTests;
	}

	public String getParameter(String key)
	{
		return data.get().get(key);
	}

	protected PageDataProvider getPageDataProvider(Class<?> clazz)
	{
		File f=new File("data"+File.separator+"PageData.xml");
		String pagedataXMLFile =f.getAbsolutePath();;
		Map<String, Map<String, String>> scnarioData = getScenarioData();
		XMLPageDatProvider datProvider = new XMLPageDatProvider(pagedataXMLFile, scnarioData, clazz);
		pageDataProvides.set(datProvider);
		return datProvider;
	}
	private Map<String, Map<String, String>> getScenarioData()
	{
		Map<String, Map<String, String>> scenarioData = new HashMap<String, Map<String, String>>();
		Map<String, String> constMap = new HashMap<String, String>();
		Map<String, String> dataMap = data.get();
		for (Entry<String, String> entry : dataMap.entrySet())
		{
			constMap.put(entry.getKey(), entry.getValue());
		}
		scenarioData.put(DEFAULT, constMap);
		for (Map<String, String> scenario : scenarioData.values())
		{
			scenario.putAll(constMap);
			scenarioData.put(DEFAULT, scenario);
		}
		return scenarioData;
	}

	public SoftAssert getSoftAssert()
	{
		return softAssert.get();
	}

	public List<AuditAction> getAlogs()
	{
		return Alogs.get();

	}

	@Test(dataProvider = "excelDataProvider")
	public void seleniumTest(ExcelData testData,ITestContext context) throws Exception
	{
		try{
			SoftAssert sofAssert = new SoftAssert();
			softAssert.set(sofAssert);
			Alogs.set(new ArrayList<AuditAction>());
			this.data.set(testData.getData());
			this.excelData.set(testData);
			logger().info("******************** Running Scenario " + testData.toString());
			executeTest();
			softAssert.get().assertAll();
		}
		catch (Exception e)
		{
			findingExceptionClassName();
			logger.warn(e.getMessage(), e);
			//logger().(e.getMessage(), e);
			//logger.error("invoked while performing the action" +failureMethod+ " error class name: "+failureClass);
			takeFailureScreenshot(testData);
			throw e;
		}
		finally
		{
			File srcLogFile = new File("logs", logFiles.get());
			if(srcLogFile.exists()){
				copySafely(srcLogFile, "logs", testData.getId(), "log");
			}
		}
	}
	private void findingExceptionClassName() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i=1; i<stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if(ste.getFileName()!=null && !ste.getFileName().equals("AbstractTestCase.java")){
				logger.info("the className: "+ste.getFileName());
				logger.info("the MethodName: "+ste.getMethodName());
				failureClass=ste.getFileName();
				failureMethod=ste.getMethodName();
				return;
			}
		}
	}
	private void takeFailureScreenshot(ExcelData data) throws Exception
	{
		if(driver.get()!=null)
		{
			byte[] screenShot = ((ClaraWebDriver) driver.get()).getScreenshotAs(OutputType.BYTES);
			copySafely(new ByteArrayInputStream(screenShot), "screenshots", data.getId(), "jpg");
		}
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod(ITestResult result)
	{
		result.setAttribute("issueId", excelData.get().getData().get("issueId"));
		try
		{
			/*driver().close();
			driver().quit();*/
		}
		catch (Exception e1) {
		}
	}

	private void copySafely(InputStream ins, String topDir, String id, String extn)
			throws Exception
	{
		File dir = new File(topDir + "/" + getClass().getName().replaceAll(Pattern.quote("."), "/")
				+ "/" + id);
		int count = 1;
		String fileName = id.trim() + "_" + getDatePart() + "_" + (count++) + "." + extn;
		File destFile = new File(dir, fileName);
		while (destFile.exists())
		{
			fileName = id.trim() + "_" + getDatePart() + "_"  + (count++) + "." + extn;
			destFile = new File(dir, fileName);
		}
		//logger.info("The dest file path is: " + destFile.getAbsolutePath());
		//logger.info("The dest file name is: " + destFile.getName());
		FileUtils.copyInputStreamToFile(ins, destFile);
	}
	private void copySafely(File srcFile, String topDir, String id, String extn) throws Exception
	{
		File dir = new File(topDir + File.separator + getClass().getSimpleName() //.replaceAll(Pattern.quote("."), "/")
				+ "/" + id);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		int count = 0;
		String fileName = id.trim() + "_" + getDatePart() + "_" + (count++) + "." + extn;
		File destFile = new File(dir, fileName);
		destFile.createNewFile();
		while (destFile.exists())
		{
			fileName = id.trim() + "_" + getDatePart() + "_" + (count++) + "." + extn;
			destFile = new File(dir, fileName);
		}
		/*logger.info("The src file path is: " + srcFile.getAbsolutePath());
		logger.info("The dest file path is: " + destFile.getAbsolutePath());
		logger.info("The dest file name is: " + destFile.getName());*/
		FileUtils.copyFile(srcFile, destFile);
	}
	private String getDatePart()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(new Date());
	}
	public void waitTillElementVisible(int timeout, By by)
	{
		try
		{
			WebElement value = new WebDriverWait(driver.get(), timeout).until(ExpectedConditions.elementToBeClickable(By.id("")));
			if (value != null)
			{
				logger().info("Element " + by + " is visible");
			}
			else
			{
				logger().info("None of Element(s) " + by
						+ " is visible even after waiting for " + timeout + " seconds");
				throw new RuntimeException("None of Element(s) " + by
						+ " is visible even after waiting for " + timeout + " seconds");
			}
		}
		catch (Exception e)
		{
			findingExceptionClassName();
			/*logger().warn("None of the Element(s) " + by
					+ " are visible even after waiting for " + timeout + " seconds");*/
			throw new RuntimeException("None of the Element(s) " + by
					+ " are visible even after waiting for " + timeout + " seconds : in :"+failureClass+" ", e);
			//Could not click element."+by+": in :"+failureClass+":", e
		}
	}
	public void waitTillAllCallsDoneUsingJS() throws Exception
	{
		try
		{
			
			JavascriptExecutor jse = (JavascriptExecutor) ((ClaraWebDriver) driver()).getNativeDriver();
			NgWebDriver ngWebDriver = new NgWebDriver((JavascriptExecutor) driver);
			ngWebDriver.waitForAngularRequestsToFinish();

			String countAjaxCalls = "return (function(){"
					+ " if(typeof countAjaxCalls === 'undefined'){" + "  return 0;" + " }"
					+ " else{" + "  return countAjaxCalls;" + " }" + "})()";

			boolean allCallsDone = false;
			int passDownCounter = 2;
			long startTimeMills = System.currentTimeMillis();
			int sleepDuration=500;

			long globalWaitTimeMills = globalWaitTime * 1000;
			int i = 0;
			while (!allCallsDone
					&& (System.currentTimeMillis() - startTimeMills) < globalWaitTimeMills)
			{
				logger().info(i++ + " )Waiting for all calls to be completed.");
				Thread.sleep(sleepDuration);
				long ajaxCallsCount = (Long) jse.executeScript(countAjaxCalls);
				if (ajaxCallsCount == 0)
				{
					passDownCounter--;
					if (passDownCounter <= 0)
					{
						allCallsDone = true;
					}
				}
			}
			logger().info("Outside While loop of allCalls Done.");
		}
		catch (Exception e)
		{
			//logger.warn("Wait till all call done thrown java script error", e);
		}
	}
	public void selectText(String text, WebElement element)
	{
		try
		{
			checkElementExists(element);
			logger().info("selecting text '" + text + "' in '" + element + "'");
			new Select(element).selectByVisibleText(text);
		}
		catch (Exception e)
		{
			logger().warn(e.getMessage(), e);
			throw new RuntimeException("Could not select Text.", e);
		}

	}
	private void checkElementExists(WebElement element)
	{
		try
		{
			new WebDriverWait(driver.get(), globalWaitTime).until(ExpectedConditions.visibilityOf(element));
		}
		catch (Exception e)
		{
			findingExceptionClassName();
			logger().info("Could not find element " + element
					+ " even after waiting. Subsequent statements may fail.");
			throw new TimeoutException("Could not find element " + element
					+ " even after waiting. Subsequent statements may fail.");
		}
	}
	public boolean isElementPresent(By locator) {
		try {
			driver().findElement(locator);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

}