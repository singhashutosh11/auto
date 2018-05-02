package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface IdVerificationPageLocators {
	
	By designationLocator=By.xpath("//select[option[1][text()='designation']]");
	//By moileNumberLocator=By.cssSelector("input[placeholder='mobile number']");
	By stateLocator=By.xpath("//select[.//option[1][text()='state']]");
	By stateLicenseNumberLocator=By.cssSelector("input[placeholder='state license number']");
	By NPINumberLocator=By.cssSelector("input[placeholder='NPI number']");
	//By DEANumberLocator=By.cssSelector("input[placeholder='DEA number']");
	By NDEANNumberLocator=By.cssSelector("//div[h3[contains(text(),'NADEAN Number')]]/input[1]");
	By validateLocator=By.xpath("//button[a[text()='Validate']]");
	
	By displayedDesignation=By.xpath("//div[h3[text()='Professional Designation']]/span");
//	By displayedMobileNumber=By.xpath("//div[h3[text()='MOBILE NUMBER']]/span");
	By displayedStateLicense=By.xpath("//div[h3[text()='Medical licence #']]/span");
	By displayedNPINumber=By.xpath("//div[h3[text()='NPI NUMBER']]/span");
//	By displayedDEANumber=By.xpath("//div[h3[text()='DEA NUMBER']]/span");
//	By displayedNDEANNumber=By.xpath("//div[h3[text()='NADEAN NUMBER']]/span");
	
	By continueButtonLocator=By.xpath("//button[a[contains(text(),'Continue')]]");
	By licenseError=By.id("licence-verification-error");
	
}
