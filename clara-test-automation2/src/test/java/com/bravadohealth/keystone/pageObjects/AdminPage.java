package com.bravadohealth.keystone.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.InviteUser;
import com.bravadohealth.pagedataset.UserDetails;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class AdminPage extends AbstractPage {
	By sendInvitationLocator=By.xpath("//button[contains(text(),'Send Invitations')]");
	By addButtonLocatr=By.xpath("//button[contains(@class,'add-row-button')]");
	By clearButtonLocator=By.xpath("//button[contains(text(),Clear All)]");

	public AdminPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	public AdminPage inviteUser(){
		InviteUser inviteUser=dataProvider.getPageData("inviteUser");
		int i=1;
		for(UserDetails User:inviteUser.getUserDetails()){
			By newUserEmailID=By.xpath("//div[@id='invite-users-wrapper']["+i+"]//input[@placeholder='email']");
			By newUserFirstName=By.xpath("//div[@id='invite-users-wrapper']["+i+"]//input[@placeholder='first name']");
			By newUserLastName=By.xpath("//div[@id='invite-users-wrapper']["+i+"]//input[@placeholder='last name']");
			By newUserRole=By.xpath("//div[@id='invite-users-wrapper']["+i+"]//select");
			webOp.driver().findElement(newUserEmailID).clear();
			webOp.driver().findElement(newUserEmailID).sendKeys(User.getEmailId());
			webOp.driver().findElement(newUserFirstName).clear();
			webOp.driver().findElement(newUserFirstName).sendKeys(User.getFirstName());
			webOp.driver().findElement(newUserLastName).clear();
			webOp.driver().findElement(newUserLastName).sendKeys(User.getLastname());
			new Select(webOp.driver().findElement(newUserRole)).selectByIndex(1);
			if(User.isClear()!=null){
				if(User.isClear().equals(true)){
					By clearRowButton=By.xpath("//div[@id='invite-users-wrapper']["+i+"]//button[contains(@class,'delete-row-button clear-row-button')]");
					webOp.driver().findElement(clearRowButton).click();
				}
				else if(i>=3 && User.isClear().equals(false))
					webOp.driver().findElement(addButtonLocatr).click();
			}
			i++;
		}
		if(inviteUser.isClearAll()!=null && inviteUser.isClearAll().equals(true)){
			webOp.driver().findElement(clearButtonLocator).click();
		}
		else
			webOp.driver().findElement(sendInvitationLocator).click();

		return this;

	}

}
