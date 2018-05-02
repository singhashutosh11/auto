package com.bravadohealth.automation.clara.security.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PatientsPage {

	By patientsSelector = By.id("mg-patients-content");
	
	WebDriver webDriver;
	public PatientsPage(WebDriver webDriver)
	{
		this.webDriver = webDriver;
	}
	
	WebElement findElement( By locator)
	{
		return webDriver.findElement(locator);
	}
	
	public boolean isPageLoaded()
	{ 
		new WebDriverWait(webDriver, 5, 100).until(ExpectedConditions.presenceOfElementLocated(patientsSelector));
		return true;
	}
	
	public void managePatients(String managePatientsUrl)
	{
		webDriver.get(managePatientsUrl);
		new WebDriverWait(webDriver, 15, 500).until(ExpectedConditions.presenceOfElementLocated(patientsSelector));
	}
	

}
