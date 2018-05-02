package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.bravadohealth.pageObjects.AbstractPage;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class PlanCheckOutPage extends AbstractPage {
	
	By submitButton=By.cssSelector("button[type='submit']");
	By LicenseTermModal=By.id("license-terms");
	By acceptTerms=By.id("user-accept-terms");
	
	By checkoutFrame=By.name("stripe_checkout_app");
	By stripeModal=By.cssSelector("div[class='Modal-animationWrapper']");
	By cardNumber=By.xpath("//input[@placeholder='Card number']");
	By expireDate=By.xpath("//input[@placeholder='MM / YY']");
	By CVC_Number=By.xpath("//input[@placeholder='CVC']");
	By placeOrderNow=By.xpath("//button[span//span[contains(text(),'Place Order Now')]]");
	
	By getStartedButton=By.xpath("//button[a[contains(text(),'Get Started')]]");

	public PlanCheckOutPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public PlanCheckOutPage ConfirmOrder() throws Exception {
		wait.until(ExpectedConditions.urlContains("/checkout"));
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(submitButton).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfElementLocated(LicenseTermModal));
		if(webOp.driver().findElement(LicenseTermModal).isDisplayed()) {
			WebElement acceptTermsButton= webOp.driver().findElement(acceptTerms);
			new Actions(webOp.driver()).moveToElement(acceptTermsButton).click().perform();
		}
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderSign));
		//WebElement iFrame = webOp.driver().findElement(checkoutFrame);
		try
		{
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(checkoutFrame));
		}
		catch(Exception e) {
			logger.info("The frame is not loaded");
		}
		//webOp.driver().switchTo().frame(iFrame);
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfElementLocated((cardNumber)));
		if(webOp.isElementPresent(stripeModal)) {
			webOp.driver().findElement(cardNumber).sendKeys("4242424242424242");
			webOp.driver().findElement(expireDate).sendKeys("1224");
			webOp.driver().findElement(CVC_Number).sendKeys("012");
			webOp.driver().findElement(placeOrderNow).click();
			logger.info("STripe checkout");
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderSign));
		}
		webOp.waitTillAllCallsDoneUsingJS();
		return this;
	}

	public SetupFacilityPage getStarted() {
		wait.until(ExpectedConditions.urlContains("/get-started"));
		webOp.driver().findElement(getStartedButton).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderSign));
		return new SetupFacilityPage(webOp, dataProvider);
	}
	
}
