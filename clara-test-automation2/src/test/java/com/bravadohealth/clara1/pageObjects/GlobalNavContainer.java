package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.pageObjects.locators.NavLocators;
import com.bravadohealth.pageObjects.AbstractPage;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class GlobalNavContainer  extends AbstractPage implements LoaderLocators, NavLocators {
	private By globlNavContainerLocator=By.xpath("//nav[@id='globalnav']");
	
	public GlobalNavContainer(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public void logout() throws Exception {
		WebElement globalNavContainerElement = webOp.driver().findElement(globlNavContainerLocator);
		WebElement signOutElement = globalNavContainerElement.findElement(signOutLocator);
		signOutElement.click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.urlContains(AppPage.LoginPage.getUrlContains(hMap)));
	}
	
	public void moveToAuditTrails() throws Exception {
		WebElement globalNavContainerElement = webOp.driver().findElement(globlNavContainerLocator);
		
		if(globalNavContainerElement.findElement(accountMenuLocator).isDisplayed()) {
			globalNavContainerElement.findElement(accountMenuLocator).click();
		}
		globalNavContainerElement.findElement(AuditTrailLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.urlContains(AppPage.AuditTrails.getUrlContains(hMap)));
	}
	
	public GlobalNavContainer moveGlobalNavContainerIntoView() throws Exception {
		WebElement globalNavContainerElement = webOp.driver().findElement(globlNavContainerLocator);
		jse.executeScript("arguments[0].scrollIntoView();", globalNavContainerElement);
		Thread.sleep(500);
		return this;
	}

}
