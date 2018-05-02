package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface TwoFAModalLocators {
    By twoFAModalLocator = By.xpath("//div[contains(@class, 'two-FA-modal')]");
    By enterPasswordInputLocator = By.xpath("//input[@placeholder='password']");
    By nextButtonLocator = By.xpath("//button[text()='Next']");
    By enterPasscodeInputLocator = By.xpath("//input[@placeholder='passcode']");
    By submitButtonLocator = By.xpath("//button[text()='Submit']");
}
