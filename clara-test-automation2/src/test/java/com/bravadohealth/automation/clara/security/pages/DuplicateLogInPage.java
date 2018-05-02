package com.bravadohealth.automation.clara.security.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DuplicateLogInPage {

	
	By continueLocator = By.cssSelector("button[class*='primary-action']");
	By cancelLocator = By.cssSelector("button[class*='secondary-action']");
	By loadingMask = By.id("loading-screen");
	
	WebDriver webDriver;
	public DuplicateLogInPage(WebDriver webDriver)
	{
		this.webDriver = webDriver;
	}
	
	WebElement findElement( By locator)
	{
		return webDriver.findElement(locator);
	}
	
	public boolean isDuplicateLogin()
	{
		return "You are already signed in".equals(webDriver.getTitle());
	}
	
	public void continueToLogin()
	{
		findElement(continueLocator).click();
		try {
			new WebDriverWait(webDriver, 5, 100).until(ExpectedConditions.invisibilityOf(findElement(loadingMask)));
		} catch (NoSuchElementException e) {
			//The loading mask could have been destroyed when the new page is loaded. We should be fine here
		}
	}
	
	public void cancel()
	{
		findElement(cancelLocator).click();
	}

}
