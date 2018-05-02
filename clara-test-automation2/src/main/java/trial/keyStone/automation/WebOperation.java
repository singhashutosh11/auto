package trial.keyStone.automation;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.testng.asserts.SoftAssert;


public interface WebOperation {

		WebDriver getDriver();
		
		WebDriver driver();
		
		WebDriver nativedriver();
		
		//List<AuditAction> Alogs = new ArrayList<AuditAction>();
		
		HashMap<String,String> gethMap();
		
		List<AuditAction> getAlogs();
		
		Logger logger();

		void waitTillElementVisible(int timeout, By by);
		
		void waitTillAllCallsDoneUsingJS()throws Exception;

		String getParameter(String key);
		
		void selectText(String text, WebElement element);
		
		SoftAssert getSoftAssert();
		
		boolean isElementPresent(By Locator);

}
