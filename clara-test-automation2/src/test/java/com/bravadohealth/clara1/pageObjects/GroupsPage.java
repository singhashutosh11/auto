package com.bravadohealth.clara1.pageObjects;

import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class GroupsPage extends AbstractPage {

	
	private String facilityAdminGroupName = "Facility Administrator";
	private String ePCSPrescriberGroupName = "EPCS Prescriber";
	private String nonEPCSPrescriberGroupName = "Non-EPCS Prescriber";
	private String prescribingAssistantGroupName = "Prescribing Assistant";
	
	By facilityAdministratorGroupLocator=By.xpath("//a[text()='" + facilityAdminGroupName + "']");
	By ePCSPrescriberGroupLocator=By.xpath("//a[text()='" + ePCSPrescriberGroupName + "']");
	By nonEPCSPrescriberGroupLocator=By.xpath("//a[text()='" + nonEPCSPrescriberGroupName + "']");
	By prescribingAssistantGroupLocator=By.xpath("//a[text()='" + prescribingAssistantGroupName + "']");
	
	By groupDetailsElementLocator=By.xpath("//a[text()='Group Details']");
	By managePermissionsElementLocator=By.xpath("//a[text()='Manage Permissions']");
	By deleteGroupBtnLocator=By.cssSelector("button.button.button-flat.button-done");
	By permissionLineItemLocator = By.cssSelector(".row.permission-group-line-item");
	By permissionLineItemToggleLocator = By.cssSelector("div.permission-toggle");
	By permissionLineItemToggleSwitcheryLocator = By.cssSelector(".switchery.switchery-small");
	By addRemoveUsersElementLocator=By.xpath("//a[text()='Add/Remove Users']");
	By selectedUsersForGroupLocator = By.cssSelector("#cl-multi-select select#selectedlist option");
	By addRemoveUsersTabLeftButtonLocator = By.cssSelector("input#btnLeft");
	By saveChangesBtnLocator=By.cssSelector("button.button.button-flat.primary-action");
	By toastrLocator=By.cssSelector("#toast-container");
	By cancelButtonOnUserDetailsModal = By.cssSelector("button.button.button-link.secondary-action");
	
	public GroupsPage(WebOperation webOp, PageDataProvider dataProvider) {
		
		super(webOp, dataProvider);	
	}

	public GroupsPage verifyDefaultGroupsExists() {
		
		Assert.assertEquals(facilityAdminGroupName, webOp.driver().findElement(facilityAdministratorGroupLocator).getText());
		Assert.assertEquals(ePCSPrescriberGroupName, webOp.driver().findElement(ePCSPrescriberGroupLocator).getText());
		Assert.assertEquals(nonEPCSPrescriberGroupName, webOp.driver().findElement(nonEPCSPrescriberGroupLocator).getText());
		Assert.assertEquals(prescribingAssistantGroupName, webOp.driver().findElement(prescribingAssistantGroupLocator).getText());
		logger.info("The Default groups were found successfully");
		return this;
	}
	
	public GroupsPage verifyDefaultGroupsCannotBeRenamed() throws Exception {
		List<By> groupLocators = Arrays.asList(facilityAdministratorGroupLocator, ePCSPrescriberGroupLocator, nonEPCSPrescriberGroupLocator, prescribingAssistantGroupLocator);
		List<String> groupNames = Arrays.asList(facilityAdminGroupName, ePCSPrescriberGroupName, nonEPCSPrescriberGroupName, prescribingAssistantGroupName);
		for (int i = 0; i < groupLocators.size(); i++) {
			findElementAndClick(groupLocators.get(i));

			Assert.assertTrue(webOp.driver().findElements(By.cssSelector("input[value='" + groupNames.get(i) + "'")).isEmpty());
			
			findElementAndClick(cancelButtonOnUserDetailsModal);
			Thread.sleep(1000);
		}
		logger.info("Verified - the default groups cannot be renamed.");
		return this;
	}
	
	public GroupsPage verifyDefaultGroupsCannotBeDeleted() {
		List<By> groupLocators = Arrays.asList(facilityAdministratorGroupLocator, ePCSPrescriberGroupLocator, nonEPCSPrescriberGroupLocator, prescribingAssistantGroupLocator);
		for (int i = 0; i < groupLocators.size(); i++) {
			WebElement groupNameEl = webOp.driver().findElement(facilityAdministratorGroupLocator);
			WebElement groupRowEl = groupNameEl.findElement(By.xpath("../.."));
			WebElement checkboxEl = groupRowEl.findElement(By.cssSelector("input.select-to-delete"));
			Assert.assertTrue(checkboxEl.getAttribute("disabled").equals("disabled") || checkboxEl.getAttribute("disabled").equals("true"));
			checkboxEl.click();
			WebElement deleteBtnElement = webOp.driver().findElement(deleteGroupBtnLocator);
			Assert.assertTrue(deleteBtnElement.getAttribute("disabled").equals("disabled") || deleteBtnElement.getAttribute("disabled").equals("true"));
		}
		logger.info("Verified - the default groups cannot be deleted.");
		return this;
	}

	@SuppressWarnings("unchecked")
	public GroupsPage verifyCorrectPermissionsAreAssociatedWithDefaultGroups() throws Exception {
		List<By> groupLocators = Arrays.asList(facilityAdministratorGroupLocator, ePCSPrescriberGroupLocator, nonEPCSPrescriberGroupLocator, prescribingAssistantGroupLocator);
		
		List<String> permissionsForFAdminGroup = Arrays.asList("Edit facility", "View facility", "Edit details", "Edit permissions", "View details", "Edit groups", "View groups", "Manage account");
		List<String> permissionsForEPCSPrescribingGroup = Arrays.asList("Prescribe Schedule II drugs", "Prescribe Schedule III drugs", "Prescribe Schedule IV drugs", "Prescribe Schedule V drugs", "Prescriber", "Edit clinical data");
		List<String> permissionsForNonEPCSPrescribingGroup = Arrays.asList("Prescriber", "Edit clinical data");
		List<String> permissionsForPrescribingAssistantGroup = Arrays.asList("Prepare prescriptions", "Edit clinical data");

		List<List<String>> permissionList = Arrays.asList(permissionsForFAdminGroup, permissionsForEPCSPrescribingGroup, permissionsForNonEPCSPrescribingGroup, permissionsForPrescribingAssistantGroup);
		
		for (int i = 0; i < groupLocators.size(); i++) {
			findElementAndClick(groupLocators.get(i));
			findElementAndClick(managePermissionsElementLocator);

			Thread.sleep(1200);
			List<String> thizPermission = permissionList.get(i);
			
			List<WebElement> permissionEls = webOp.driver().findElements(permissionLineItemLocator);
			int permissionCount = 0;
			for (WebElement permEl: permissionEls) {
				WebElement permissionToggleEl = permEl.findElement(permissionLineItemToggleLocator);
				if (permissionToggleEl.findElement(permissionLineItemToggleSwitcheryLocator).getCssValue("background-color").equals("rgba(76, 217, 100, 1)")) {
					String permissionName = permissionToggleEl.findElement(By.xpath("..")).findElement(By.cssSelector(".permission .permission-lable")).getText();
					if (thizPermission.indexOf(permissionName) != -1) {
						permissionCount++;
					} else {
						fail("An extra permission was found for the default group. Permission Name: " + permissionName + ". Group Name: " + groupLocators.get(i).toString());
					}
				}
			}
			if (permissionCount != thizPermission.size()) {
				fail("A permission for the default group wasn't found. Group Name - " + groupLocators.get(i).toString());
			}
			webOp.driver().findElement(By.cssSelector("button.button.button-link.secondary-action")).click();
			Thread.sleep(1200);
		}
		logger.info("Verified - the default groups have the correct permissions.");
		return this;
	}
	
	public GroupsPage verifyFacilityAdminGroupRestrictions() throws Exception {
		findElementAndClick(facilityAdministratorGroupLocator);
		verifyFAdminGroupPermissionsCannotBeChanged();
		verifyFAdminGroupUsersListCannotBeEmpty();
		return this;
	}
	
	private void verifyFAdminGroupPermissionsCannotBeChanged() throws Exception, InterruptedException {
		findElementAndClick(addRemoveUsersElementLocator);
		Thread.sleep(1200);
		
		List<WebElement> permissionEls = webOp.driver().findElements(permissionLineItemLocator);
		for (WebElement permEl: permissionEls) {
			WebElement permissionToggleEl = permEl.findElement(permissionLineItemToggleLocator);
			WebElement switcheryElement = permissionToggleEl.findElement(permissionLineItemToggleSwitcheryLocator);
			String oldValue = switcheryElement.getCssValue("background-color");
			new Actions(webOp.driver()).moveToElement(switcheryElement).click().perform();
			String newValue = switcheryElement.getCssValue("background-color");
			Assert.assertTrue(oldValue.equals(newValue));
		}
		logger.info("Verified - Permission of the Facility Administrator group cannot be modified by any user");
	}
	
	private void verifyFAdminGroupUsersListCannotBeEmpty() throws Exception, InterruptedException {
		findElementAndClick(addRemoveUsersElementLocator);
		Thread.sleep(1200);
		
		List<WebElement> selectedUsersEl = webOp.driver().findElements(selectedUsersForGroupLocator);
		WebElement leftButtonEl = webOp.driver().findElement(addRemoveUsersTabLeftButtonLocator);
		Actions leftButtonActionEl = new Actions(webOp.driver()).moveToElement(leftButtonEl);
		
		for (WebElement selectedUserEl: selectedUsersEl) {
			new Actions(webOp.driver()).moveToElement(selectedUserEl).click().perform();
			leftButtonActionEl.click().perform();
		}
		new Actions(webOp.driver()).moveToElement(webOp.driver().findElement(saveChangesBtnLocator)).click().perform();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(toastrLocator));
		WebElement toastrEl = webOp.driver().findElement(toastrLocator);
		Assert.assertEquals(toastrEl.findElement(By.cssSelector(".toast-message")).getText(), "There must always be at least one user in this group.", "Verified - Facility Administrator Group Users List cannot be empty");
	}

	private boolean isUserPresentInThizGroupNextStep() {
		String userName = getUserNameFromClaraUserDetails((ClaraUserDetails)(dataProvider.getPageData("claraUserDetails")));
		List<WebElement> selectedUsersEl = webOp.driver().findElements(selectedUsersForGroupLocator);
		boolean userFound = false;
		for (WebElement selectedUserEl: selectedUsersEl) {
			if (selectedUserEl.getText().equals(userName)) {
				userFound = true;
				break;
			}
		}
		return userFound;
	}
	
	public GroupsPage verifyUserPresentInEPCSGroup() throws Exception {
		findElementAndClick(ePCSPrescriberGroupLocator);
		findElementAndClick(addRemoveUsersElementLocator);
		Assert.assertTrue(isUserPresentInThizGroupNextStep());
		
		return this;
	}
	
	public GroupsPage verifyUserPresentInNonEPCSGroup() throws Exception {
		findElementAndClick(nonEPCSPrescriberGroupLocator);
		findElementAndClick(addRemoveUsersElementLocator);
		Assert.assertTrue(isUserPresentInThizGroupNextStep());
		
		return this;
	}
	
	public GroupsPage verifyBasicUserOnlyPresentInFAdminGroup() throws Exception {
		List<By> groupLocators = Arrays.asList(facilityAdministratorGroupLocator, ePCSPrescriberGroupLocator, nonEPCSPrescriberGroupLocator, prescribingAssistantGroupLocator);
		for (int i = 0; i < groupLocators.size(); i++) {
			findElementAndClick(groupLocators.get(i));
			findElementAndClick(addRemoveUsersElementLocator);
			Assert.assertTrue((groupLocators.get(i) == facilityAdministratorGroupLocator && isUserPresentInThizGroupNextStep()) 
					|| (groupLocators.get(i) != facilityAdministratorGroupLocator && !isUserPresentInThizGroupNextStep()));
			
			findElementAndClick(cancelButtonOnUserDetailsModal);
			Thread.sleep(1000);
		}
		
		return this;
	}

	private String getUserNameFromClaraUserDetails(ClaraUserDetails claraUserDetails) {
		StringBuilder entityName = new StringBuilder();
		entityName.append(claraUserDetails.getFirstname());
		entityName.append(" ");
		if (!StringUtils.isEmpty(claraUserDetails.getMiddleName())) {
			entityName.append(claraUserDetails.getMiddleName());
			entityName.append(" ");
		}
		entityName.append(claraUserDetails.getLastName());
		
		return entityName.toString();
	}

	private void findElementAndClick(By elSelector) throws Exception {
		WebElement webElement = webOp.driver().findElement(elSelector);
		wait.until(ExpectedConditions.visibilityOf(webElement));
		new Actions(webOp.driver()).moveToElement(webElement).click().perform();
		webOp.waitTillAllCallsDoneUsingJS();
	}
}
