package com.bravadohealth.clara1.pageObjects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.bravadohealth.clara1.pageObjects.constants.ApplicationConstatnts;
import com.bravadohealth.clara1.services.FacilityService;
import com.bravadohealth.clara1.services.UserService;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.clara1.utility.NameUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.ClaraWebDriver;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class CreateClaraIdPage extends AbstractPage{

	By emailAddressLocator=By.cssSelector("input[name='claraID']");
	By passwordLocator=By.cssSelector("input[placeholder='password']");
	By confirmPasswordLocator=By.cssSelector("input[placeholder='confirm password']");
	By firstNameLocator=By.cssSelector("input[placeholder='first name']");
	By LastNameLocator=By.cssSelector("input[placeholder='last name']");
	By BirthDayLocator=By.cssSelector("input[placeholder='birthday']");
	By ContinueButton=By.xpath("//button[contains(text(),'Continue')]");
	By OTPinputLocator=By.xpath("//div[@class='fieldgroup security-code']/div[1]/input");
	By resendOTP=By.xpath("//button[contains(text(),'Send a new code')]");
	By continueChangeMail=By.cssSelector("button[ng-click*='verifyOtpCtrl.submit']");
	By InvalidOTP=By.cssSelector("div[class*='error-wrapper']");
	By cancelChangeMail=By.cssSelector("button[ng-click*='verifyOtpCtrl.cancel']");
	By ExistingEmailError=By.xpath("//div[contains(text(),'Email address already exists.')]");
	
	private static final String EMAIL_ADDRESS_PREFIX = "aaccolitetest";
	private static final String EMAIL_ADDRESS_DOMAIN = "@gmail.com";

	public CreateClaraIdPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}

	public PlanCheckOutPage createClaraId() throws Exception {
		ClaraUserDetails claraUserDetails=dataProvider.getPageData("claraUserDetails");
		String emailAddress="";

		String randomString=NameUtility.generateRandom();
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("ssMs");
		String datetime = ft.format(dNow);

		if(claraUserDetails.getEmail() != null) {
			emailAddress= EMAIL_ADDRESS_PREFIX + "+" + datetime + EMAIL_ADDRESS_DOMAIN;
		}
		webOp.driver().findElement(emailAddressLocator).sendKeys(emailAddress);
		webOp.driver().findElement(passwordLocator).sendKeys(claraUserDetails.getPassword());
		webOp.driver().findElement(confirmPasswordLocator).sendKeys(claraUserDetails.getPassword());
		webOp.driver().findElement(firstNameLocator).sendKeys(claraUserDetails.getFirstname());
		webOp.driver().findElement(LastNameLocator).sendKeys(claraUserDetails.getLastName());
		webOp.driver().findElement(BirthDayLocator).sendKeys(claraUserDetails.getBirthDay());
		webOp.driver().findElement(ContinueButton).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderSign));
		webOp.waitTillAllCallsDoneUsingJS();
		while(webOp.isElementPresent(ExistingEmailError)) 
		{
			//n = rand.nextInt(50-0) + 50;
			emailAddress= EMAIL_ADDRESS_PREFIX + "+" + datetime + EMAIL_ADDRESS_DOMAIN;;
			webOp.driver().findElement(emailAddressLocator).clear();
			webOp.driver().findElement(emailAddressLocator).sendKeys(emailAddress);
			webOp.driver().findElement(ContinueButton).click();
		}
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(OTPinputLocator).sendKeys("000000");
		webOp.driver().findElement(continueChangeMail).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderSign));
		webOp.waitTillAllCallsDoneUsingJS();
		FacilityService.setDateTimeAtSelectFacility();
		Alogs.add(AuditAction.USER_SELECTED_FACILITY);
		if(webOp.isElementPresent(InvalidOTP)) {
			String errorMessage = webOp.driver().findElement(InvalidOTP).getText();
			//Assert.assertEquals(errorMessage, "Email change token is invalid.","Error message displayed for invalid OTP");
			webOp.driver().findElement(By.xpath("resendOTP")).click();
			webOp.driver().findElement(OTPinputLocator).sendKeys("000000");
			//webOp.driver().findElement(cancelChangeMail).click();	
		}
		claraUserDetails.setEmail(emailAddress);
		UserService.setCurrentUser(claraUserDetails);
		String body = enrollUserForDuo(emailAddress);
		return new PlanCheckOutPage(webOp, dataProvider);
	}

	private String enrollUserForDuo(String emailAddress) throws Exception {
		String url = HelperUtility.getFormattedText(ApplicationConstatnts.DuoProxyUrls.duoAuth, emailAddress);
		return HelperUtility.openUrlInNewTabAndThenCloseTab(webOp, wait, jse, url);
	}

}
