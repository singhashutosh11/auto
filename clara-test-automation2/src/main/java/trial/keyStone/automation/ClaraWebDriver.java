package trial.keyStone.automation;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

public class ClaraWebDriver implements WebDriver, HasInputDevices {

	private WebDriver nativeDriver; 
	
	public ClaraWebDriver (WebDriver webDriver)
	{
		nativeDriver = webDriver;
	}
	
	public WebDriver getNativeDriver()
	{
		return nativeDriver;
	}
	
	public void get(String url) {
		
		nativeDriver.get(url);
	}

	public String getCurrentUrl() {
		
		return nativeDriver.getCurrentUrl();
	}

	public String getTitle() {
		
		return nativeDriver.getTitle();
	}

	public List<WebElement> findElements(By by) {
		
		return nativeDriver.findElements(by);
	}

	public WebElement findElement(By by) {
		
		return findElementWithRetry(by, 3);
	}
	
	public WebElement findElementWithRetry(By by, int retryCount)
	{
		try {
			
			return nativeDriver.findElement(by);
		} catch (NoSuchElementException e) {
			
			if (retryCount < 0)
			{
				throw e;
			}
			try {
				
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				
			}
			return findElementWithRetry(by, --retryCount);
		}
	}
		

	public String getPageSource() {
		
		return nativeDriver.getPageSource();
	}

	public void close() {
		nativeDriver.close();
		
	}

	public void quit() {
		nativeDriver.quit();
		
	}

	public Set<String> getWindowHandles() {
		return nativeDriver.getWindowHandles();
	}

	public String getWindowHandle() {
		return nativeDriver.getWindowHandle();
	}

	public TargetLocator switchTo() {
		return nativeDriver.switchTo();
	}

	public Navigation navigate() {
		return nativeDriver.navigate();
	}

	public Options manage() {
		
		return nativeDriver.manage();
	}

	public byte[] getScreenshotAs(OutputType<byte[]> bytes) {
		
		return ((TakesScreenshot) nativeDriver).getScreenshotAs(OutputType.BYTES);
	}

	public void executeScript(String script) {
		
		((JavascriptExecutor) nativeDriver).executeScript(script);
	}

	public Keyboard getKeyboard() {
		
		return ((HasInputDevices) nativeDriver).getKeyboard();
	}

	public Mouse getMouse() {

		return ((HasInputDevices) nativeDriver).getMouse();
	}

}
