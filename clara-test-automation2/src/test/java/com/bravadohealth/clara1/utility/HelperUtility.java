package com.bravadohealth.clara1.utility;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.pageObjects.constants.ApplicationConstatnts;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.pageObjects.locators.TwoFAModalLocators;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.Login;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class HelperUtility implements ConfirmationModalLocators, TwoFAModalLocators, LoaderLocators{

	public static String thizClassName = HelperUtility.class.getName();

	public static boolean isElementPresent(WebOperation webOp, By elementToFindLocator){
		return !webOp.driver().findElements(elementToFindLocator).isEmpty();
	}

	public static boolean isElementAbsent(WebOperation webOp, By elementToFindLocator){
		return !isElementPresent(webOp, elementToFindLocator);
	}

	public static String getString(String str) {
		return StringUtils.isEmpty(str) ? "": str;
	}

	public static boolean isToasterDisplayed(WebOperation webOp) throws InterruptedException {
		Thread.sleep(1000);
		return isElementPresent(webOp, By.xpath("//div[contains(@class,'clara-alert-message-container')]"));
	}

	public static void verifyToasterDisplayed(WebOperation webOp, String failMessage) throws InterruptedException {
		Boolean isToasterDisplayed = isToasterDisplayed(webOp);
		Assert.assertTrue(isToasterDisplayed, failMessage);
	}

	public static void confirmAlertModal(WebOperation webOp, By alertTitleLocator) throws InterruptedException{
		Thread.sleep(2000);
		if (HelperUtility.isElementPresent(webOp, alertTitleLocator)) {
			webOp.driver().findElement(okButtonLocator).click();
		}
	}

	public static void navigateBack(WebOperation webOp) throws InterruptedException{
		webOp.driver().navigate().back();
		confirmAlertModal(webOp, navigateAwayAlertTitleLocator);
	}

	public static void checkUrlChangesTo(WebOperation webOp, String urlContains) {
		WebDriverWait wait = new WebDriverWait(webOp.driver(), 30, 500);
		wait.until(ExpectedConditions.urlContains(urlContains));
	}

	public static void checkModalClosed(WebOperation webOp, By modalLocator) throws InterruptedException {
		Thread.sleep(1000);
		Boolean modalWindowIsClosed = HelperUtility.isElementAbsent(webOp, modalLocator);
		Assert.assertTrue(modalWindowIsClosed, "Modal Window is not closed");
	}

	public static Integer getRandomZipCode(){
		Random r = new Random();
		int Low = (int) Math.pow(10, 4);
		int High = (int) Math.pow(10, 5);
		Integer zipCode = r.nextInt(High-Low) + Low;
		return zipCode;
	}

	public static boolean isMockProject(HashMap<String,String> hMap) {
		return hMap.containsKey("mockProject");
	}

	public static void checkModalAndClickPrimary(WebOperation webOp) throws InterruptedException{
		Thread.sleep(1000);
		if (HelperUtility.isElementPresent(webOp, modalLocator)) {
			webOp.driver().findElement(removeRxButtonLocator).click();
		}
	}

	public static String getFullNameFromFirstAndLastName(String firstName, String lastName){
		return new StringBuilder(firstName).append(" ").append(lastName).toString();
	}

	public static void closeAllOtherWindows(WebOperation webOp) {
		String currentWindow=webOp.driver().getWindowHandle();
		Set<String> windowHandles = webOp.driver().getWindowHandles();
		for (String winHandle: windowHandles) {
			if(!winHandle.equals(currentWindow)) {
				webOp.driver().switchTo().window(winHandle);
				webOp.driver().manage().window().maximize();
				webOp.driver().close();
			}
		}
		webOp.driver().switchTo().window(currentWindow);
	}

	public static JSONArray getJsonArray(String jsonString){
		JSONArray arr = null;
		try {
			arr = new JSONArray(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}

	public static JSONObject getJsonObject(String jsonString){
		JSONObject obj = null;
		try {
			obj = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public static boolean isInputFieldInvalid(WebOperation webOp, By inputFieldLocator){
		WebElement inputElement = webOp.driver().findElement(inputFieldLocator);
		String inputElementClass = inputElement.getAttribute("class");
		if(inputElementClass.contains("has-error has-popover-error")) {
			return true;
		}
		return false;
	}

	public static boolean isInputFieldInvalid(WebElement inputElement){
		String inputElementClass = inputElement.getAttribute("class");
		if(inputElementClass.contains("has-error has-popover-error")) {
			return true;
		}
		return false;
	}

	public static String openUrlInNewTabAndThenCloseTab(WebOperation webOp, WebDriverWait wait, JavascriptExecutor jse, String url) throws InterruptedException{
		jse.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String> (webOp.driver().getWindowHandles());
		webOp.driver().switchTo().window(tabs.get(1));

		webOp.driver().get(url);
		wait.until(new WaitTillPageLoadExpectedCondition());

		WebElement bodyElement = webOp.driver().findElement(By.xpath("//body"));
		String body = bodyElement.getText();
		webOp.driver().close();

		webOp.driver().switchTo().window(tabs.get(0));
		return body;

	}

	public static void completeTwoFAWithDUOBypass(WebOperation webOp, WebDriverWait wait, JavascriptExecutor jse, ClaraUserDetails claraUser) throws InterruptedException{
		completeTwoFAWithDUOBypass(webOp, wait, jse, claraUser.getEmail(), claraUser.getPassword());
	}

	public static void completeTwoFAWithDUOBypass(WebOperation webOp, WebDriverWait wait, JavascriptExecutor jse, Login login) throws InterruptedException{
		completeTwoFAWithDUOBypass(webOp, wait, jse, login.getUserName(), login.getPassword());
	}

	public static void completeTwoFAWithDUOBypass(WebOperation webOp, WebDriverWait wait, JavascriptExecutor jse, String email, String password) throws InterruptedException{
		wait.until(ExpectedConditions.visibilityOfElementLocated(twoFAModalLocator));
		WebElement twoFAModal= webOp.driver().findElement(twoFAModalLocator);
		WebElement inputPasswordElement = twoFAModal.findElement(enterPasswordInputLocator);
		inputPasswordElement.sendKeys(password);
		twoFAModal.findElement(nextButtonLocator).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(enterPasscodeInputLocator));
		String url = getFormattedText(ApplicationConstatnts.DuoProxyUrls.duoAllow, email);
		openUrlInNewTabAndThenCloseTab(webOp, wait, jse, url);
		WebElement inputPasscodeElement = twoFAModal.findElement(enterPasscodeInputLocator);
		inputPasscodeElement.sendKeys(ApplicationConstatnts.MockData.DUOPasscode);
		twoFAModal.findElement(submitButtonLocator).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(twoFAModalLocator));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
	}

	public static String getFormattedText(String text, Object... replacementVars) {
		return replacementVars == null ? text : MessageFormat.format(text, replacementVars);
	}

}
