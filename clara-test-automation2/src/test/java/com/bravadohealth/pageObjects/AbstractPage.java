package com.bravadohealth.pageObjects;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;

import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.ClaraWebDriver;
//import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class AbstractPage {

	public WebOperation webOp;
	public PageDataProvider dataProvider;
	protected Logger logger;
	public HashMap<String,String> hMap;
	public List<AuditAction> Alogs;
	public WebDriverWait wait;
	public JavascriptExecutor jse;
	public By loaderSign =LoaderLocators.loadingScreenLocator;

	public AbstractPage(WebOperation webOp,PageDataProvider dataProvider)
	{
		this.webOp=webOp;
		this.dataProvider = dataProvider;
		this.logger = webOp.logger();
		this.hMap=webOp.gethMap();
		this.Alogs=webOp.getAlogs();
		this.wait = new WebDriverWait(webOp.driver(), 30);
		jse= (JavascriptExecutor) ((ClaraWebDriver) webOp.driver()).getNativeDriver();
	}
	
	public boolean retryingFindClick(By by) {
		
	    boolean result = false;
	    int attempts = 0;
	    while(attempts < 2) {
	        try {
	        	webOp.driver().findElement(by).click();
	            result = true;
	            break;
	        } catch(StaleElementReferenceException e) {
	        }
	        attempts++;
	    }
	    return result;
	}

}
