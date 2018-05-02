package com.bravadohealth.clara1.pageObjects;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ForgotCredential;
import com.bravadohealth.pagedataset.Login;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class VerifyClaraAppEmailPage extends AbstractPage {

	By verifyEmailInputField = By.cssSelector("input[type='text']");
	By submitButtonLocator = By.xpath("//button[a[contains(text(),continue)]]");
	By forgotClaraIdLink = By.cssSelector("a.button-compact");

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public VerifyClaraAppEmailPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public VerifyClaraAppEmailPage verifyEmailAddressInClaraApp() throws Exception {
		ForgotCredential forgotCredential = dataProvider.getPageData("forgotCredential");
		if (forgotCredential.getForgotPassword() != null) {
			String userName = forgotCredential.getForgotPassword().getClaraUserId();
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(verifyEmailInputField).sendKeys(userName);
			webOp.driver().findElement(submitButtonLocator).click();
			Thread.sleep(2000);
			boolean emailFormatCheck = validate(userName);
			Assert.assertTrue(emailFormatCheck, "The emailId format is incorrect");
			/*
			 * } }
			 */
		}
		webOp.waitTillAllCallsDoneUsingJS();
		String successfulMailSentNotification = "We sent you an email";
		Thread.sleep(2000);
		String MailSentNotification = webOp.driver().findElement(By.cssSelector("h2.subtitle")).getText();
		Assert.assertEquals(MailSentNotification, successfulMailSentNotification, "We sent you an email");
		// assertion to check correct message
		return this;
	}

	public VerifyClaraAppEmailPage verifyInvalidEmailAddressInClaraApp() throws Exception {
		ForgotCredential forgotCredential = dataProvider.getPageData("forgotCredential");
		if (forgotCredential.getForgotPassword() != null) {
			String userName = forgotCredential.getForgotPassword().getClaraUserId();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.presenceOfElementLocated(verifyEmailInputField));
			webOp.driver().findElement(verifyEmailInputField).sendKeys(userName);
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(submitButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			Thread.sleep(2000);
			boolean emailFormatCheck = validate(userName);
			if (!emailFormatCheck) {
				Assert.assertFalse(emailFormatCheck, "The emailId format is incorrect");
			}
		}
		webOp.waitTillAllCallsDoneUsingJS();
		return this;
	}

	public recoverClaraIdPage forgotClaraId() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(forgotClaraIdLink).click();
		return new recoverClaraIdPage(webOp, dataProvider);
	}

	public boolean validate(String hex) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}

}
