package com.bravadohealth.keystone.pageObjects;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.Login;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class VerifyEmailPage extends AbstractPage{
	By emailAddressLocator=By.cssSelector("input#emailInputId");
	By submitButtonLocator=By.xpath("//button[input[@type='submit']]");

	public VerifyEmailPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	public VerifyEmailPage verifyEmailAddress() throws Exception{
		Login loginDetails = dataProvider.getPageData("login");
		String userName=loginDetails.getUserName();
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(emailAddressLocator).clear();
		webOp.driver().findElement(emailAddressLocator).sendKeys(userName);
		if(!userName.contains("bravadohealth")){
			Assert.assertTrue(userName.contains("bravadohealth"));
			return this;
		}
		webOp.driver().findElement(submitButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		/*String successfulMailSentNotification="We sent you an email";
		String MailSentNotification=webOp.driver().findElement(By.cssSelector("h1.header")).getText();
		Assert.assertEquals(MailSentNotification, successfulMailSentNotification, "email to the corresponding mail address is sent successfully");*/
		//assertion to check correct message
		return this;
		}

}
