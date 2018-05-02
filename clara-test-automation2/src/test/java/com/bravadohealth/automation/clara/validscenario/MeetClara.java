package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.GroupsPage;
import com.bravadohealth.clara1.pageObjects.HomeViewPage;
import com.bravadohealth.clara1.pageObjects.IdVerificationPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class MeetClara extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK	= "negetiveScenarioCheck";
	private static final String	YES	= "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/meet_clara/meetClara.xls";
	
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";meetClara";
		} catch (UnsupportedEncodingException e) {
			
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		AbstractPage abstractPage = applicationPage.navigateToClaraPage()
				.moveToSelectPlanPage()
				.selectPlan()
				.selectNoOfUser()
				.checkOut()
				.createClaraId()
				.ConfirmOrder()
				.getStarted()
				.provideFacilityDetails()
				.continueAfterLicenseSelected();
		
		if(abstractPage instanceof IdVerificationPage) {
			IdVerificationPage idVerificationPage = (IdVerificationPage) abstractPage;
			abstractPage = idVerificationPage.verifyIdentity()
							.validateSuccessIDVerification()
							.continueToDone()
							.postCompletionAction();
		}
		
		if(abstractPage instanceof HomeViewPage) {
			
			if (doWeGoToGroupsPage()) {
				GroupsPage groupsPage = new HomeViewPage(this, pdp)
						.moveToAccountPage()
						.moveToGroupsPage();
				
				if (StringUtils.equalsIgnoreCase(getParameter("verifyDefaultGroups"), "Y")) {
					groupsPage
						.verifyDefaultGroupsExists()
						.verifyDefaultGroupsCannotBeRenamed()
						.verifyDefaultGroupsCannotBeDeleted()
						.verifyCorrectPermissionsAreAssociatedWithDefaultGroups();
				}
				if (StringUtils.equalsIgnoreCase(getParameter("verifyFacilityAdminGroupRestrictions"), "Y")) {
					groupsPage.verifyFacilityAdminGroupRestrictions();
				}
				if (StringUtils.equalsIgnoreCase(getParameter("verifyUserPresentInNonEPCSGroup"), "Y")) {
					groupsPage.verifyUserPresentInNonEPCSGroup();
				}
				if (StringUtils.equalsIgnoreCase(getParameter("verifyUserPresentInEPCSGroup"), "Y")) {
					groupsPage.verifyUserPresentInEPCSGroup();
				}
				if (StringUtils.equalsIgnoreCase(getParameter("verifyBasicUserOnlyPresentInFAdminGroup"), "Y")) {
					groupsPage.verifyBasicUserOnlyPresentInFAdminGroup();
				}
			} else {				
				abstractPage=new HomeViewPage(this, pdp).moveToAccountPage()
						.moveToFacilityPage()
						.verifyFacilityPage()
						.navigateToAccountSection()
						.verifyUserDetailsAfterSetUp()
						.viewActivityLogAfterSetUpFacility();
				if(StringUtils.equalsIgnoreCase(getParameter("verifyTimeStamp"), "Y"))
				{
					((AuditLogPage)abstractPage).verifyAuditLogForTimeZoneAndDSTSetDuringCreateFacility();
				}
			}
			
		}

	}
	
	private boolean doWeGoToGroupsPage() {
		return StringUtils.equalsIgnoreCase(getParameter("verifyDefaultGroups"), "Y") 
				|| StringUtils.equalsIgnoreCase(getParameter("verifyFacilityAdminGroupRestrictions"), "Y")
				|| StringUtils.equalsIgnoreCase(getParameter("verifyUserPresentInNonEPCSGroup"), "Y")
				|| StringUtils.equalsIgnoreCase(getParameter("verifyUserPresentInEPCSGroup"), "Y")
				|| StringUtils.equalsIgnoreCase(getParameter("verifyBasicUserOnlyPresentInFAdminGroup"), "Y");
	}
}
