package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface CreateFacilityLocators {
	
	By facilityName=By.cssSelector("input[placeholder='facility name']");
	By facilityAddress=By.cssSelector("input[placeholder='address']");
	By cityName=By.cssSelector("input[placeholder='city']");
	By StateLocator=By.xpath("//select[option[1][text()='state']]");
	By zipCode=By.cssSelector("input[placeholder='zip code']");
	By phoneNumber=By.cssSelector("input[placeholder='phone number']");
	By faxNumber=By.cssSelector("input[placeholder='fax number']");
	By facilityNPI=By.cssSelector("input[placeholder='facility NPI number']");
	By facilityDEA=By.cssSelector("input[placeholder='facility DEA number']");
	By validateLocator=By.xpath("//button[a[text()='Validate']]");
	By continueButtonLocator=By.xpath("//button[a[contains(text(),'Continue')]]");

}
