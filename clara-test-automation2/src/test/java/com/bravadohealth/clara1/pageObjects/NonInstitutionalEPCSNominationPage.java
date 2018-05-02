package com.bravadohealth.clara1.pageObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.bravadohealth.clara1.pageObjects.constants.ApplicationConstatnts;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.services.FacilityService;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.Facility;
import com.bravadohealth.pagedataset.Nominator;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class NonInstitutionalEPCSNominationPage extends AbstractPage implements LoaderLocators{

	private By nomineesTableLocator = By.xpath("//table[contains(@class, 'nominees-table')]");
	private String nomineeLocator = "//tr[contains(@class, 'nominee-row')]";
	private String prescriberNameCellLocator = "/td[contains(@class, 'name')]";
	private String thankYouCellLocator = "//span[contains(@class, 'thanks-content')]";
	private String nominateButtonLocator = "//button[contains(text(), 'Nominate')]";
	private By noNomineesLeftLocator = By.xpath("//div[contains(@class, 'flex-message')]/p");
	public NonInstitutionalEPCSNominationPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}
	
	public NonInstitutionalEPCSNominationPage getAndStoreNonInstitutionalEPCSNominationUrlTokenForNominators() throws Exception {
		Facility facility=dataProvider.getPageData("facility");
		Boolean doesPrimaryNominatorNominates = Boolean.valueOf(webOp.getParameter("doesPrimaryNominatorNominates"));
		Boolean doesAlternateNominatorNominates = Boolean.valueOf(webOp.getParameter("doesAlternateNominatorNominates"));
		if(Boolean.TRUE == doesPrimaryNominatorNominates) {
			Nominator nominator = facility.getNominator();
			String nominatorMail=nominator.getEmailAddress();
			String body = getNonInstitutionalEPCSNominatorTokensSentToEmail(nominatorMail);
			FacilityService.setNominatorEmailToken(body);
			if(StringUtils.equalsIgnoreCase(webOp.getParameter("isFacilityOwnerNominator"),"TRUE"))
			{
				Assert.assertTrue(FacilityService.getNominatorEmailToken().contains("HTTP Status 500"),"User is able to self nominate");
			}
		}
		if(Boolean.TRUE == doesAlternateNominatorNominates) {
			Nominator altNominator = facility.getAltNominator();
			String altNominatorMail=altNominator.getEmailAddress();
			String body = getNonInstitutionalEPCSNominatorTokensSentToEmail(altNominatorMail);
			FacilityService.setAltNominatorEmailToken(body);
		}
		
		return this;
	}
	
	private String getNonInstitutionalEPCSNominatorTokensSentToEmail(String nominatorEmailAddress) throws Exception {
		String url = HelperUtility.getFormattedText(ApplicationConstatnts.DuoProxyUrls.nintToken, nominatorEmailAddress);
		return HelperUtility.openUrlInNewTabAndThenCloseTab(webOp, wait, jse, url);
	}

	public void nominateFacilityOwner() throws Exception {
		Facility facility = dataProvider.getPageData("facility");
		Boolean doesPrimaryNominatorNominates = Boolean.valueOf(webOp.getParameter("doesPrimaryNominatorNominates"));
		Boolean doesAlternateNominatorNominates = Boolean.valueOf(webOp.getParameter("doesAlternateNominatorNominates"));
		ClaraUserDetails claraUserDetails = dataProvider.getPageData("claraUserDetails");
		if(Boolean.TRUE == doesPrimaryNominatorNominates && Boolean.FALSE == doesAlternateNominatorNominates && !StringUtils.equalsIgnoreCase(webOp.getParameter("isFacilityOwnerNominator"),"TRUE")) {
			nominateProvidersInNewTabUsingNominatorToken(facility.getNominator(), FacilityService.getNominatorEmailToken(), Arrays.asList(claraUserDetails));
		}
		else if(Boolean.TRUE == doesAlternateNominatorNominates && Boolean.FALSE == doesPrimaryNominatorNominates) {
			nominateProvidersInNewTabUsingNominatorToken(facility.getAltNominator(), FacilityService.getAltNominatorEmailToken(), Arrays.asList(claraUserDetails));
		}
		else if(StringUtils.equalsIgnoreCase(webOp.getParameter("isFacilityOwnerNominator"),"TRUE") && Boolean.TRUE == doesAlternateNominatorNominates)
		{
			nominateProvidersInNewTabUsingNominatorToken(facility.getAltNominator(), FacilityService.getAltNominatorEmailToken(), Arrays.asList(claraUserDetails));
		}
		else if(Boolean.TRUE == doesPrimaryNominatorNominates && Boolean.TRUE == doesAlternateNominatorNominates && !StringUtils.equalsIgnoreCase(webOp.getParameter("isFacilityOwnerNominator"),"TRUE")) {
			nominateProvidersInNewTabUsingNominatorsToken(FacilityService.getNominatorEmailToken(), FacilityService.getAltNominatorEmailToken(), Arrays.asList(claraUserDetails));
		}
		
	}
	
	private void nominateProvidersInNewTabUsingNominatorToken(Nominator nominator, String nominatorToken, List<ClaraUserDetails> eleigibleUsers) throws Exception {
		String url = HelperUtility.getFormattedText(ApplicationConstatnts.nonInstitutionalEPCSNominationUrl, nominatorToken);
		jse.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String> (webOp.driver().getWindowHandles());
		webOp.driver().switchTo().window(tabs.get(1));
		
		webOp.driver().get(url);
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.visibilityOfElementLocated(nomineesTableLocator));
		
		List<WebElement> nomineeElements = webOp.driver().findElements(By.xpath(nomineeLocator));
		int totalNomineeElements = nomineeElements.size();
		while(totalNomineeElements > 0) {
			nominateFirstPrescriber();
			totalNomineeElements--;
		}
		verifyNoBodyToNominateMessage();
		webOp.driver().close();
		webOp.driver().switchTo().window(tabs.get(0));
	}
	
	private void nominateProvidersInNewTabUsingNominatorsToken(String nominatorToken, String altNominatorToken, List<ClaraUserDetails> eleigibleUsers) throws Exception {
		String currentWindow = webOp.driver().getWindowHandle();
		
		//open first tab for primary nominator
		jse.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String> (webOp.driver().getWindowHandles());
		String newTab1 = tabs.get(1);
		webOp.driver().switchTo().window(newTab1);
		String url = HelperUtility.getFormattedText(ApplicationConstatnts.nonInstitutionalEPCSNominationUrl, nominatorToken);
		webOp.driver().get(url);
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.visibilityOfElementLocated(nomineesTableLocator));
		//open second tab for secondary nominator
		jse.executeScript("window.open()");
		tabs = new ArrayList<String> (webOp.driver().getWindowHandles());
		String newTab2 = tabs.get(2);
		webOp.driver().switchTo().window(newTab2);
		url = HelperUtility.getFormattedText(ApplicationConstatnts.nonInstitutionalEPCSNominationUrl, altNominatorToken);
		webOp.driver().get(url);
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.visibilityOfElementLocated(nomineesTableLocator));
		//nominate all providers in second tab
		List<WebElement> tab2NomineeElements = webOp.driver().findElements(By.xpath(nomineeLocator));
		int tab2TotalNominees = tab2NomineeElements.size();
		while(tab2TotalNominees > 0) {
			nominateFirstPrescriber();
			tab2TotalNominees--;
		}
		verifyNoBodyToNominateMessage();
		webOp.driver().close();
		//nominate all providers in first tab
		webOp.driver().switchTo().window(newTab1);
		List<WebElement> nomineeElements = webOp.driver().findElements(By.xpath(nomineeLocator));
		int tab1TotalNominees = nomineeElements.size();
		while(tab1TotalNominees > 0) {
			boolean ableToNominate = nominateFirstPrescriber();
			Assert.assertFalse(ableToNominate,"Error: Able to renominate from Nominator, after nomination by Alternate Nominator");
			tab1TotalNominees--;
		}
		verifyNoBodyToNominateMessage();
		webOp.driver().close();

		webOp.driver().switchTo().window(currentWindow);
	}
	
	private boolean nominateFirstPrescriber() throws Exception {
		String firstPrescriberLocatorPrefix = nomineeLocator+"[1]";
		WebElement prescriberNameElement = webOp.driver().findElement(By.xpath(firstPrescriberLocatorPrefix+prescriberNameCellLocator));
		String prescriberName = prescriberNameElement.getText();
		WebElement nominateButton = webOp.driver().findElement(By.xpath(firstPrescriberLocatorPrefix+nominateButtonLocator));
		nominateButton.click();
		boolean isThankYouElementPresent = HelperUtility.isElementPresent(webOp, By.xpath(firstPrescriberLocatorPrefix+thankYouCellLocator));
		if(!isThankYouElementPresent) {
			webOp.logger().error("thank you message not shown on nominate", prescriberName);
			webOp.getSoftAssert().fail("Thank you message was not shown on clicking nominate"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		By prescriberWithNameLocator = By.xpath(nomineeLocator + "/td[contains(@class, 'name') and text()='"+prescriberName+"']");
		if(HelperUtility.isElementPresent(webOp, prescriberWithNameLocator)) {
			WebElement prescriberWithNameElement = webOp.driver().findElement(prescriberWithNameLocator);
			if(prescriberWithNameElement.isDisplayed()) {
				webOp.logger().error("prescriber not removed prescriber name = {}", prescriberName);
				webOp.getSoftAssert().fail("Prescriber was not removed on click of nominate"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			}
		}
		boolean nominatedSuccessfully;
		if(HelperUtility.isToasterDisplayed(webOp)){
			nominatedSuccessfully = false;
		} else {
			nominatedSuccessfully = true;
			Alogs.add(AuditAction.USER_NOMINATED_FOR_EPCS);
		}
		return nominatedSuccessfully;
	}
	
	private void verifyNoBodyToNominateMessage() throws Exception {
		Thread.sleep(600);
		if(HelperUtility.isElementAbsent(webOp, noNomineesLeftLocator)) {
			webOp.logger().error("no nominee left message not shown");
			webOp.getSoftAssert().fail("Nobody to nominate message is not shown after nominating all"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		
	}
	
}
