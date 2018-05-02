package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.bravadohealth.clara1.enums.PlanFrequency;
import com.bravadohealth.clara1.enums.PlanType;
import com.bravadohealth.pageObjects.AbstractPage;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;
import com.bravadohealth.pagedataset.ClaraSubscription;
import com.bravadohealth.pagedataset.SelectPlan;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class SelectPlanPage extends AbstractPage{

    By monthlyPlanButtonLocator = By.cssSelector("button.price-toggle-monthly");
    By annualPlanButtonLocator = By.cssSelector("button.price-toggle-annual");
    By proPlanButtonLocator = By.xpath("//div[h3[text()='Clara Pro']]//button");
    By proCSPlanButtonLocator = By.xpath("//div[h3[contains(text(),'Clara Pro CS')]]//button");
	public SelectPlanPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	
	public void logout() throws Exception {
		HomeViewPage homeViewPage = new HomeViewPage(webOp, dataProvider);
		homeViewPage.logout();
	}
	
	public CustomizePlanPage selectPlan() throws Exception { 
        wait.until(ExpectedConditions.urlContains("/meet-clara"));
        webOp.waitTillAllCallsDoneUsingJS();
        ClaraSubscription claraSubscriptionData = dataProvider.getPageData("claraSubscription");
        SelectPlan selectPlanData = claraSubscriptionData.getSelectPlanData();
       // String planFrequency = selectPlanData.getPlanFrequency();
        String planType = selectPlanData.getPlanType();
       /* if(PlanFrequency.Annual == PlanFrequency.getPlanFrequency(planFrequency)){
            webOp.driver().findElement(annualPlanButtonLocator).click();
        } else{
            webOp.driver().findElement(monthlyPlanButtonLocator).click();
        }*/
        jse.executeScript("window.scrollBy(0,1000)");
        if(PlanType.Pro == PlanType.getPlanType(planType)){
            webOp.driver().findElement(proPlanButtonLocator).click();
        } else{
            webOp.driver().findElement(proCSPlanButtonLocator).click();
        }
        return new CustomizePlanPage(webOp, dataProvider);
	}
	
}
