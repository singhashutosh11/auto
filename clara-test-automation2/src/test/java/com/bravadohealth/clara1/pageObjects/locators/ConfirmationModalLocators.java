package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface ConfirmationModalLocators {
    By navigateAwayAlertTitleLocator = By.xpath("//div[@class='modal-content'][div/h1[text()='Discard data']]");
    By okButtonLocator = By.xpath("//button[@ng-click='modalCtrl.onOkClick();']");
    By cancelButtonLocator = By.xpath("//button[@ng-click='modalCtrl.onCancelClick();']");
    By modalLocator = By.cssSelector("div.modal");
    By continueRxButtonLocator = By.xpath("//div[@class='modal-content']//button[contains(@class, 'secondary-action')]");
    By removeRxButtonLocator = By.xpath("//div[@class='modal-content']//button[contains(@class, 'primary-action')]");
	By signoutLink = By.xpath("//button[text()='Sign Out']");
	By continueLogin = By.xpath("//button[text()='Continue']");
    By logoutModalLocator = By.xpath("//div[@class='modal-content']//button[text()='Sign out']");


}
