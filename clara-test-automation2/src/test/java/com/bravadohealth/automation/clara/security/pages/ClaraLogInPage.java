package com.bravadohealth.automation.clara.security.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClaraLogInPage {

	WebDriver webDriver;
	
	public ClaraLogInPage(WebDriver webDriver)
	{
		this.webDriver = webDriver;
	}
	
	By userNameLocator = By.cssSelector("input[placeholder='Clara ID']");
	By passWordLocator = By.cssSelector("input[placeholder='Password']");
	By signInLocator = By.cssSelector("button#sign-in");
	By signInError = By.cssSelector("div[class*='signin-error']");
	
	By loadingMask = By.id("loading-screen");
	
	WebElement findElement( By locator)
	{
		return webDriver.findElement(locator);
	}
	
	public void provideUserName(String userName)
	{
		findElement(userNameLocator).sendKeys(userName);
	}
	
	public void providePassWord(String passWord)
	{
		findElement(passWordLocator).sendKeys(passWord);
	}
	
	public void signIn()
	{
		findElement(signInLocator).click();
		try {
			new WebDriverWait(webDriver, 5, 100).until(ExpectedConditions.invisibilityOf(findElement(loadingMask)));
		} catch (Exception e) {
			return;
		}
	}
	
	public boolean isLoggedIn()
	{
		try {
			
			ExpectedConditions.presenceOfElementLocated(userNameLocator).apply(webDriver);
			return false;
		} catch (NoSuchElementException e) {
			
			return true;
		}
	}
	
	public boolean isPageLoaded()
	{
		new WebDriverWait(webDriver, 5, 100).until(ExpectedConditions.presenceOfElementLocated(userNameLocator));
		return true;
	}
	
	public boolean isLoginError()
	{
		try {
			ExpectedConditions.presenceOfElementLocated(signInError).apply(webDriver);
		}
		catch (NoSuchElementException e) {
		
			return false;
		}
		return true;
		
	}
	

}
