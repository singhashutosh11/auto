package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.PostCompetionAccountOperation;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class DoneWithAddedLicensePage extends AbstractPage{ 
	
	By getStartedButtonLocator=By.xpath("//button[a[text()='Get Started Now']]");
	By InviteUserButtonLocator=By.xpath("//button[a[text()='Invite Users']]");

	public DoneWithAddedLicensePage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	
	public AbstractPage postCompletionAction() {
		ClaraUserDetails claraUserDetails=dataProvider.getPageData("claraUserDetails");
		PostCompetionAccountOperation pcao=claraUserDetails.getPostCompetionAccountOperation();
		if(pcao.isGetStarted()!=null &&pcao.isGetStarted().equals(true)){
			webOp.driver().findElement(getStartedButtonLocator).click();
			HelperUtility.checkUrlChangesTo(webOp, AppPage.SelectPatient.getUrlContains(hMap));
			return new HomeViewPage(webOp, dataProvider);
		}
		else if(pcao.isAddUser()!=null && pcao.isAddUser().booleanValue()==true){
			webOp.driver().findElement(InviteUserButtonLocator).click();
			HelperUtility.checkUrlChangesTo(webOp, "invite-users.php");
			return this;
		}
		return new AbstractPage(webOp, dataProvider);
	}
	

}
