package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;

import com.bravadohealth.pagedataset.AuditLogFilter;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.EditUserFields;

import com.bravadohealth.pagedataset.Facility;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.services.FacilityService;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.EditUserFields;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class AccountPage extends AbstractPage implements LoaderLocators{

	public AccountPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	By ClaraUserId = By.xpath("//small[contains(@class,'clara-username')]//strong");
	By cancelProfileChangesButtonLocator = By
			.xpath("//button[contains(@ng-click,'profile')][contains(@class,'button-cancel')]");
	By EditButtonLocator = By.xpath("//a[contains(text(),'Edit')]");
	By saveProfileChangesButtonLocator = By
			.xpath("//button[contains(@ng-click,'profile')][contains(@class,'button-done')]");

	By AccountEditButton = By.xpath("//button[contains(@ng-click,'profile')][contains(@class,'button-edit')]");
	By FirstNameInputField = By.name("firstName");
	By MiddleNameInputField = By.cssSelector("input[placeholder='middle name (optional)']");
	By LastNameInputField = By.name("lastName");
	By BirthDayLocator = By.cssSelector("input[placeholder='birthday']");

	By ChangeMailAddressLink = By.id("startChangeUsernameFlow");
	By newMailAddress = By.id("emailInputId");
	By cancelChangeMailButtonLocator = By.xpath("//button[@ng-click='changeEmailCtrl.cancelChangeEmail()']");
	By continueChangeMail = By.id("emailContinue");
	By resendActivationCode = By.id("emailResendCode");
	By OTPinputLocator = By.id("codeInputId");
	By InvalidOTP = By.xpath("//div[contains(@class,'email-input-wrapper')]/span");

	By FacilityLinkLocator=By.xpath("//a[text()='Facility']");
	By TeamLinkLocator=By.xpath("//a[text()='Team']");
	By GroupLinkLocator=By.xpath("//a[text()='Groups']");
	By facilityCompleteSectionLocator=By.xpath("//div[contains(@class, 'container main-wrapper')]");
	By AuditTrailLocator=By.xpath("//a[span[text()='Audit Trail']]");
	
	By displayedDesignationLocator=By.xpath("//div[h3[contains(text(),'DESIGNATION')]]/div[contains(@class,'designation ')]");
	By displayedNPILocator=By.xpath("//div[h3[contains(text(),'NPI NUMBER')]]/div[contains(@class,'npi-number')]");
	By displayedStateLicenseLocator=By.xpath("//div[h3[contains(text(),'STATE LICENSE')]]/div[contains(@class,'state-license-number')]");


	public AccountPage verifyUserDetails() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		Thread.sleep(5000);
		String userName = hMap.get("userName");
		String KeyStoneID = webOp.driver().findElement(ClaraUserId).getText();
		Assert.assertEquals(userName, KeyStoneID);
		logger.info("The KeyStoneId is dispaying same in the KSHome page as the UserId:" + KeyStoneID);
		return this;

	}

	public AccountPage navigateToUserProfile() throws Exception {
		webOp.driver().findElement(EditButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		Thread.sleep(3000);
		return new AccountPage(webOp, dataProvider);
	}

	public AccountPage editUserField() throws Exception {
		EditUserFields editUserFields = dataProvider.getPageData("editUserFields");
		Thread.sleep(2000);
		if (editUserFields.isEmailIdChange() != null && editUserFields.isEmailIdChange()) {
			changeEmailAddress(editUserFields);
		}
		webOp.driver().findElement(FirstNameInputField).clear();
		webOp.driver().findElement(FirstNameInputField).sendKeys(editUserFields.getFirstName());
		if(editUserFields.getMiddleName()!=null) {
			webOp.driver().findElement(MiddleNameInputField).sendKeys(editUserFields.getMiddleName());
		}
		webOp.driver().findElement(LastNameInputField).clear();
		webOp.driver().findElement(LastNameInputField).sendKeys(editUserFields.getLastname());
		if (editUserFields.getBirthDate() != null) {
			webOp.driver().findElement(BirthDayLocator).sendKeys(editUserFields.getBirthDate());
		}
		saveOrDiscardChanges();
		return this;
	}

	private void changeEmailAddress(EditUserFields editUserFields) throws Exception {
		String newEmail = editUserFields.getNewMailAddress();
		webOp.driver().findElement(ChangeMailAddressLink).click();
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(newMailAddress).clear();
		webOp.driver().findElement(newMailAddress).sendKeys(newEmail);
		String userName = hMap.get("userName");
		Assert.assertFalse(newEmail.equals(userName), "Same email entered");
		boolean emailFormatCheck = new VerifyClaraAppEmailPage(webOp, dataProvider).validate(userName);
		if (!emailFormatCheck) {
			Assert.assertFalse(emailFormatCheck, "The emailId format is incorrect");
		} else
			webOp.driver().findElement(continueChangeMail).click();
		webOp.waitTillAllCallsDoneUsingJS();

		if (editUserFields.getActivationCode() != null) {
			webOp.driver().findElement(OTPinputLocator).sendKeys(editUserFields.getActivationCode());
			webOp.driver().findElement(continueChangeMail).click();
			webOp.waitTillAllCallsDoneUsingJS();
			String errorMessage = webOp.driver().findElement(InvalidOTP).getText();
			Assert.assertEquals(errorMessage, "Email change token is invalid.",
					"Error message displayed for invalid OTP");
		}
		if (editUserFields.isEmailIdChangeCancel() != null && editUserFields.isEmailIdChangeCancel()) {
			webOp.driver().findElement(cancelChangeMailButtonLocator).click();
		}
	}

	public AccountPage saveOrDiscardChanges() throws Exception {
		EditUserFields editUserFields = dataProvider.getPageData("editUserFields");
		if (editUserFields.isSaveChanges() && editUserFields.isSaveChanges().booleanValue() == true) {
			webOp.driver().findElement(saveProfileChangesButtonLocator).click();
		} else {
			webOp.driver().findElement(cancelProfileChangesButtonLocator).click();
		}
		return this;
	}

	public AccountPage editAccountField() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(AccountEditButton).click();
		return this;
	}

	public FacilityPage moveToFacilityPage() throws Exception {
		webOp.driver().findElement(FacilityLinkLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.visibilityOfElementLocated(facilityCompleteSectionLocator));
		FacilityService.setDateTimeAtSelectFacility();
		Alogs.add(AuditAction.USER_SELECTED_FACILITY);
		FacilityService.initializeFacility(webOp, dataProvider);
		return new FacilityPage(webOp, dataProvider);
	}

	public TeamPage moveToTeamPage() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(TeamLinkLocator).click();
		return new TeamPage(webOp, dataProvider);
	}
	
	public GroupsPage moveToGroupsPage() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(GroupLinkLocator).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		return new GroupsPage(webOp, dataProvider);
	}

	public UsersPage selectUsersTabUnderAccountPage() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(By.xpath("//a[contains(@class,'ng-binding')][contains(text(),'Users')]")).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.urlContains(AppPage.UsersPage.getUrlContains(hMap)));
		return new UsersPage(webOp, dataProvider);
	}
	public AuditLogPage verifyUserDetailsAfterSetUp() throws Exception {
		ClaraUserDetails claraUserDetails = dataProvider.getPageData("claraUserDetails");
		String displayedDesignation=webOp.driver().findElement(displayedDesignationLocator).getText();
		String displayedNPI=webOp.driver().findElement(displayedNPILocator).getText();
		String displayedStateLicense=webOp.driver().findElement(displayedStateLicenseLocator).getText();
		Assert.assertEquals(displayedDesignation, claraUserDetails.getDesignation(),"The designation of the user is displayed correctly.");
		Assert.assertEquals(displayedNPI, claraUserDetails.getNPINumber(),"The NPI number of the user is displayed correctly.");
		Assert.assertEquals(displayedStateLicense, claraUserDetails.getStateLicenseNumber(),"The stateLicense of the user is displayed correctly.");
		return new AuditLogPage(webOp, dataProvider);
	}
}
