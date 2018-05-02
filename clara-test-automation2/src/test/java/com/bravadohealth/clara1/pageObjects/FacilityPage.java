package com.bravadohealth.clara1.pageObjects;


import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.enums.SupportedTimeZoneEnum;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.FacilityCompleteSectionLocators;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.services.FacilityService;
import com.bravadohealth.clara1.utility.EnumLookUpUtil;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.EditFacility;
import com.bravadohealth.pagedataset.EditFacilitySection;
import com.bravadohealth.pagedataset.EditFacilitySettings;
import com.bravadohealth.pagedataset.Facility;
import com.bravadohealth.pagedataset.IdentityProofing;
import com.bravadohealth.pagedataset.Nominator;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class FacilityPage extends AbstractPage implements ConfirmationModalLocators, LoaderLocators, FacilityCompleteSectionLocators{
	/*--------------------facility section locators start-----------------------------------------*/
	private By facilitySectionLocator=By.xpath("//div[contains(@class, 'facility-wrapper')]");
	
	private By facilityViewSectionLocator = By.xpath("//div[contains(@class, 'facility-details')]");
	private By facilityEditSectionLocator = By.xpath("//div[contains(@class, 'facility-edit')]");
	private String facilityViewSectionLocatorXpath = "//div[contains(@class, 'facility-details')]";
	private String facilityEditSectionLocatorXpath = "//div[contains(@class, 'facility-edit')]";
	
	private By viewFacilityNameInFacilityEditSectionLocator =By.xpath("//h3[contains(text(), 'FACILITY NAME')]/following-sibling::div[@id='usernameConfirmation']");
	private By ChangeFacilityName=By.xpath("//button[text()='Change Facility Name…']");
	private By changeFacilityNameContainer = By.id("change-facility-name-container");
	private By facilityNameInput = By.xpath("//input[@placeholder = 'facility name']");
	private By facilityNameShownInChangeFacilityNameContainerLocator = By.xpath("//div[contains(@class, 'flow-label')]/strong");
	private By facilityNameErrorMsg = By.xpath("//span[contains(@class, 'inline-error-msg')]");
	private By cancelFacilityNameButton=By.cssSelector("button.nav-cancel");
	private By saveFacilityNameButton=By.cssSelector("button.nav-action");


	By facilitySaveButton=By.xpath("//button[contains(text(),'Save')]");

	By viewFacilityName=By.xpath("//h3[contains(text(), 'FACILITY NAME')]/following-sibling::div");
	By viewFullAddress=By.xpath("//h3[contains(text(), 'ADDRESS')]/following-sibling::div");
	By viewPhoneNumber=By.xpath("//h3[contains(text(), 'PHONE')]/following-sibling::div");
	By viewFax=By.xpath("//h3[contains(text(), 'FAX')]/following-sibling::div");
	By viewNPINumber=By.xpath("//h3[contains(text(), 'NPI NUMBER')]/following-sibling::div");
	By viewIdentityProofingType=By.xpath("//h3[contains(text(), 'IDENTITY PROOFING TYPE')]/following-sibling::div");
	
	By displayedTimeZoneLocator=By.xpath("//div[h3[contains(text(),'Time Zone')]]/div[contains(@class,'time-zone')]");
	By displayedAppliedDstLocator=By.xpath("//div[h3[contains(text(),'Apply Daylight Saving Time')]]/div[contains(@class,'time-zone')]");
	By accountLocator = By.xpath("//span[contains(@class,'gn-text') and text()='Account']");

	private By facilityAddress=By.cssSelector("input[placeholder='address']");
	private By cityName=By.cssSelector("input[placeholder='city']");
	private By StateLocator=By.xpath("//select[option[1][text()='state']]");
	private By zipCode=By.cssSelector("input[placeholder='zip code']");
	
	private By phoneNumber=By.cssSelector("input[placeholder='phone']");
	private By faxNumber=By.cssSelector("input[placeholder='fax']");
	private By npiNumber=By.cssSelector("input[placeholder='npi number']");
	


	private By viewNominatorName=By.xpath("//h3[contains(text(), 'NOMINATOR NAME')]/following-sibling::div");
	private By viewNominatorEmailId=By.xpath("//h3[contains(text(), 'NOMINATOR EMAIL ID')]/following-sibling::div");
	private By viewAltNominatorName=By.xpath("//h3[contains(text(), 'ALTERNATE NOMINATOR NAME')]/following-sibling::div");
	private By viewAltNominatorEmailId=By.xpath("//h3[contains(text(), 'ALTERNATE NOMINATOR EMAIL ID')]/following-sibling::div");


	/*--------------------facility section locators end-----------------------------------------*/
	/*--------------------settings section locators start-----------------------------------------*/
	private By facilitySettingsSectionLocator=By.xpath("//div[contains(@class, 'settings-wrapper')]");
	
	private By facilitySettingsViewSectionLocator = By.xpath("//div[contains(@class, 'settings-details')]");
	private By facilitySettingsEditSectionLocator = By.xpath("//div[contains(@class, 'settings-edit')]");
	private String facilitySettingsViewSectionXpath = "//div[contains(@class, 'settings-details')]";
	private String facilitySettingsEditSectionXpath = "//div[contains(@class, 'settings-edit')]";
	
	private static String timeZoneOptionsValuePrefix = "string:"; 
	private By editTimeZoneLocator=By.xpath("//h3[contains(text(), 'Time Zone')]/following-sibling::div[contains(@class, 'select-wrapper')]/select");
	private By editApplyDSTCheckboxLocator=By.xpath("//input[contains(@class, 'js-switch')]");
	private By editApplyDSTSwitcheryLocator=By.xpath("//span[contains(@class, 'switchery')]");
	private By editFtiNumber=By.cssSelector("input[placeholder='fti number']");
	private By editIdleTimeOut=By.cssSelector("input[name='idleTimeoutInMin']");
	
	private By viewTimeZoneLocator=By.xpath("//h3[contains(text(), 'Time Zone')]/following-sibling::div");
	private By viewDSTLocator=By.xpath("//h3[contains(text(), 'Apply Daylight Saving Time')]/following-sibling::div");
	private By viewIdleLogoutTimeLocator=By.xpath("//h3[contains(text(), 'IDLE LOGOUT TIME')]/following-sibling::div");
	private By viewFtiNumberLocator=By.xpath("//h3[contains(text(), 'FTI NUMBER')]/following-sibling::div");
	/* ---------------------settings section locators end------------------------------------*/
	/* ---------------------common locators start------------------------------------*/
	private static String editSectionButton = "//button[contains(text(),'Edit')]";
	private static String saveSectionButton = "//button[contains(text(),'Save')]";
	private static String cancelSectionButton = "//button[contains(text(),'Cancel')]";
	/* ---------------------common locators end------------------------------------*/
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityPage.class);
    
	public FacilityPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}
	
	public FacilityPage moveFacilitySectionIntoView() throws InterruptedException {
		WebElement facilitySectionElement = webOp.driver().findElement(facilitySectionLocator);
		jse.executeScript("arguments[0].scrollIntoView(true);", facilitySectionElement);
		Thread.sleep(500);
		return this;
	}

	public FacilityPage clickOnEditFacility() throws Exception {
		moveFacilitySectionIntoView();
		
		WebElement facilityViewSectionEditButtonElement = webOp.driver().findElement(By.xpath(facilityViewSectionLocatorXpath + editSectionButton));
		facilityViewSectionEditButtonElement.click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfElementLocated(facilityEditSectionLocator));
		return this;
	}
	
	public FacilityPage editFacilityDetails() throws Exception {
		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");
		if(editFacilitySection != null) {
			EditFacility facility = editFacilitySection.getEditFacility();
			if(facility!=null) {
				ChangeFacilityName(facility);
				if(facility.getAddress() != null) {
					webOp.driver().findElement(facilityAddressLocator).clear();
					webOp.driver().findElement(facilityAddressLocator).sendKeys(facility.getAddress());
				}
				if(facility.getCity() != null) {
					webOp.driver().findElement(cityLocator).clear();
					webOp.driver().findElement(cityLocator).sendKeys(facility.getCity());
				}
				if(facility.getState() != null) {
					Select stateDropDown = new Select(webOp.driver().findElement(stateLocator));
					stateDropDown.selectByVisibleText(facility.getState());
				}
				if(facility.getZipCode() != null) {
					webOp.driver().findElement(zipCode).clear();
					webOp.driver().findElement(zipCode).sendKeys(facility.getZipCode());
				}
				if(facility.getPhone() != null) {
					webOp.driver().findElement(phoneNumber).clear();
					webOp.driver().findElement(phoneNumber).sendKeys(Long.toString(facility.getPhone()));
				}
				if(facility.getFaxNumber() != null) {
					webOp.driver().findElement(faxNumber).clear();
					webOp.driver().findElement(faxNumber).sendKeys(Long.toString(facility.getFaxNumber()));
				}
				if(facility.getFacilityNPI() != null) {
					webOp.driver().findElement(npiNumber).clear();
					webOp.driver().findElement(npiNumber).sendKeys(facility.getFacilityNPI());
				}
				if(facility.getNominator() != null) {
					Nominator nominator = facility.getNominator();
					String firstName = nominator.getFirstName();
					String lastName = nominator.getLastName();
					String emailAddress = nominator.getEmailAddress();
					boolean isNominatorChanged = false;
					if(firstName != null) {
						WebElement nominatorFirstNameElement = webOp.driver().findElement(NominatorFirstNameLocator);
						String existingFirstName = nominatorFirstNameElement.getAttribute("value");
						nominatorFirstNameElement.clear();
						nominatorFirstNameElement.sendKeys(firstName);
						if(!firstName.equalsIgnoreCase(existingFirstName)) {
							isNominatorChanged = true;
						}
					}
					if(lastName != null) {
						WebElement nominatorLastNameElement = webOp.driver().findElement(NominatorLastNameLocator);
						String existingLastName = nominatorLastNameElement.getAttribute("value");
						nominatorLastNameElement.clear();
						nominatorLastNameElement.sendKeys(lastName);
						if(!lastName.equalsIgnoreCase(existingLastName)) {
							isNominatorChanged = true;
						}
					}
					if(emailAddress != null) {
						WebElement nominatorEmailAddressElement = webOp.driver().findElement(NominatorMailAddress);
						String existingEmailAddress = nominatorEmailAddressElement.getAttribute("value");
						nominatorEmailAddressElement.clear();
						nominatorEmailAddressElement.sendKeys(emailAddress);
						if(!emailAddress.equalsIgnoreCase(existingEmailAddress)) {
							isNominatorChanged = true;
						}
					}
					FacilityService.setIsNominatorChanged(isNominatorChanged);
				}
				if(facility.getAltNominator() != null) {
					Nominator altNominator = facility.getAltNominator();
					String firstName = altNominator.getFirstName();
					String lastName = altNominator.getLastName();
					String emailAddress = altNominator.getEmailAddress();
					boolean isAltNominatorChanged = false;
					if(firstName != null) {
						WebElement altNominatorFirstNameElement = webOp.driver().findElement(AltNominatorFirstNameLocator);
						String existingFirstName = altNominatorFirstNameElement.getAttribute("value");
						altNominatorFirstNameElement.clear();
						altNominatorFirstNameElement.sendKeys(firstName);
						if(!firstName.equalsIgnoreCase(existingFirstName)) {
							isAltNominatorChanged = true;
						}
					}
					if(lastName != null) {
						WebElement altNominatorLastNameElement = webOp.driver().findElement(AltNominatorLastNameLocator);
						String existingLastName = altNominatorLastNameElement.getAttribute("value");
						altNominatorLastNameElement.clear();
						altNominatorLastNameElement.sendKeys(lastName);
						if(!lastName.equalsIgnoreCase(existingLastName)) {
							isAltNominatorChanged = true;
						}
					}
					if(emailAddress != null) {
						WebElement altNominatorEmailAddressElement = webOp.driver().findElement(AltNominatorMailAddress);
						String existingEmailAddress = altNominatorEmailAddressElement.getAttribute("value");
						altNominatorEmailAddressElement.clear();
						altNominatorEmailAddressElement.sendKeys(emailAddress);
						if(!emailAddress.equalsIgnoreCase(existingEmailAddress)) {
							isAltNominatorChanged = true;
						}
					}
					FacilityService.setIsAltNominatorChanged(isAltNominatorChanged);
				}
				webOp.driver().findElement(By.xpath(facilityEditSectionLocatorXpath +saveSectionButton)).click();
				webOp.waitTillAllCallsDoneUsingJS();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
				wait.until(ExpectedConditions.visibilityOfElementLocated(facilityViewSectionLocator));
			}
		}
		cancelEditFacilityDetailsIfNeeded();
		return this;
	}

	private void ChangeFacilityName(EditFacility editFacility) throws Exception {
		if(editFacility.getNewFacilityName()!=null){
			WebElement changeFacilityNameElement = webOp.driver().findElement(ChangeFacilityName);
			changeFacilityNameElement.click();
			WebElement facilityNameChangeContainer = webOp.driver().findElement(changeFacilityNameContainer);
			wait.until(ExpectedConditions.visibilityOf(facilityNameChangeContainer));
			//give explicit delay so that popup modal is visible when changing
			Thread.sleep(1000);
			int randomSuffix = (int) (Math.random()*(Integer.MAX_VALUE));
			//add random suffix to new facility name  from pageData
			facilityNameChangeContainer.findElement(facilityNameInput).sendKeys(editFacility.getNewFacilityName()+randomSuffix);
			//give explicit delay so that changed name is visible
			Thread.sleep(500);
			if(editFacility.isCancelChangeFacilityName()== null || Boolean.FALSE == editFacility.isCancelChangeFacilityName()) {
				facilityNameChangeContainer.findElement(saveFacilityNameButton).click();
			}else {
				facilityNameChangeContainer.findElement(cancelFacilityNameButton).click();
			}
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
			wait.until(ExpectedConditions.invisibilityOf(facilityNameChangeContainer));
		}
	}

	public FacilityPage verifyModifiedFacilityDetails() {
		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");
		if(editFacilitySection != null) {
			EditFacility newFacilityDetails = editFacilitySection.getEditFacility();
			if(newFacilityDetails !=null) {
				WebElement facilityViewElement = webOp.driver().findElement(facilityViewSectionLocator);
				if(newFacilityDetails.getNewFacilityName()!=null && (newFacilityDetails.isCancelChangeFacilityName()== null || Boolean.FALSE == newFacilityDetails.isCancelChangeFacilityName())) {
					String displayedFacilityName=facilityViewElement.findElement(viewFacilityName).getText();
					//displayed facility name should contain new facility name from pageData plus some suffix 
					if(!displayedFacilityName.contains(newFacilityDetails.getNewFacilityName())) {
						LOGGER.error("facility name not correct expected={}, actual={}", new Object[]{newFacilityDetails.getNewFacilityName(), displayedFacilityName});
						webOp.getSoftAssert().fail("The facilityName is not displayed correctly");
					}
					FacilityService.getFacility().setFacilityName(newFacilityDetails.getNewFacilityName());
				}
				if(newFacilityDetails.getAddress()!=null) {
					//TODO: should build full address from state, city..
					String displayedFacilityAddress=facilityViewElement.findElement(viewFullAddress).getText();
					Assert.assertEquals(displayedFacilityAddress,newFacilityDetails.getAddress() ,"The facilityAddress is displayed correctly");
				}
				if(newFacilityDetails.getPhone() != null) {
					String displayedFacilityPhone=facilityViewElement.findElement(viewPhoneNumber).getText();
					Assert.assertEquals(displayedFacilityPhone,newFacilityDetails.getPhone() ,"The facility phoneNumber is displayed correctly");
				}
				if(newFacilityDetails.getFaxNumber() != null) {
					String displayedFacilityFax=facilityViewElement.findElement(viewFax).getText();
					Assert.assertEquals(displayedFacilityFax,newFacilityDetails.getFaxNumber() ,"The facility Fax is displayed correctly");
				}
				if(newFacilityDetails.getFacilityNPI() != null) {
					String displayedFacilityNpi=facilityViewElement.findElement(viewNPINumber).getText();
					Assert.assertEquals(displayedFacilityNpi,newFacilityDetails.getFacilityNPI() ,"The facility NPI number is displayed correctly");
				}
				if(newFacilityDetails.getNominator() != null) {
					Nominator nominator = newFacilityDetails.getNominator();
					String firstName = nominator.getFirstName();
					String lastName = nominator.getLastName();
					String emailAddress = nominator.getEmailAddress();
					if(firstName != null) {
						String displayedNominatorName=facilityViewElement.findElement(viewNominatorName).getText();
						if(!displayedNominatorName.contains(firstName)) {
							LOGGER.error("nominator first name not found fullName={}, firstName={}", new Object[]{displayedNominatorName, firstName});
							webOp.getSoftAssert().fail("Nominator firstName is not found in full name");
						}
					}
					if(lastName != null) {
						String displayedNominatorName=facilityViewElement.findElement(viewNominatorName).getText();
						if(!displayedNominatorName.contains(lastName)) {
							LOGGER.error("nominator last name not found fullName={}, lastName={}", new Object[]{displayedNominatorName, lastName});
							webOp.getSoftAssert().fail("Nominator lastName is not found in full name");
						}
					}
					if(emailAddress != null) {
						String displayedNominatorEmail=facilityViewElement.findElement(viewNominatorEmailId).getText();
						if(!displayedNominatorEmail.equalsIgnoreCase(emailAddress)) {
							LOGGER.error("nominator email not correct expected={}, actual={}", new Object[]{emailAddress, displayedNominatorEmail});
							webOp.getSoftAssert().fail("Nominator emailAddress is not displayed correctly");
						}
					}
					FacilityService.getFacility().setNominator(newFacilityDetails.getNominator());
				}
				if(newFacilityDetails.getAltNominator() != null) {
					Nominator altNominator = newFacilityDetails.getAltNominator();
					String firstName = altNominator.getFirstName();
					String lastName = altNominator.getLastName();
					String emailAddress = altNominator.getEmailAddress();
					if(firstName != null) {
						String displayedNominatorName=facilityViewElement.findElement(viewAltNominatorName).getText();
						if(!displayedNominatorName.contains(firstName)) {
							LOGGER.error("altNominator first name not found fullName={}, firstName={}", new Object[]{displayedNominatorName, firstName});
							webOp.getSoftAssert().fail("Alternate Nominator firstName is not found in full name");
						}
					}
					if(lastName != null) {
						String displayedNominatorName=facilityViewElement.findElement(viewAltNominatorName).getText();
						if(!displayedNominatorName.contains(lastName)) {
							LOGGER.error("altNominator last name not found fullName={}, lastName={}", new Object[]{displayedNominatorName, lastName});
							webOp.getSoftAssert().fail("Alternate Nominator lastName is not found in full name");
						}
					}
					if(emailAddress != null) {
						String displayedNominatorEmail=facilityViewElement.findElement(viewAltNominatorEmailId).getText();
						if(!displayedNominatorEmail.equalsIgnoreCase(emailAddress)) {
							LOGGER.error("altNominator email not correct expected={}, actual={}", new Object[]{emailAddress, displayedNominatorEmail});
							webOp.getSoftAssert().fail("Alternate Nominator emailAddress is not displayed correctly");
						}
					}
					FacilityService.getFacility().setAltNominator(newFacilityDetails.getAltNominator());
				}
				//Verify the type of IdentityProofing
				if(newFacilityDetails.getIdentityProofing()!=null) {
					IdentityProofing identityProofing=newFacilityDetails.getIdentityProofing();
					WebElement idProofingElement = facilityViewElement.findElement(viewIdentityProofingType);
					if(identityProofing.isInstitutional()!=null) {
						Assert.assertTrue(idProofingElement.getText().equalsIgnoreCase("Institutional"), "The identity proofing is not institutaional");
					}
					else 
						Assert.assertTrue(idProofingElement.getText().equalsIgnoreCase("Individual"), "The identity proofing is not individual");
				}
			}
		}
		return this;
	}

	public FacilityPage verifyFacilityPage() {
		Facility facility=dataProvider.getPageData("facility");
		/*boolean displayedFacilityName=webOp.driver().findElement(By.xpath("//div[contains(text(),'"+facilityName+"')]")).isDisplayed();
		Assert.assertTrue(displayedFacilityName, "The facility name is displayed correctly");*/
		String NominatorEmail=facility.getNominator().getEmailAddress();
		boolean displayedNominatorEmail=webOp.driver().findElement(By.xpath("//div[contains(text(),'"+NominatorEmail+"')]")).isDisplayed();
		Assert.assertTrue(displayedNominatorEmail, "The nominator email displayed correctly");
		String altNominatorEmail=facility.getAltNominator().getEmailAddress();
		boolean displayedAltNominatorEmail=webOp.driver().findElement(By.xpath("//div[contains(text(),'"+altNominatorEmail+"')]")).isDisplayed();
		Assert.assertTrue(displayedAltNominatorEmail, "The alt nominator email displayed correctly");
		//String selectedTimeZone=facility.getTimeZone();
		String selectedTimeZone = SupportedTimeZoneEnum.valueOf(facility.getTimeZone()).getLabel();
		String displayedTimeZone=webOp.driver().findElement(displayedTimeZoneLocator).getText();
		Assert.assertEquals(displayedTimeZone, selectedTimeZone,"The timezone is displayed correctly as selected in the facility setup");		
		Boolean dstValue=facility.isApplyDST();
		if(dstValue != null && dstValue.booleanValue() == true) {
			String ApplyDst=webOp.driver().findElement(displayedAppliedDstLocator).getText();
			assertEquals(ApplyDst, "Yes", "By default the DST will be applied for a facility");
		}
		String displayedTime = webOp.driver().findElement(viewIdleLogoutTimeLocator).getText();
		Assert.assertEquals(displayedTime, "10 minutes","The idleLogoutTime is displayed correctly as selected in the facility setup");
		return this;
	}
	public AccountPage navigateToAccountSection() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(accountLocator).click();
		return new AccountPage(webOp, dataProvider);
	}

	
	public FacilityPage editFacilityDetailsNegativeScenario() throws Exception {
		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");
		if(editFacilitySection != null) {
			EditFacility editFacility = editFacilitySection.getEditFacility();
			if(editFacility!=null) {
				boolean isInputFieldInvalid = false;
				isInputFieldInvalid = changeFacilityNameToSame(editFacility);
				if(editFacility.getAddress() != null) {
					WebElement facilityAddressElement = webOp.driver().findElement(facilityAddressLocator);
					facilityAddressElement.clear();
					facilityAddressElement.sendKeys(editFacility.getAddress());
					if(HelperUtility.isInputFieldInvalid(facilityAddressElement)){
						isInputFieldInvalid = true;
					}
				}
				if(editFacility.getCity() != null) {
					WebElement cityElement = webOp.driver().findElement(cityLocator);
					cityElement.clear();
					cityElement.sendKeys(editFacility.getCity());
					if(HelperUtility.isInputFieldInvalid(cityElement)){
						isInputFieldInvalid = true;
					}
				}
				if(editFacility.getState() != null) {
					Select stateDropDown = new Select(webOp.driver().findElement(stateLocator));
					stateDropDown.selectByVisibleText(editFacility.getState());
				}
				if(editFacility.getZipCode() != null) {
					WebElement zipCodeElement = webOp.driver().findElement(zipCode);
					zipCodeElement.clear();
					zipCodeElement.sendKeys(editFacility.getZipCode());
					if(HelperUtility.isInputFieldInvalid(zipCodeElement)){
						isInputFieldInvalid = true;
					}
				}
				if(editFacility.getPhone() != null) {
					WebElement phoneNumberElement = webOp.driver().findElement(phoneNumber);
					phoneNumberElement.clear();
					phoneNumberElement.sendKeys(Long.toString(editFacility.getPhone()));
					if(HelperUtility.isInputFieldInvalid(phoneNumberElement)){
						isInputFieldInvalid = true;
					}
				}
				if(editFacility.getFaxNumber() != null) {
					WebElement faxNumberElement = webOp.driver().findElement(faxNumber);
					faxNumberElement.clear();
					faxNumberElement.sendKeys(Long.toString(editFacility.getFaxNumber()));
					if(HelperUtility.isInputFieldInvalid(faxNumberElement)){
						isInputFieldInvalid = true;
					}
				}
				if(editFacility.getNominator() != null) {
					Nominator nominator = editFacility.getNominator();
					String firstName = nominator.getFirstName();
					String lastName = nominator.getLastName();
					String emailAddress = nominator.getEmailAddress();
					if(firstName != null) {
						WebElement nominatorFirstNameElement = webOp.driver().findElement(NominatorFirstNameLocator);
						nominatorFirstNameElement.clear();
						nominatorFirstNameElement.sendKeys(firstName);
						if(HelperUtility.isInputFieldInvalid(nominatorFirstNameElement)){
							isInputFieldInvalid = true;
						}
					}
					if(lastName != null) {
						WebElement nominatorLastNameElement = webOp.driver().findElement(NominatorLastNameLocator);
						nominatorLastNameElement.clear();
						nominatorLastNameElement.sendKeys(lastName);
						if(HelperUtility.isInputFieldInvalid(nominatorLastNameElement)){
							isInputFieldInvalid = true;
						}
					}
					if(emailAddress != null) {
						WebElement nominatorEmailElement = webOp.driver().findElement(NominatorMailAddress);
						nominatorEmailElement.clear();
						nominatorEmailElement.sendKeys(emailAddress);
						if(HelperUtility.isInputFieldInvalid(nominatorEmailElement)){
							isInputFieldInvalid = true;
						}
					}
				}
				if(editFacility.getAltNominator() != null) {
					Nominator altNominator = editFacility.getAltNominator();
					String firstName = altNominator.getFirstName();
					String lastName = altNominator.getLastName();
					String emailAddress = altNominator.getEmailAddress();
					if(firstName != null) {
						WebElement altNominatorFirstNameElement = webOp.driver().findElement(AltNominatorFirstNameLocator);
						altNominatorFirstNameElement.clear();
						altNominatorFirstNameElement.sendKeys(firstName);
						if(HelperUtility.isInputFieldInvalid(altNominatorFirstNameElement)){
							isInputFieldInvalid = true;
						}
					}
					if(lastName != null) {
						WebElement altNominatorLastNameElement = webOp.driver().findElement(AltNominatorLastNameLocator);
						altNominatorLastNameElement.clear();
						altNominatorLastNameElement.sendKeys(lastName);
						if(HelperUtility.isInputFieldInvalid(altNominatorLastNameElement)){
							isInputFieldInvalid = true;
						}
					}
					if(emailAddress != null) {
						WebElement altNominatorEmailElement = webOp.driver().findElement(AltNominatorMailAddress);
						altNominatorEmailElement.clear();
						altNominatorEmailElement.sendKeys(lastName);
						if(HelperUtility.isInputFieldInvalid(altNominatorEmailElement)){
							isInputFieldInvalid = true;
						}
					}
				}
				if(isInputFieldInvalid == false) {
					LOGGER.error("no field found invalid in editFacilityDetails");
					webOp.getSoftAssert().fail("No field was found invalid in editFacilityDetails");
				}
			}
		}
		return this;
	}
	
	private boolean changeFacilityNameToSame(EditFacility editFacility) throws Exception {
		Facility currentFacility = dataProvider.getPageData("facility");
		boolean isInputInvalid = false;
		if(editFacility.getNewFacilityName()!=null){
			isInputInvalid = true;
			WebElement viewFacilityNameInFacilityEditSection = webOp.driver().findElement(viewFacilityNameInFacilityEditSectionLocator);
			String currentFacilityName = viewFacilityNameInFacilityEditSection.getText();
			WebElement changeFacilityNameElement = webOp.driver().findElement(ChangeFacilityName);
			changeFacilityNameElement.click();
			WebElement facilityNameChangeContainer = webOp.driver().findElement(changeFacilityNameContainer);
			wait.until(ExpectedConditions.visibilityOf(facilityNameChangeContainer));
			//give explicit delay so that popup modal is visible when changing
			Thread.sleep(500);
			facilityNameChangeContainer.findElement(facilityNameInput).sendKeys(currentFacilityName);
			//give explicit delay so that changed name is visible
			Thread.sleep(500);
			facilityNameChangeContainer.findElement(saveFacilityNameButton).click();
			webOp.waitTillAllCallsDoneUsingJS();
			if(HelperUtility.isElementAbsent(webOp, facilityNameErrorMsg)) {
				String facilityNameShownInChangeFacilityNameContainer = facilityNameChangeContainer.findElement(facilityNameShownInChangeFacilityNameContainerLocator).getText();
				LOGGER.error("facilityName not equal entered={}, existing={}", new Object[]{currentFacilityName, facilityNameShownInChangeFacilityNameContainer});
				webOp.getSoftAssert().fail("The facility name entered is not equal");
				isInputInvalid = false;
				wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
			}
			else {
				facilityNameChangeContainer.findElement(cancelFacilityNameButton).click();
			}
			wait.until(ExpectedConditions.invisibilityOf(facilityNameChangeContainer));
		}
		return isInputInvalid;
	}
	
	public FacilityPage cancelEditFacilityDetailsIfNeeded() throws Exception {
		By cancelEditFacilityButtonLocator = By.xpath(facilityEditSectionLocatorXpath + cancelSectionButton);
		if(HelperUtility.isElementAbsent(webOp, cancelEditFacilityButtonLocator)){
			return this;
		}
		WebElement cancelEditFacilityButton = webOp.driver().findElement(cancelEditFacilityButtonLocator);
		if(cancelEditFacilityButton.isDisplayed()) {
			cancelEditFacilityButton.click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.visibilityOfElementLocated(facilityViewSectionLocator));
		}
		return this;
	}
	
	public FacilityPage moveFacilitySettingsSectionIntoView() throws InterruptedException {
		WebElement facilitySettingsSectionElement = webOp.driver().findElement(facilitySettingsSectionLocator);
		jse.executeScript("arguments[0].scrollIntoView(true);", facilitySettingsSectionElement);
		Thread.sleep(500);
		return this;
	}

	public FacilityPage clickOnEditFacilitySettings() throws Exception {
		moveFacilitySettingsSectionIntoView();
		
		WebElement facilitySettingsViewSectionEditButtonElement = webOp.driver().findElement(By.xpath(facilitySettingsViewSectionXpath + editSectionButton));
		facilitySettingsViewSectionEditButtonElement.click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfElementLocated(facilitySettingsEditSectionLocator));
		return this;
	}
	
	public FacilityPage editFacilitySettingsDetails() throws Exception {
		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");
		if(editFacilitySection != null) {
			EditFacilitySettings facilitySettings = editFacilitySection.getEditFacilitySettings();
			if(facilitySettings!=null) {
				if(facilitySettings.getTimeZone() != null) {
					Select stateDropDown = new Select(webOp.driver().findElement(editTimeZoneLocator));
					stateDropDown.selectByValue(timeZoneOptionsValuePrefix+facilitySettings.getTimeZone());
				}
				if(facilitySettings.isApplyDST() != null) {
					WebElement applyDSTCheckBox = webOp.driver().findElement(editApplyDSTCheckboxLocator);
					boolean isChecked = applyDSTCheckBox.isSelected();
					if(isChecked != facilitySettings.isApplyDST().booleanValue()) {
						webOp.driver().findElement(editApplyDSTSwitcheryLocator).click();
					}
				}
				if(facilitySettings.getIdleLogoutTime() != null) {
					WebElement idleTimeoutInput = webOp.driver().findElement(editIdleTimeOut);
					idleTimeoutInput.clear();
					idleTimeoutInput.sendKeys(Integer.toString(facilitySettings.getIdleLogoutTime()));
				}
				if(facilitySettings.getFtiNumber() != null) {
					WebElement ftiNumberInput = webOp.driver().findElement(editFtiNumber);
					ftiNumberInput.clear();
					ftiNumberInput.sendKeys(facilitySettings.getFtiNumber());
				}
				webOp.driver().findElement(By.xpath(facilitySettingsEditSectionXpath + saveSectionButton)).click();
				webOp.waitTillAllCallsDoneUsingJS();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
				wait.until(ExpectedConditions.visibilityOfElementLocated(facilitySettingsViewSectionLocator));
			}
		}
		cancelEditFacilitySettingsDetailsIfNeeded();
		return this;
	}
	
	public FacilityPage verifyModifiedFacilitySettingsDetails() {
		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");
		if(editFacilitySection != null) {
			EditFacilitySettings facilitySettings = editFacilitySection.getEditFacilitySettings();
			if(facilitySettings !=null) {
				WebElement facilitySettingsViewElement = webOp.driver().findElement(facilitySettingsViewSectionLocator);
				if(facilitySettings.getTimeZone() != null) {
					SupportedTimeZoneEnum tzEnum = SupportedTimeZoneEnum.valueOf(facilitySettings.getTimeZone());
					String displayedTimeZoneLabel = facilitySettingsViewElement.findElement(viewTimeZoneLocator).getText();
					Assert.assertEquals(displayedTimeZoneLabel, tzEnum.getLabel() ,"Time zone is not displayed correctly");
					if(!tzEnum.getLabel().equals(displayedTimeZoneLabel)){
						LOGGER.error("timeZone not correct expected={}, actual={}", new Object[]{tzEnum.getLabel(), displayedTimeZoneLabel});
						webOp.getSoftAssert().fail("Time zone is not displayed correctly");
					}
					FacilityService.getFacility().setTimeZone(facilitySettings.getTimeZone());
				}
				if(facilitySettings.isApplyDST()!=null) {
					String displayedDST = facilitySettingsViewElement.findElement(viewDSTLocator).getText();
					String expectedDisplayedDST;
					if(facilitySettings.isApplyDST().booleanValue() == Boolean.FALSE) {
						expectedDisplayedDST = "No";
					} else {
						expectedDisplayedDST = "Yes";
					}
					if(!expectedDisplayedDST.equals(displayedDST)) {
						LOGGER.error("dst not correct expected={}, actual={}", new Object[]{expectedDisplayedDST, displayedDST});
						webOp.getSoftAssert().fail("Apply dst is not displayed correctly");
					}
					FacilityService.getFacility().setApplyDST(facilitySettings.isApplyDST());
				}
				if(facilitySettings.getIdleLogoutTime()!=null) {
					String displayedTime = facilitySettingsViewElement.findElement(viewIdleLogoutTimeLocator).getText();
					String expectedDisplayedTime = facilitySettings.getIdleLogoutTime() + " minutes";
					if(!expectedDisplayedTime.equals(displayedTime)) {
						LOGGER.error("idleTimeoutTime not correct expected={}, actual={}", new Object[]{expectedDisplayedTime, displayedTime});
						webOp.getSoftAssert().fail("Apply idleTimeoutTime is not displayed correctly");
					}
					FacilityService.getFacility().setIdleLogoutTime(facilitySettings.getIdleLogoutTime());
				}

				if(facilitySettings.getFtiNumber()!=null) {
					String displayedFtiNumber = facilitySettingsViewElement.findElement(viewFtiNumberLocator).getText();
					String expectedDisplayedFtiNumber = facilitySettings.getFtiNumber();
					if(!expectedDisplayedFtiNumber.equals(displayedFtiNumber)) {
						LOGGER.error("ftiNumber not correct expected={}, actual={}", new Object[]{expectedDisplayedFtiNumber, displayedFtiNumber});
						webOp.getSoftAssert().fail("Apply ftiNumber is not displayed correctly");
					}
					FacilityService.getFacility().setFtiNumber(facilitySettings.getFtiNumber());
				}

			}
		}
		return this;
	}
	
	public FacilityPage verifyTimeZoneDropdown() throws Exception {
		moveFacilitySettingsSectionIntoView();
		Select timeZoneDropDown = new Select(webOp.driver().findElement(editTimeZoneLocator));
		List<WebElement> timeZoneDropDownOptions = timeZoneDropDown.getOptions();
		int totalOptionsFound = 0;
		for(WebElement timeZoneDropDownOption: timeZoneDropDownOptions) {
			String optionValue = timeZoneDropDownOption.getAttribute("value");
			optionValue = optionValue.substring(timeZoneOptionsValuePrefix.length());
			if(EnumLookUpUtil.lookup(SupportedTimeZoneEnum.class, optionValue) == null) {
				LOGGER.error("timeZone dropdown option not found={}", optionValue);
				webOp.getSoftAssert().fail("timeZone dropdown option is not found");
			} else {
				totalOptionsFound++;
			}
		}
		if(totalOptionsFound != SupportedTimeZoneEnum.values().length) {
			LOGGER.error("less timeZone dropdown options expected={}, actual={}", new Object[]{SupportedTimeZoneEnum.values().length, totalOptionsFound});
			webOp.getSoftAssert().fail("all timeZone dropdown options not found");
		}
		cancelEditFacilitySettingsDetailsIfNeeded();
		return this;
	}
	
	public FacilityPage cancelEditFacilitySettingsDetailsIfNeeded() throws Exception {
		By cancelEditFacilitySettingsButtonLocator = By.xpath(facilitySettingsEditSectionXpath + cancelSectionButton);
		if(HelperUtility.isElementAbsent(webOp, cancelEditFacilitySettingsButtonLocator)){
			return this;
		}
		WebElement cancelEditFacilitySettingsButton = webOp.driver().findElement(cancelEditFacilitySettingsButtonLocator);
		if(cancelEditFacilitySettingsButton.isDisplayed()) {
			cancelEditFacilitySettingsButton.click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.visibilityOfElementLocated(facilitySettingsViewSectionLocator));
		}
		return this;
	}
	
	
	public FacilityPage editFacilitySettingsNegativeScenario() throws Exception {
		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");
		if(editFacilitySection != null) {
			EditFacilitySettings editFacilitySettings = editFacilitySection.getEditFacilitySettings();
			if(editFacilitySettings!=null) {
				boolean isInputFieldInvalid = false;
				if(editFacilitySettings.getIdleLogoutTime() != null) {
					WebElement idleTimeOutElement = webOp.driver().findElement(editIdleTimeOut);
					idleTimeOutElement.clear();
					idleTimeOutElement.sendKeys(Integer.toString(editFacilitySettings.getIdleLogoutTime()));
					if(HelperUtility.isInputFieldInvalid(idleTimeOutElement)){
						isInputFieldInvalid = true;
					}
				}
				if(isInputFieldInvalid == false) {
					LOGGER.error("no field found invalid in editFacilitySettings");
					webOp.getSoftAssert().fail("No field was found invalid in editFacilitySettings");
				}
			}
		}
		return this;
	}
	
	public FacilityPage verifyIdleLogoutTimeModalOpen () throws InterruptedException{
		Thread.sleep(65000);
		if (!HelperUtility.isElementPresent(webOp, modalLocator)) {
			LOGGER.error("Logout Modal is not visible");
			webOp.getSoftAssert().fail();
		}
		return this;
	}

	public FacilityPage  resetLogoutTimeOnAnyAction() throws InterruptedException{
		Thread.sleep(35000);
		webOp.driver().findElement(viewFacilityName).click();
		Thread.sleep(30000);
		if (!HelperUtility.isElementPresent(webOp, modalLocator)) {
			Assert.assertEquals(!HelperUtility.isElementPresent(webOp, modalLocator),true,"Auto Logout Reset succesfully");
			webOp.driver().findElement(viewFacilityName).click();
		};
		return this;		
	}


	public FacilityPage  clickOnContinueToRemainLoggedIn(){
		webOp.driver().findElement(continueLogin).click();
		String url = webOp.driver().getCurrentUrl(); 
		if(!url.contains(AppPage.FcilityPage.getUrlContains(hMap))) {
			LOGGER.error("Continue login from Logout modal failed");
			webOp.getSoftAssert().fail();
		} else {
			Assert.assertEquals(url.contains("facility-view"),true,"login continued successfully");
		} 
		return this;
		
	}

	public FacilityPage  signoutOnClickOfLogoutModalSignout() throws InterruptedException{		
		webOp.driver().findElement(signoutLink).click();
		wait.until(ExpectedConditions.urlContains("/login"));
		String url = webOp.driver().getCurrentUrl();		
		if(!url.contains(AppPage.LoginPage.getUrlContains(hMap))) {
			LOGGER.error("auto signout failed");
			webOp.getSoftAssert().fail();
		} else {
			Assert.assertEquals(url.contains("login"),true,"Signout form the app by autologout Modal");
		} 
		return this;
		
	}

	public FacilityPage  autoSignOutAfterModalOpen(){
		try {
			Thread.sleep(121000);
			String url = webOp.driver().getCurrentUrl(); 
			if(url.contains("login")) {
				Assert.assertEquals(url.contains("login"),true,"Signout form the app by autologout Modal");
			} else {
				LOGGER.error("auto signout failed");
				webOp.getSoftAssert().fail();
			} 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;		
	}

}
