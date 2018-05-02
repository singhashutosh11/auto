package com.bravadohealth.clara1.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.LicenseStatus;
import com.bravadohealth.pagedataset.UserDetails;
import com.bravadohealth.pagedataset.UserNLicenseDetails;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class TeamPage extends AbstractPage {

	By confirmRemoveUser=By.xpath("//div[@class='popover-content']//button[text()='Remove user']");
	By CancelRemove=By.xpath("//div[@class='popover-content']//button[text()='Cancel']");
	By totalActiveUser=By.xpath("//button[contains(@class,'active')]/span");

	By pendingUserLocator=By.xpath("//button[contains(text(),'Pending Users')]");
	By unAssignedLicensesLocator=By.xpath("//button[contains(text(),'Unassigned Licenses')]	");
	By popUpForAssignUser=By.xpath("");
	By emailAddressForAssignUser=By.xpath("//div[textarea[@placeholder='Copy/paste email addresses']]");
	By cancelAssignLicense=By.xpath("");
	By confirmAssignLicense=By.xpath("");

	By activerUserList=By.xpath("//tbody[contains(@ng-if,'listOfActiveUsers')]/tr");
	By pendingUserList=By.xpath("");


	public TeamPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}

	public void changeLicenses() {
		LicenseStatus licenseStatus=dataProvider.getPageData("licenseStatus");
		manageActiveLicenses(licenseStatus);
		//managePendingLicenses(licenseStatus);	
		//manageUnassignedLicenses(licenseStatus);
	}

	private void manageUnassignedLicenses(LicenseStatus licenseStatus) {
		webOp.driver().findElement(unAssignedLicensesLocator).click();
		if(licenseStatus.getUnassigned()!=null && licenseStatus.getUnassigned().getUserNLicenseDetails()!=null ) {
			UserNLicenseDetails userNLicense=licenseStatus.getUnassigned().getUserNLicenseDetails();
			int noOfUnassignedUser=userNLicense.getNoOfLicensesToChange();
			for(int i=0;i<noOfUnassignedUser;i++) {
				/*if(licenseStatus.getUnassigned().isAssignLicense()!=null && licenseStatus.getUnassigned().isAssignLicense().equals(true)) {
					webOp.driver().findElement("//dynamic assignlicense locator").click();
					//popup for assign license
					if(webOp.driver().findElement(popUpForAssignUser).isDisplayed()) {
						webOp.driver().findElement(emailAddressForAssignUser).sendKeys(userNLicense.getUserDetails().getEmailId());
					}
					if(userNLicense.isCancel()!=null && userNLicense.isCancel().equals(true)) {
						webOp.driver().findElement(cancelAssignLicense).click();
					}
					else
						webOp.driver().findElement(confirmAssignLicense).click();
				}*/
			}

		}
	}

	private void managePendingLicenses(LicenseStatus licenseStatus) {
		webOp.driver().findElement(pendingUserLocator).click();
		List<WebElement> OldPendingUserList=webOp.driver().findElements(pendingUserList);
		int previousNoOfPendingUser=OldPendingUserList.size();
		if(licenseStatus.getPending()!=null && licenseStatus.getPending().getUserNLicenseDetails()!=null) {
			UserNLicenseDetails userNLicense=licenseStatus.getPending().getUserNLicenseDetails();
			int noOfPendingLicensesToModify=userNLicense.getNoOfLicensesToChange();
			/*for(int i=0;i<noOfPendingLicensesToModify;i++){
				if(licenseStatus.getPending().isResend()!=null && licenseStatus.getPending().isResend().equals(true)) {
					webOp.driver().findElement("//dynamic locator for pending user").click();
					if(userNLicense.isCancel()!=null && userNLicense.isCancel().equals(true)) {
						webOp.driver().findElement("CancelResendInviteLocator").click();
					}else 
						webOp.driver().findElement("ConfirmResendInviteLocator").click();
				}
				if(licenseStatus.getPending().isCancelInvite()!=null && licenseStatus.getPending().isCancelInvite().equals(true)) {
					webOp.driver().findElement("CancelInviteLocator").click();
				}
			}*/
			int pendingUser=previousNoOfPendingUser-noOfPendingLicensesToModify;//check the size of the table after removing the user
			List<WebElement> CurrentPendingUserList=webOp.driver().findElements(pendingUserList);
			int currentNoOfPendingUser=CurrentPendingUserList.size();
			Assert.assertEquals(currentNoOfPendingUser, pendingUser,"After removing the pending user,the pending user list is displayed correctly.");
		}
	}

	private void manageActiveLicenses(LicenseStatus licenseStatus) {

		List<WebElement> OldActiveUserList=webOp.driver().findElements(activerUserList);
		int previousNoOfActiveUser=OldActiveUserList.size();
		if(licenseStatus.getActive()!=null && licenseStatus.getActive().getUserNLicenseDetails()!=null) {
			int noOfLicensesToModify=licenseStatus.getActive().getUserNLicenseDetails().getNoOfLicensesToChange();
			UserNLicenseDetails userNLicense=licenseStatus.getActive().getUserNLicenseDetails();
			for(int i=0;i<noOfLicensesToModify;i++){
				if(licenseStatus.getActive().isRemoveUser()!=null && licenseStatus.getActive().isRemoveUser().equals(true)){
					String emailID=licenseStatus.getActive().getUserNLicenseDetails().getUserDetails().get(0).getEmailId();
					By trashCanLocator=By.xpath("//tr[td/span[contains(text(),'"+emailID+"')]]//button[@title='Remove user']");
					webOp.driver().findElement(trashCanLocator).click();
					if(userNLicense.isCancel()!=null && userNLicense.isCancel().equals(true)) {
						webOp.driver().findElement(CancelRemove).click();
					}
					else 
						webOp.driver().findElement(confirmRemoveUser).click();
				}
			}
			int activeUser=previousNoOfActiveUser-noOfLicensesToModify;//check the size of the table after removing the user
			List<WebElement> CurrentActiveUserList=webOp.driver().findElements(activerUserList);
			int currentNoOfActiveUser=CurrentActiveUserList.size();
			Assert.assertEquals(currentNoOfActiveUser, activeUser,"After removing the active user,the active user list is displayed correctly.");
		}

	}

}
