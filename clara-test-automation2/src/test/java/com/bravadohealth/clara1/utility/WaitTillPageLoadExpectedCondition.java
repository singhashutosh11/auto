package com.bravadohealth.clara1.utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import trial.keyStone.automation.ClaraWebDriver;

/**
 * Returns 'true' if the value of the 'window.document.readyState' via
 * JavaScript is 'complete'
 */
public class WaitTillPageLoadExpectedCondition implements ExpectedCondition<Boolean> {

	public Boolean apply(WebDriver driver) {
		JavascriptExecutor localJse = (JavascriptExecutor) ((ClaraWebDriver) driver).getNativeDriver();
		System.out.println("Current Window State       : "
                + String.valueOf(localJse.executeScript("return document.readyState")));
            return String
                .valueOf(localJse.executeScript("return document.readyState"))
                .equals("complete");
	}
 
}	
