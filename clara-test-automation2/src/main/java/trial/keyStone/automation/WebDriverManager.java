package trial.keyStone.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverManager {

	
	private WebDriverManager()
	{
		
	}
	
	@SuppressWarnings("deprecation")
	public static WebDriver getDriver() {

		if (isHeadlessMode())
		{
			return buildHeadlessChromeDriver();
		}
		WebDriver driver = null;
		if (!System.getProperty("webdriver.ie.driver", "").isEmpty()) {
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability("ie.ensureCleanSession", true);
			driver = new InternetExplorerDriver(capabilities);
		} else if (!System.getProperty("webdriver.chrome.driver", "").isEmpty()) {
			
			

			driver = new ChromeDriver();
		} else
			driver = new FirefoxDriver();
		return new ClaraWebDriver(driver);

	}
	
	
	public static boolean isHeadlessMode()
	{
		if (!System.getProperty("WEB_DRIVER_MODE", "").isEmpty())
		{
			return  "HEADLESS".equalsIgnoreCase(System.getProperty("WEB_DRIVER_MODE"));
		}
		
		if (System.getenv("WEB_DRIVER_MODE") !=null && !System.getenv("WEB_DRIVER_MODE").isEmpty())
		{
			return "HEADLESS".equalsIgnoreCase(System.getenv("WEB_DRIVER_MODE")) ;
		}
		
		return false;
		
	}
	
	public static WebDriver buildHeadlessChromeDriver()
	{
		String chromeExecutablePath = "";
		
		if (System.getenv("WEB_BROWSER_HOME") !=null && !System.getenv("WEB_BROWSER_HOME").isEmpty())
		{
			chromeExecutablePath = System.getenv("WEB_BROWSER_HOME");
		}
		else
		{
			chromeExecutablePath = System.getProperty("WEB_BROWSER_HOME", "");
		}
		
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setBinary(chromeExecutablePath);
		chromeOptions.addArguments("--window-size=1200x600");
		chromeOptions.setHeadless(true);
		return new ClaraWebDriver(new ChromeDriver(chromeOptions));
	}

}
