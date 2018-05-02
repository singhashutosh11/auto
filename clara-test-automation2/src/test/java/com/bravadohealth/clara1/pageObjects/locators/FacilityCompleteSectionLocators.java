package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface FacilityCompleteSectionLocators {
	
	By facilityAddressLocator=By.cssSelector("input[name='address']");
	By cityLocator=By.cssSelector("input[name='city']");
	By stateLocator=By.xpath("//select[option[1][text()='state']]");

	By NominatorFirstNameLocator=By.cssSelector("input[name='nominatorFirstName']");
	By NominatorLastNameLocator=By.cssSelector("input[name='nominatorLastName']");
	By NominatorMailAddress=By.cssSelector("input[name='nominatorEmail']");

	By AltNominatorFirstNameLocator=By.cssSelector("input[name='alternateNominatorFirstName']");
	By AltNominatorLastNameLocator=By.cssSelector("input[name='alternateNominatorLastName']");
	By AltNominatorMailAddress=By.cssSelector("input[name='alternateNominatorEmail']");
}
