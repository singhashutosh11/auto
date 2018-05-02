package com.bravadohealth.automation.clara.security.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PrescriptionsPage {

	By accountSelector = By.id("gn-link-account");
	By signoutSelector = By.cssSelector("clara-signout>span");
	By patientSelector = By.cssSelector("li[class*='gn-item-patients']");
	By loadingMask = By.id("loading-screen");
	
	WebDriver webDriver;
	public PrescriptionsPage(WebDriver webDriver)
	{
		this.webDriver = webDriver;
	}
	
	WebElement findElement( By locator)
	{
		return webDriver.findElement(locator);
	}
	
	public boolean isPageLoaded()
	{
		new WebDriverWait(webDriver, 5, 100).until(ExpectedConditions.presenceOfElementLocated(accountSelector));
		return true;
	}
	
	public void logout()
	{
		findElement(accountSelector).click();
		findElement(signoutSelector).click();
		new WebDriverWait(webDriver, 5, 100).until(ExpectedConditions.invisibilityOf(findElement(loadingMask)));
	}
	
	public void searchPatients()
	{
		findElement(patientSelector).click();
		new WebDriverWait(webDriver, 5, 100).until(ExpectedConditions.invisibilityOf(findElement(loadingMask)));
	}

}
