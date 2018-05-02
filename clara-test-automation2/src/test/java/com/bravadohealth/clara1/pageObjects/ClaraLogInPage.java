package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.pageObjects.locators.NavLocators;
import com.bravadohealth.clara1.services.UserService;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.Login;
import com.bravadohealth.pagedataset.ManageFacilityDetails;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class ClaraLogInPage extends AbstractPage implements NavLocators, LoaderLocators {

	By userNameLocator = By.cssSelector("input[placeholder='Clara ID']");
	By passWordLocator = By.cssSelector("input[placeholder='Password']");
	By signInLocator = By.cssSelector("button#sign-in");
	By signInError = By.cssSelector("div[class*='signin-error']");
	By forgotCredentialLink = By.xpath("//div[@id='forgot-link']/a");
	
	public ClaraLogInPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public HomeViewPage logIn() throws Exception {
		Login loginDetails = dataProvider.getPageData("login");
		String userName = loginDetails.getUserName();
		String passWord = loginDetails.getPassword();
		wait.until(ExpectedConditions.presenceOfElementLocated(userNameLocator));
		webOp.driver().findElement(userNameLocator).sendKeys("" + userName + "");
		hMap.put("userName", userName);
		hMap.put("logInPassword", passWord);
		webOp.driver().findElement(passWordLocator).sendKeys("" + passWord + "");
		webOp.driver().findElement(signInLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		String endSession = "//div[contains(@class,'si-container-card')]";
		if(webOp.driver().findElements(By.xpath(endSession)).size() !=0)
		{
			webOp.driver().findElement(By.xpath("//button[contains(@class,'button-link primary-action pull-right')]")).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		}
		ManageFacilityDetails manageFacilityDetails = dataProvider.getPageData("manageFacilityDetails");
		if(Integer.parseInt(manageFacilityDetails.getNoOfFacility())>1) {
			wait.until(ExpectedConditions.urlContains(AppPage.SelectFacility.getUrlContains(hMap)));
		}
		Alogs.add(AuditAction.SUCCESSFUL_LOGIN);
		return new HomeViewPage(webOp, dataProvider);
	}
	
	public HomeViewPage newUserLogIn() throws Exception {
		ClaraUserDetails claraUserDetails = UserService.getCurrentUser();
		String userName = claraUserDetails.getEmail();
		String passWord = claraUserDetails.getPassword();
		WebDriverWait wait = new WebDriverWait(webOp.driver(), 1000);
		wait.until(ExpectedConditions.presenceOfElementLocated(userNameLocator));
		webOp.driver().findElement(userNameLocator).sendKeys("" + userName + "");
		hMap.put("userName", userName);
		hMap.put("logInPassword", passWord);
		webOp.driver().findElement(passWordLocator).sendKeys("" + passWord + "");
		webOp.driver().findElement(signInLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		String endSession = "//div[contains(@class,'si-container-card')]";
		if(webOp.driver().findElements(By.xpath(endSession)).size() !=0)
		{
			webOp.driver().findElement(By.xpath("//button[contains(@class,'button-link primary-action pull-right')]")).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		}
		Alogs.add(AuditAction.SUCCESSFUL_LOGIN);
		return new HomeViewPage(webOp, dataProvider);
	}

	public ClaraLogInPage InvalidlogIn() throws Exception {
		Login loginDetails = dataProvider.getPageData("login");
		String userName = loginDetails.getUserName();
		String passWord = loginDetails.getPassword();
		wait.until(ExpectedConditions.presenceOfElementLocated(userNameLocator));
		webOp.driver().findElement(userNameLocator).sendKeys("" + userName + "");
		webOp.driver().findElement(passWordLocator).sendKeys("" + passWord + "");
		webOp.driver().findElement(signInLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.presenceOfElementLocated(signInError));
		boolean condition = webOp.driver().findElement(signInError).getText()
				.contains("Your Clara ID or password was incorrect");
		//SoftAssert sa = new SoftAssert();
		if (loginDetails.getAttemptsAllowed()== null) {
			Assert.assertTrue(condition, "With 1 failure attempts ,The claraId and password was incorrect");
			return new ClaraLogInPage(webOp, dataProvider);
		}
		int i = 0;
		while (condition) {
			webOp.driver().findElement(userNameLocator).clear();
			webOp.driver().findElement(userNameLocator).sendKeys("" + userName + "");
			webOp.driver().findElement(passWordLocator).clear();
			webOp.driver().findElement(passWordLocator).sendKeys("" + passWord + "");
			webOp.driver().findElement(signInLocator).click();
			condition = webOp.driver().findElement(signInError).getText()
					.contains("Your Clara ID or password was incorrect");
			if (loginDetails.getAttemptsAllowed() != null) {
				if (i == Integer.parseInt(loginDetails.getAttemptsAllowed()) && i == 3) {
					String expectedMessage = "Your account will be locked after 1 more unsuccessful attempts. If you�re having trouble remembering your Clara ID or password click here.";
					condition = webOp.driver().findElement(signInError).getText().contains(expectedMessage);
					Assert.assertTrue(condition,
							"With 3 failure attempts ,the account will be locked after 2 more attempts");
				}
				if (i == Integer.parseInt(loginDetails.getAttemptsAllowed()) && i == 5) {
					String expectedMessage = "Your account is locked. To unlock your account click here.";
					condition = webOp.driver().findElement(signInError).getText().contains(expectedMessage);
					Assert.assertTrue(condition, "With 5 failure attempts the account is locked for the user");
				}
			}
			i++;
		}
		String url = webOp.driver().getCurrentUrl();
		Assert.assertFalse(url.contains("/select-patient"));

		return new ClaraLogInPage(webOp, dataProvider);
	}

	public VerifyClaraAppEmailPage forgotCredential() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.presenceOfElementLocated(forgotCredentialLink));
		webOp.driver().findElement(forgotCredentialLink).click();
		return new VerifyClaraAppEmailPage(webOp, dataProvider);
	}

    public SelectPlanPage moveToSelectPlanPage() {
    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[span[contains(text(),'Meet Clara')]]")));
		webOp.driver().findElement(By.xpath("//a[span[contains(text(),'Meet Clara')]]")).click();
        return new SelectPlanPage(webOp, dataProvider);
    }

}
