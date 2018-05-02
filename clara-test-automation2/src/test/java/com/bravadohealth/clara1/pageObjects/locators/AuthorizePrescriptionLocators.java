package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface AuthorizePrescriptionLocators {
    By goBackLinkLocator = By.xpath("//a[contains(text(),'Go back')]");
    By signPrescriptionsButtonLocator = By.xpath("//button[contains(text(),'Sign prescriptions…')]");

    By prescriberNameLocator = By.xpath("//span[contains(@class, 'prescriber-name')]");
    By prescriberAddressLocator = By.xpath("//span[contains(@class, 'prescriber-address')]");
    By prescriberDEALocator = By.xpath("//span[contains(@class, 'prescriber-DEA-number')]");
    By patientNameLocator = By.xpath("//span[contains(@class, 'patient-name')]");
    By dateIssuedLocator = By.xpath("//span[contains(@class, 'date-issued')]");
    
    String controlledSubstancesLocatorPrefix = "//li[contains(@class, 'rx-auth-ready')]";
    String prescriptionDetailsRow1LocatorString = "//div[contains(@class, 'column lg-9')]";
    String prescriptionDetailsRow2LocatorString = "//div[contains(@class, 'column lg-3')]";
    By readyToSignIconLocator = By.cssSelector("div.confirm-auth-ready > icon");
}
