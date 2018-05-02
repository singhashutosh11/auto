package com.bravadohealth.clara1.pageObjects;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.Choose;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.enums.SupportedTimeZoneEnum;
import com.bravadohealth.clara1.pageObjects.constants.ApplicationConstatnts;
import com.bravadohealth.clara1.pageObjects.locators.FacilityCompleteSectionLocators;
import com.bravadohealth.clara1.services.FacilityService;
import com.bravadohealth.clara1.services.UserService;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.clara1.utility.NameUtility;
import com.bravadohealth.clara1.utility.UserUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.Facility;
import com.bravadohealth.pagedataset.Nominator;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class SetupFacilityPage extends AbstractPage implements FacilityCompleteSectionLocators{

	By facilityNameLocator=By.cssSelector("input[name='facilityName']");
	By zipCodeLocator=By.cssSelector("input[name='zip']");
	By phoneNumberLocator=By.cssSelector("input[name='phone']");
	By faxLocator=By.cssSelector("input[name='fax']");

	By facilityNPILocator=By.cssSelector("input[name='npiNumber']");
	By facilityDEALocator=By.cssSelector("input[name='deaNumber']");
	By ftiLocator=By.cssSelector("input[name='ftiNumber']");

	By timeZoneLocator=By.xpath("//select[@name='timeZone']");
	By dstLocator=By.xpath("//label[input[@name='nameApplyDST']]/span");
	
	By validateButton=By.xpath("//button[a[contains(text(),'Validate')]]");


	public SetupFacilityPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public ChooseLicensePage provideFacilityDetails() {
		wait.until(ExpectedConditions.urlContains(AppPage.SetupFacility.getUrlContains(hMap)));
		Facility facility=dataProvider.getPageData("facility");

		String facilityName=NameUtility.generateRandom();
		String facilityAddress=facility.getAddress();
		String City=facility.getCity();
		String state=facility.getState();

		String faciltyNPI=facility.getFacilityNPI();
		String facilityDEA=facility.getFacilityDEA();
		String facility_fti=facility.getFtiNumber();

		int faciltyZipcode=facility.getZipCode();
		long facilityPhone=facility.getPhone();
		long facility_fax=facility.getFaxNumber();
		
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yMds");
		String datetime = ft.format(dNow);
		
		Nominator nominator = facility.getNominator();
		String nominatorFirstName=nominator.getFirstName();
		String nominatorLastName=nominator.getLastName();
		String nominatorMail = nominator.getEmailAddress() + datetime + ApplicationConstatnts.accoliteIndiaEmailSuffix;
		
		Nominator altNominator = facility.getAltNominator();
		String altNominatorFirstName=altNominator.getFirstName();
		String altNominatorLastName=altNominator.getLastName();
		String altNominatorMail = altNominator.getEmailAddress() + datetime + ApplicationConstatnts.accoliteIndiaEmailSuffix;

		String timeZone=SupportedTimeZoneEnum.valueOf(facility.getTimeZone()).getLabel();
		Boolean dstValue=facility.isApplyDST();

		webOp.driver().findElement(facilityNameLocator).sendKeys(facilityName);
		webOp.driver().findElement(facilityAddressLocator).sendKeys(facilityAddress);
		webOp.driver().findElement(cityLocator).sendKeys(City);
		Select stateDropDown = new Select(webOp.driver().findElement(stateLocator));
		stateDropDown.selectByVisibleText(state);
		webOp.driver().findElement(zipCodeLocator).sendKeys(""+faciltyZipcode+"");
		webOp.driver().findElement(phoneNumberLocator).sendKeys(""+facilityPhone+"");
		webOp.driver().findElement(faxLocator).sendKeys(""+facility_fax+"");

		webOp.driver().findElement(facilityNPILocator).sendKeys(faciltyNPI);
		webOp.driver().findElement(facilityDEALocator).sendKeys(facilityDEA);
		webOp.driver().findElement(ftiLocator).sendKeys(facility_fti);
		
		webOp.driver().findElement(NominatorFirstNameLocator).sendKeys(nominatorFirstName);
		webOp.driver().findElement(NominatorLastNameLocator).sendKeys(nominatorLastName);
		if(StringUtils.equalsIgnoreCase(webOp.getParameter("isFacilityOwnerNominator"),"TRUE"))
		{
			webOp.driver().findElement(NominatorMailAddress).sendKeys(UserService.getCurrentUser().getEmail());
			nominator.setEmailAddress(UserService.getCurrentUser().getEmail());
		}
		else
		{
			webOp.driver().findElement(NominatorMailAddress).sendKeys(nominatorMail);
			nominator.setEmailAddress(nominatorMail);
		}
		webOp.driver().findElement(AltNominatorFirstNameLocator).sendKeys(altNominatorFirstName);
		webOp.driver().findElement(AltNominatorLastNameLocator).sendKeys(altNominatorLastName);
		webOp.driver().findElement(AltNominatorMailAddress).sendKeys(altNominatorMail);


		Select timeZoneDown = new Select(webOp.driver().findElement(timeZoneLocator));
		timeZoneDown.selectByVisibleText(timeZone);
		if(dstValue != null && dstValue.booleanValue() != true) {
			webOp.driver().findElement(dstLocator).click();
		}
		webOp.driver().findElement(validateButton).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderSign));
		
		facility.setFacilityName(facilityName);
		facility.setFacilityOwner(UserService.getCurrentUser().getEmail());
		altNominator.setEmailAddress(altNominatorMail);
		FacilityService.setFacility(facility);
		
		Alogs.add(AuditAction.FACILITY_CREATED);
		Alogs.add(AuditAction.DESIGNATE_NOMINATOR);
		Alogs.add(AuditAction.DESIGNATE_ALTERNATE_NOMINATOR);
		return new ChooseLicensePage(webOp, dataProvider);
	}

	public SetupFacilityPage providerFacilityDetailsWithSameNominators() {
		wait.until(ExpectedConditions.urlContains(AppPage.SetupFacility.getUrlContains(hMap)));
		Facility facility=dataProvider.getPageData("facility");
		webOp.driver().findElement(facilityNameLocator).sendKeys(facility.getFacilityName());
		webOp.driver().findElement(facilityAddressLocator).sendKeys(facility.getAddress());
		webOp.driver().findElement(cityLocator).sendKeys(facility.getCity());
		Select stateDropDown = new Select(webOp.driver().findElement(stateLocator));
		stateDropDown.selectByVisibleText(facility.getState());
		webOp.driver().findElement(zipCodeLocator).sendKeys(""+facility.getZipCode()+"");
		webOp.driver().findElement(phoneNumberLocator).sendKeys(""+facility.getPhone()+"");
		webOp.driver().findElement(faxLocator).sendKeys(""+facility.getFaxNumber()+"");

		webOp.driver().findElement(facilityNPILocator).sendKeys(facility.getFacilityNPI());
		webOp.driver().findElement(facilityDEALocator).sendKeys(facility.getFacilityDEA());
		webOp.driver().findElement(ftiLocator).sendKeys(facility.getFtiNumber());
		
		webOp.driver().findElement(NominatorFirstNameLocator).sendKeys(facility.getNominator().getFirstName());
		webOp.driver().findElement(NominatorLastNameLocator).sendKeys(facility.getNominator().getLastName());
		webOp.driver().findElement(NominatorMailAddress).sendKeys(facility.getNominator().getEmailAddress());
		webOp.driver().findElement(AltNominatorFirstNameLocator).sendKeys(facility.getAltNominator().getFirstName());
		webOp.driver().findElement(AltNominatorLastNameLocator).sendKeys(facility.getAltNominator().getLastName());
		webOp.driver().findElement(AltNominatorMailAddress).sendKeys(facility.getAltNominator().getEmailAddress());
		if(StringUtils.equalsIgnoreCase(webOp.getParameter("errorField"), "altNomEmailAddress"))
		{
			Assert.assertTrue(webOp.driver().findElement(By.xpath("//div[input[@name='alternateNominatorEmail']][div[contains(@class,'error-msg')]]")).isDisplayed(), "Alternate Nominator Email address field was not found invalid during facility setup");
		}
		return this;
	}



}
