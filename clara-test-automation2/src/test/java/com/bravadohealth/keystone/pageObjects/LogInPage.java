package com.bravadohealth.keystone.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.Login;

import trial.keyStone.automation.ExcelData;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class LogInPage extends AbstractPage{

	By userNameLocator=By.cssSelector("input#keystoneId");
	By passWordLocator=By.cssSelector("input#pwd");
	By signInLocator=By.cssSelector("button#sign-in");
	By signInError=By.cssSelector("div[class^='signin-error']");
	By forgotPasswordLink=By.xpath("//a[contains(text(),'Forgot password?')]");

	public LogInPage(WebOperation webOp,PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}

	public HomePage logIn() throws InterruptedException{
		Login loginDetails = dataProvider.getPageData("login");
		String userName=loginDetails.getUserName();

		String passWord=loginDetails.getPassword();
		wait.until(ExpectedConditions.presenceOfElementLocated(userNameLocator));
		webOp.driver().findElement(userNameLocator).sendKeys(""+userName+"");
		hMap.put("userName", userName);
		hMap.put("logInPassword", passWord);
		webOp.driver().findElement(passWordLocator).sendKeys(""+passWord+"");
		webOp.driver().findElement(signInLocator).click();
		/*	boolean condition=webOp.driver().findElement(signInError).getText().contains("Your Bravado Health email or password was incorrect.");
		Assert.assertFalse(condition, "The entered credential is not correct");*/
		return new HomePage(webOp,dataProvider);
	}
	public HomePage InvalidlogIn() throws InterruptedException{
		Login loginDetails = dataProvider.getPageData("login");
		String userName=loginDetails.getUserName();
		String passWord=loginDetails.getPassword();
		wait.until(ExpectedConditions.presenceOfElementLocated(userNameLocator));
		webOp.driver().findElement(userNameLocator).sendKeys(""+userName+"");
		webOp.driver().findElement(passWordLocator).sendKeys(""+passWord+"");
		webOp.driver().findElement(signInLocator).click();
		boolean condition=webOp.driver().findElement(signInError).getText().contains("Your Bravado Health email or password was incorrect.");
		Assert.assertTrue(condition, "The entered credential is not correct");
		return new HomePage(webOp,dataProvider);
	}
	public VerifyEmailPage forgotPassword() throws Exception{
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(forgotPasswordLink).click();
		return new VerifyEmailPage(webOp, dataProvider);
		} 

}
