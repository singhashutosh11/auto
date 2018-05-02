package com.bravadohealth.clara1.pageObjects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.bravadohealth.clara1.enums.PlanFrequency;
import com.bravadohealth.clara1.enums.PlanType;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraSubscription;
import com.bravadohealth.pagedataset.CustomizePlan;
import com.bravadohealth.pagedataset.SelectPlan;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class CustomizePlanPage extends AbstractPage{

	By ProCSLicenseQuantityLocator=By.cssSelector("input#proCS-license-quantity");
	By ProLicenseQuantityLocator=By.cssSelector("input#pro-license-quantity");
	By BasicLicenseQuantityLocator=By.cssSelector("input#basic-license-quantity");
	By MedicationEducationButton=By.xpath("//button[span[contains(text(),'Medication Education')]]");
	By CheckOutButton=By.cssSelector("button[value='add-to-cart']");
	By viewMonthlyPricing=By.xpath("//a[contains(text(),'View monthly pricing')]");
	public CustomizePlanPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}
	
	public void logout() throws Exception {
		HomeViewPage homeViewPage = new HomeViewPage(webOp, dataProvider);
		homeViewPage.logout();
	}
	public CustomizePlanPage selectNoOfUser(){
		  ClaraSubscription claraSubscriptionData = dataProvider.getPageData("claraSubscription");
		  CustomizePlan customizePlan=claraSubscriptionData.getCustomizePlanData();
		  SelectPlan selectPlanData = claraSubscriptionData.getSelectPlanData();
		  String planType=selectPlanData.getPlanType();
		  int proCSUser= (customizePlan.getProCSUsers()!=null)?customizePlan.getProCSUsers():0;
		  int proUser= (customizePlan.getProUsers()!=null)?customizePlan.getProUsers():0;
		  int basicUser=(customizePlan.getBasicUsers()!=null)?customizePlan.getBasicUsers():0;
		  String planFrequency = selectPlanData.getPlanFrequency();
		  if(PlanType.ProCS == PlanType.getPlanType(planType)){
		  webOp.driver().findElement(ProCSLicenseQuantityLocator).sendKeys(Integer.toString(proCSUser));
		  }
		  if(customizePlan.getProUsers()!=null){
		  webOp.driver().findElement(ProLicenseQuantityLocator).sendKeys(Integer.toString(proUser));
		  }
		  if(customizePlan.getBasicUsers()!=null){
		  // webOp.driver().findElement(BasicLicenseQuantityLocator).sendKeys(Integer.toString(basicUser));
		  }
		  jse.executeScript("window.scrollBy(0,1000)");
		  if(StringUtils.isNotEmpty(customizePlan.getMedicationEducation())){
			  webOp.driver().findElement(MedicationEducationButton).click(); 
		  }
		  
		  if(PlanFrequency.Monthly == PlanFrequency.getPlanFrequency(planFrequency)){
	            webOp.driver().findElement(viewMonthlyPricing).click();
	            //check the monthly prices for the package
	       }
		return this;
	}
	
	public CreateClaraIdPage checkOut() {
		webOp.driver().findElement(CheckOutButton).click();
	    return new CreateClaraIdPage(webOp, dataProvider);
	}
	
}
