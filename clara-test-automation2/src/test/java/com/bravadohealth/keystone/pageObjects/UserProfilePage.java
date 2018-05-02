package com.bravadohealth.keystone.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.EditUserFields;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class UserProfilePage extends AbstractPage{
	
	By FirstNameInputField=By.id("firstNameInput");
	By MiddleNameInputField=By.cssSelector("input[class^='middle-name']");
	By LastNameInputField=By.id("lastNameInput");
	By DepartmentInputField=By.id("departmentInputId");
	By OfcLocationInputField=By.id("locationInputId");
	By saveButtonLocator=By.xpath("//button[contains(text(),'Save')]");
	By ChangeMailAddress=By.id("startChangeUsernameFlow");
	By newMailAddress=By.id("emailInputId");
	By cancelChangeMail=By.xpath("//button[contains(text(),'Cancel')]");
	By resendActivationCode=By.id("emailResendCode");
	By continueChangeMail=By.id("emailContinue");

	public UserProfilePage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public UserProfilePage editUserField() throws Exception {
		EditUserFields editUserFields=dataProvider.getPageData("editUserFields");
		Thread.sleep(2000);
		if(editUserFields.isEmailIdChange()!=null && editUserFields.isEmailIdChange()){
			String newEmail=editUserFields.getNewMailAddress();
			webOp.driver().findElement(ChangeMailAddress).click();
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(newMailAddress).clear();
			webOp.driver().findElement(newMailAddress).sendKeys(newEmail);
			if(!newEmail.contains("bravadohealth")){
				Assert.assertTrue(newEmail.contains("bravadohealth"));
				return this;
			}
			if(editUserFields.isEmailIdChangeCancel()!=null && editUserFields.isEmailIdChangeCancel()){
				webOp.driver().findElement(cancelChangeMail).click();
			}
		}
		webOp.driver().findElement(FirstNameInputField).clear();
		webOp.driver().findElement(FirstNameInputField).sendKeys(editUserFields.getFirstName());
		//webOp.driver().findElement(MiddleNameInputField).sendKeys(editUserFields.getMiddleName());
		webOp.driver().findElement(LastNameInputField).clear();
		webOp.driver().findElement(LastNameInputField).sendKeys(editUserFields.getLastname());
		webOp.driver().findElement(DepartmentInputField).click();
		new Select(webOp.driver().findElement(DepartmentInputField)).selectByValue(editUserFields.getDepartment());
		new Select(webOp.driver().findElement(OfcLocationInputField)).selectByValue(editUserFields.getOfficeLocation());
		webOp.driver().findElement(saveButtonLocator).click();
		return this;
	}

	public UserProfilePage invalidEditUserField() throws Exception {
		EditUserFields editUserFields=dataProvider.getPageData("editUserFields");
		Thread.sleep(2000);
		if(editUserFields.isEmailIdChange()!=null && editUserFields.isEmailIdChange()){
			String newEmail=editUserFields.getNewMailAddress();
			webOp.driver().findElement(ChangeMailAddress).click();
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(newMailAddress).clear();
			webOp.driver().findElement(newMailAddress).sendKeys(newEmail);
			webOp.driver().findElement(continueChangeMail).click();
			if(!newEmail.contains("bravadohealth")){
				Assert.assertFalse(newEmail.contains("bravadohealth"));
				return this;
			}
			String activationCode=editUserFields.getActivationCode();
			if(activationCode!=null && activationCode.length()<6){
				Assert.assertFalse(activationCode.length()>6);
				return this;
			}
			if(editUserFields.isEmailIdChangeCancel()!=null && editUserFields.isEmailIdChangeCancel()){
				webOp.driver().findElement(cancelChangeMail).click();
			}
			if(editUserFields.isResendActivationCode()!=null && editUserFields.isResendActivationCode()){
				webOp.driver().findElement(resendActivationCode).click();
			}
		}

		webOp.driver().findElement(FirstNameInputField).clear();
		webOp.driver().findElement(FirstNameInputField).sendKeys(editUserFields.getFirstName());
		//webOp.driver().findElement(MiddleNameInputField).sendKeys(editUserFields.getMiddleName());
		webOp.driver().findElement(LastNameInputField).clear();
		webOp.driver().findElement(LastNameInputField).sendKeys(editUserFields.getLastname());
		webOp.driver().findElement(DepartmentInputField).click();
		new Select(webOp.driver().findElement(DepartmentInputField)).selectByValue(editUserFields.getDepartment());
		new Select(webOp.driver().findElement(OfcLocationInputField)).selectByValue(editUserFields.getOfficeLocation());
		webOp.driver().findElement(saveButtonLocator).click();
		return this;
	}

}
