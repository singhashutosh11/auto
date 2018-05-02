package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;

import com.bravadohealth.pageObjects.AbstractPage;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class UsersPage extends AbstractPage {

	public UsersPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}
	
	By AllUsersInUsersList = By.cssSelector("td.user-control.group-name a");
	
	public EditUserDetailsPage selectAUserFromUserList() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElements(AllUsersInUsersList).get(0).click();
		webOp.waitTillAllCallsDoneUsingJS();
		return new EditUserDetailsPage(webOp, dataProvider);
	}
}
