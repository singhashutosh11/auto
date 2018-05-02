package com.bravadohealth.automation.clara.security;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.bravadohealth.automation.clara.security.pages.ClaraLogInPage;
import com.bravadohealth.automation.clara.security.pages.DuplicateLogInPage;
import com.bravadohealth.automation.clara.security.pages.PatientsPage;
import com.bravadohealth.automation.clara.security.pages.PrescriptionsPage;

import trial.keyStone.automation.ClaraWebDriver;
import trial.keyStone.automation.WebDriverManager;

public class DuplicateSession {

	private static final String PASSWORD_CLARA = "Clara123";
	private static final String USER_EFORTES_BRAVADOHEALTH_COM = "efortes@bravadohealth.com";
	private static final String USER_DDIETZE_BRAVADOHEALTH_COM = "ddietze@bravadohealth.com";
	private static final String USER_NDIAZ_BRAVADOHEALTH_COM = "Ndiaz@bravadohealth.com";
	private static final String USER_SVINCI_BRAVADOHEALTH_COM = "svinci@bravadohealth.com";
	
	private String ClaraURL="http://clara-dev.bravadocloud.com";
	private String ClaraManagePatientsURL="https://clara-dev.bravadocloud.com/#!/app/patients/manage-patients";
	
	@Test(description="CLAR-2869 Validate that the user is displayed a warning when he tries to login to Clara through another browser")
	public void CLAR_2869_Validate_that_the_user_is_displayed_warning()
	{
		logoutUser(USER_EFORTES_BRAVADOHEALTH_COM);
		WebDriver browser1 = WebDriverManager.getDriver();
		browser1.get(ClaraURL);
		
		ClaraLogInPage loginPage = new ClaraLogInPage(browser1);
		loginPage.provideUserName(USER_EFORTES_BRAVADOHEALTH_COM);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		assertTrue(loginPage.isLoggedIn());
		
		PrescriptionsPage prescriptionsPage = new PrescriptionsPage(browser1);
		assertTrue(prescriptionsPage.isPageLoaded());
		
		WebDriver browser2 = WebDriverManager.getDriver();
		browser2.get(ClaraURL);
		
		loginPage = new ClaraLogInPage(browser2);
		loginPage.provideUserName(USER_EFORTES_BRAVADOHEALTH_COM);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		assertTrue(loginPage.isLoggedIn());
		
		DuplicateLogInPage duplicateLogInPage = new DuplicateLogInPage(browser2);
		assertTrue(duplicateLogInPage.isDuplicateLogin());
		
		browser1.close();
		browser2.close();
	}
	
	@Test(description="CLAR-2871 Validate that the user remains logged in in the previous browser when he cancels login operation in the new browser")
	public void CLAR_2871_Validate_that_the_user_remains_logged_in_previous_browser()
	{
		logoutUser(USER_DDIETZE_BRAVADOHEALTH_COM);
		WebDriver browser1 = WebDriverManager.getDriver();
		browser1.get(ClaraURL);
		
		ClaraLogInPage loginPage = new ClaraLogInPage(browser1);
		loginPage.provideUserName(USER_DDIETZE_BRAVADOHEALTH_COM);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		assertTrue(loginPage.isLoggedIn());
		
		PrescriptionsPage prescriptionsPage = new PrescriptionsPage(browser1);
		assertTrue(prescriptionsPage.isPageLoaded());
		
		WebDriver browser2 = WebDriverManager.getDriver();
		browser2.get(ClaraURL);
		
		loginPage = new ClaraLogInPage(browser2);
		loginPage.provideUserName(USER_DDIETZE_BRAVADOHEALTH_COM);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		assertTrue(loginPage.isLoggedIn());
		
		DuplicateLogInPage duplicateLogInPage = new DuplicateLogInPage(browser2);
		duplicateLogInPage.cancel();
		
		prescriptionsPage.searchPatients();
		
		PatientsPage patientsPage = new PatientsPage(browser1);
		
		assertTrue(patientsPage.isPageLoaded());
		
		browser1.close();
		browser2.close();
	}
	
	@Test(description="Validate that a user is not prompted for login on access to Clara from a different tab of the same browser")
	public void CLAR_2866_Validate_that_a_user_is_not_prompted_for_login_on_different_tab()
	{
		logoutUser(USER_NDIAZ_BRAVADOHEALTH_COM);
		WebDriver browser1 = WebDriverManager.getDriver();
		browser1.get(ClaraURL);
		
		ClaraLogInPage loginPage = new ClaraLogInPage(browser1);
		loginPage.provideUserName(USER_NDIAZ_BRAVADOHEALTH_COM);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		assertTrue(loginPage.isLoggedIn());
		
		PrescriptionsPage prescriptionsPage = new PrescriptionsPage(browser1);
		assertTrue(prescriptionsPage.isPageLoaded());
		
		((ClaraWebDriver)browser1).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(browser1.getWindowHandles());
		browser1.switchTo().window(tabs.get(1));
		
		PatientsPage patientsPage = new PatientsPage(browser1);
		patientsPage.managePatients(ClaraManagePatientsURL);
		
		assertTrue(patientsPage.isPageLoaded());
		
		browser1.close();
	}

	@Test(description="Validate that the user is logged out from the previous browser when he proceeds to login in the new browser")
	public void CLAR_2873_Validate_that_the_user_is_logged_out_from_the_previous_browser()
	{

		logoutUser(USER_SVINCI_BRAVADOHEALTH_COM);
		WebDriver browser1 = WebDriverManager.getDriver();
		browser1.get(ClaraURL);
		
		ClaraLogInPage loginPage = new ClaraLogInPage(browser1);
		loginPage.provideUserName(USER_SVINCI_BRAVADOHEALTH_COM);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		assertTrue(loginPage.isLoggedIn());
		
		PrescriptionsPage prescriptionsPage = new PrescriptionsPage(browser1);
		assertTrue(prescriptionsPage.isPageLoaded());
		
		WebDriver browser2 = WebDriverManager.getDriver();
		browser2.get(ClaraURL);
		
		loginPage = new ClaraLogInPage(browser2);
		loginPage.provideUserName(USER_SVINCI_BRAVADOHEALTH_COM);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		assertTrue(loginPage.isLoggedIn());
		
		DuplicateLogInPage duplicateLogInPage = new DuplicateLogInPage(browser2);
		assertTrue(duplicateLogInPage.isDuplicateLogin());
		
		duplicateLogInPage.continueToLogin();
		
		PrescriptionsPage newPrescriptionsPage = new PrescriptionsPage(browser2);
		assertTrue(newPrescriptionsPage.isPageLoaded());
		
		prescriptionsPage.searchPatients();
		ClaraLogInPage unAuthorisedLogOutPage = new ClaraLogInPage(browser1);
		
		assertTrue(unAuthorisedLogOutPage.isPageLoaded());
		
		browser1.close();
		browser2.close();
	}

	private void logoutUser(String userName) {
		
		WebDriver webDriver = WebDriverManager.getDriver();
		webDriver.get(ClaraURL);
		
		ClaraLogInPage loginPage = new ClaraLogInPage(webDriver);
		loginPage.provideUserName(userName);
		loginPage.providePassWord(PASSWORD_CLARA);
		loginPage.signIn();
		
		if(loginPage.isLoggedIn())
		{
			DuplicateLogInPage duplicateLogInPage = new DuplicateLogInPage(webDriver);
			if(duplicateLogInPage.isDuplicateLogin())
			{
				duplicateLogInPage.continueToLogin();
				doLogout(webDriver);
			}
			else
			{
				doLogout(webDriver);
			}
		}
		
		webDriver.close();
	}

	private void doLogout(WebDriver webDriver) {
		
		PrescriptionsPage prescriptionsPage = new PrescriptionsPage(webDriver);
		if (prescriptionsPage.isPageLoaded())
		{
			prescriptionsPage.logout();
		}
	}

}
