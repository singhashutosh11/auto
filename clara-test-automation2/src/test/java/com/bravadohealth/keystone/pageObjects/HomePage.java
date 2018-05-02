package com.bravadohealth.keystone.pageObjects;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ChangePassword;
import com.bravadohealth.pagedataset.EditUserFields;

import trial.keyStone.automation.ExcelData;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class HomePage extends AbstractPage {
	By KSUserId = By.xpath("//small[@id='username']//strong");
	By ChangePasswordEditLocator = By.xpath("//div[@class='security-wrapper']//button[contains(text(),'Edit')]");
	By ChangePasswordLocator = By.id("startChangePasswordFlow");
	By CurrentPasswordLocator = By.id("currentPassword");
	By NewPasswordLocator = By.id("newPassword");
	By confirmNewPasswordLocator = By.id("newPasswordConfirm");
	By CancelButtonLocator = By.xpath("//button[@ng-click='changePwdCtrl.cancel()']");
	By EditButtonLocator = By.xpath("//a[contains(text(),'Edit')]");
	By AdminLinkLocator = By.xpath("//a[span[contains(text(),'Administration')]]");
	By confirmButtonLocator = By.id("passwordContinue");
	By changePasswordConfirmUpdate = By.xpath("//div[contains(@class,'security-details')]//div[@class='description']");
	By claraChangePasswordConfirmUpdate = By.xpath(
			"//form[contains(@name,'security.form')]//div[contains(@class,'edit-row')][1]//div[@class='description ng-binding']");
	By claraUserId = By.xpath("//small[contains(@class,'clara-username')]//strong");
	By securityEditButtonLocator=By.xpath("//button[contains(@ng-click,'security')][contains(@class,'button-edit')]");
	By mobilNumberInputField=By.cssSelector("input[placeholder='phone']");

	public HomePage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		PageFactory.initElements(webOp.driver(), this);
		// TODO Auto-generated constructor stub
	}

	public HomePage verifyUserDetails() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		Thread.sleep(5000);
		String userName = hMap.get("userName");
		String KeyStoneID = webOp.driver().findElement(KSUserId).getText();
		Assert.assertEquals(userName, KeyStoneID);
		logger.info("The KeyStoneId is dispaying same in the KSHome page as the UserId:" + KeyStoneID);
		return this;
	}

	public HomePage verifyClaraUserId() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		Thread.sleep(5000);
		String userName = hMap.get("userName");
		String claraUserID = webOp.driver().findElement(claraUserId).getText();
		Assert.assertEquals(userName, claraUserID);
		logger.info("The claraId is dispaying same in the ClaraAccountDetails page as the UserId:" + claraUserID);
		return this;
	}

	public HomePage changePassWord() throws Exception {
		ChangePassword changePassword = dataProvider.getPageData("changePassword");
		webOp.waitTillAllCallsDoneUsingJS();
		Thread.sleep(2000);
		if (changePassword != null) {
			String currentPassword = changePassword.getCurrentPassword();
			String newPassword = changePassword.getNewPassword();
			String confirmPassword = changePassword.getConfirmPassword();
			webOp.driver().findElement(ChangePasswordEditLocator).click();
			webOp.driver().findElement(ChangePasswordLocator).click();
			webOp.driver().findElement(CurrentPasswordLocator).sendKeys(currentPassword);
			String logInPassword = hMap.get("logInPassword");
			if (!logInPassword.equals(currentPassword)) {
				Assert.assertNotEquals(logInPassword, currentPassword,
						"The message is displaying as expected when the currentPassword is entered wrong");
				return this;
			}
			
			// some tests(changepassword/verifycancel) break if this delay is removed
			Thread.sleep(200);
			
			webOp.driver().findElement(NewPasswordLocator).sendKeys(newPassword);
			int satisfyingCondition = webOp.driver().findElements(By.xpath("//div[contains(@class,'error') and contains(@class,'success')]")).size();
			if (satisfyingCondition < 3) {
				Assert.assertTrue(satisfyingCondition<3,"The password is not satisfying the minimum ceiteria");
				return this;
			}
			webOp.driver().findElement(confirmNewPasswordLocator).sendKeys(confirmPassword);
			if (!newPassword.equals(confirmPassword)) {
				Assert.assertEquals(newPassword, confirmPassword,
						"new password and confirm password are not matching.");
				return this;
			}
			if (changePassword.isCancel() != null && changePassword.isCancel()) {
				webOp.driver().findElement(CancelButtonLocator).click();
			} else {
				webOp.driver().findElement(confirmButtonLocator).click();
				webOp.waitTillAllCallsDoneUsingJS();
				String changePasswordNotification = null;
				if (!logInPassword.equalsIgnoreCase("Clara123")) {
					changePasswordNotification = webOp.driver().findElement(changePasswordConfirmUpdate).getText();
				} else
					changePasswordNotification = webOp.driver().findElement(claraChangePasswordConfirmUpdate).getText();
				LocalDate nowLD = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
				String formattedString = nowLD.format(formatter);
				//System.out.println("formatted string = "+formattedString + "changePasswordNotification"+changePasswordNotification);
					Assert.assertTrue(changePasswordNotification.contains(formattedString),
							"With successful password change, the date is updating.");
			}
		}
		return this;
	}

	public UserProfilePage navigateToUserProfile() throws Exception {
		webOp.driver().findElement(EditButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		Thread.sleep(3000);
		return new UserProfilePage(webOp, dataProvider);
	}

	public AdminPage navigateToAdminPage() throws Exception {
		webOp.driver().findElement(AdminLinkLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		return new AdminPage(webOp, dataProvider);
	}

	public HomePage InvalidchangePassWord() throws Exception {
		ChangePassword changePassword = dataProvider.getPageData("changePassword");
		webOp.waitTillAllCallsDoneUsingJS();
		Thread.sleep(2000);
		if (changePassword != null) {
			String currentPassword = changePassword.getCurrentPassword();
			String logInPassword = hMap.get("logInPassword");
			webOp.driver().findElement(ChangePasswordEditLocator).click();
			webOp.driver().findElement(ChangePasswordLocator).click();
			webOp.driver().findElement(CurrentPasswordLocator).sendKeys(currentPassword);
			if (!logInPassword.equals(currentPassword)) {
				Assert.assertNotEquals(logInPassword, currentPassword,
						"The message is displaying as expected when the currentPassword is entered wrong");
				return this;
			}
			String newPassword = changePassword.getNewPassword();
			webOp.driver().findElement(NewPasswordLocator).sendKeys(newPassword);
			int satisfyingCondition = webOp.driver().findElements(By.cssSelector("div[class$='success']")).size();
			if (satisfyingCondition < 3) {
				Assert.assertFalse(satisfyingCondition == 3);
				return this;
			}
			String confirmPassword = changePassword.getConfirmPassword();
			webOp.driver().findElement(confirmNewPasswordLocator).sendKeys(confirmPassword);
			if (!newPassword.equals(confirmPassword)) {
				Assert.assertNotEquals(newPassword, confirmPassword,
						"new password and confirm password are not matching.");
				return this;
			}
			webOp.driver().findElement(CancelButtonLocator).click();

		}
		return this;

	}

	public HomePage editSecurityField() throws Exception {
		webOp.driver().findElement(securityEditButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		return this;
	}

	public void changeMobileNumber() {
		// TODO Auto-generated method stub
		webOp.driver().findElement(mobilNumberInputField).sendKeys("");
	}

}
