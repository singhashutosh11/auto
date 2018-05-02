package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface DemographicLocators {
    By firstNameLocator = By.cssSelector("input[name='firstName']");
    By lastNameLocator = By.cssSelector("input[name='lastName']");
    By middleNameLocator = By.cssSelector("input[name='middleName']");
    By addressLocator = By.cssSelector("input[name='address']");
    By cityLocator = By.cssSelector("input[name='city']");
    By stateLocator = By.cssSelector("select[name='state']");
    By zipCodeLocator = By.cssSelector("input[name='zipCode']");
    By birthdayLocator = By.cssSelector("input[name='birthday']");
    By genderLocator = By.cssSelector("select[name='gender']");
    By weightLocator = By.cssSelector("input[name='weight']");
    By weightMeasurementUnitLocator = By.cssSelector("div.control[ng-click='patDmghcsCtrl.toggleWeightUnit()']");

    By phoneLocator = By.cssSelector("input[name='phone']");
    By emailLocator = By.cssSelector("input[placeholder='email']");
    
    By ageViewLocator = By.cssSelector("span.sub-detail.age");
}
