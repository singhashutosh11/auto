package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ForgotCredential;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class recoverClaraIdPage extends AbstractPage{

	public recoverClaraIdPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	By firstNameLocator=By.cssSelector("input[name=firstName]");
	By lastNameLocator=By.cssSelector("input[name=lastName]");
	//By emailAddressLocator=By.cssSelector("input[name=claraID]");
	By emailAddressLocator = By.xpath("//input[@name='email']");
	//By continueButtonLocator=By.cssSelector("button[type=button]>a");
	By continueButtonLocator = By.xpath("//button[a[contains(text(),continue)]]");
	By claraIdVerifySubtitleLocator=By.cssSelector("h2[class=subtitle]");

	public recoverClaraIdPage findClaraId() throws Exception{
		ForgotCredential forgotCredential = dataProvider.getPageData("forgotCredential");
		String firstName=forgotCredential.getForgotClaraID().getFirstName();
		String lastName=forgotCredential.getForgotClaraID().getLastname();
		String emailAddress=forgotCredential.getForgotClaraID().getEmailAddress();
		wait.until(ExpectedConditions.presenceOfElementLocated(firstNameLocator));
		webOp.driver().findElement(firstNameLocator).sendKeys(firstName);
		webOp.driver().findElement(lastNameLocator).sendKeys(lastName);
		webOp.driver().findElement(emailAddressLocator).sendKeys(emailAddress);

		Thread.sleep(500);
		
		webOp.driver().findElement(continueButtonLocator).click();
		
		webOp.waitTillAllCallsDoneUsingJS();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(claraIdVerifySubtitleLocator));
		String actualMessage=webOp.driver().findElement(claraIdVerifySubtitleLocator).getText();
		Assert.assertEquals(actualMessage, "Clara ID found","The clara Id was identified");
		return this;
	}
}
