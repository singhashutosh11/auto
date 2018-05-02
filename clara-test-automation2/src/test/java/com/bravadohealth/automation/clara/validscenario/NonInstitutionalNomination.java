package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;

import com.bravadohealth.clara1.enums.PlanType;
import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.GlobalNavContainer;
import com.bravadohealth.clara1.pageObjects.HomeViewPage;
import com.bravadohealth.clara1.pageObjects.IdVerificationPage;
import com.bravadohealth.clara1.pageObjects.IdentityProofingPage;
import com.bravadohealth.clara1.pageObjects.NonInstitutionalEPCSNominationPage;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.AuditLogFilter;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;

public class NonInstitutionalNomination extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK	= "negetiveScenarioCheck";
	private static final String	YES	= "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/meet_clara/meetClaraWithNonInstitutionalNomination.xls";
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";nonInstitutionalNomination";
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
		
		ClaraUserDetails claraUserDetails=pdp.getPageData("claraUserDetails");
		String facilityOwnerLicenseLabel = claraUserDetails.getLicenseType();
		PlanType ownerPlanType = PlanType.getPlanTypeFromUiLabel(facilityOwnerLicenseLabel);
		if(abstractPage instanceof IdVerificationPage) {
			IdVerificationPage idVerificationPage = (IdVerificationPage) abstractPage;
			abstractPage = idVerificationPage.verifyIdentity()
							.validateSuccessIDVerification()
							.markIdProofingAsSuccess()
							.continueToDone()
							.postCompletionAction();
		}
		
		if(abstractPage instanceof HomeViewPage) {
			HomeViewPage hvp = (HomeViewPage) abstractPage;
			if(PlanType.ProCS == ownerPlanType) {
				new NonInstitutionalEPCSNominationPage(this, pdp)
				.getAndStoreNonInstitutionalEPCSNominationUrlTokenForNominators()
				.nominateFacilityOwner();
				
				if(StringUtils.isNotBlank(getParameter("auditLogFilter"))) {
					AuditLogFilter auditlogs = pdp.getPageData("auditLogFilter");
					String searchText = auditlogs.getSearchLog().getSearchText();
					if(StringUtils.equalsIgnoreCase(AuditAction.USER_NOMINATED_FOR_EPCS.name(), searchText)) {
						new AuditLogPage(this, pdp)
						.navigateToAuditPage()
						.verifyAuditTrailForNonInstitutionalNomination(AuditAction.USER_NOMINATED_FOR_EPCS)
						.logout();
					}
				} else {
					hvp.logout();
				}
				
				String selfApproveForEPCSResponse = getParameter("selfApproveForEPCSResponse");
				if(StringUtils.isNotBlank(selfApproveForEPCSResponse) && !selfApproveForEPCSResponse.equalsIgnoreCase("None")) {
					new ApplicationPage(this, pdp)
					.navigateToClaraPage()
					.newUserLogIn()
					.respondToSelfApproveForEPCSPopup()
					.logout();
					
					Boolean selfApprovedForEPCS = Boolean.valueOf(selfApproveForEPCSResponse);
					if(Boolean.FALSE == selfApprovedForEPCS) {
						String selfApproveForEPCSResponseAgain = getParameter("selfApproveForEPCSResponseAgain");
						if(StringUtils.isNotBlank(selfApproveForEPCSResponseAgain) && !selfApproveForEPCSResponseAgain.equalsIgnoreCase("None")) {
							new ApplicationPage(this, pdp)
							.navigateToClaraPage()
							.newUserLogIn()
							.respondToSelfApprovePopupAgainAfterInitialCancelResponse()
							.logout();
							
						}
					}
				}
			} else {
				hvp.logout();
			}
		}

		if(StringUtils.isNotBlank(getParameter("auditLogFilter"))) {
			AuditLogFilter auditlogs = pdp.getPageData("auditLogFilter");
			String searchText = auditlogs.getSearchLog().getSearchText();
			if(StringUtils.equalsIgnoreCase(AuditAction.SELF_APPROVE_FOR_EPCS.name(), searchText)) {
				new ApplicationPage(this, pdp)
				.navigateToClaraPage()
				.newUserLogIn();
				
				new AuditLogPage(this, pdp)
				.navigateToAuditPage()
				.verifyAuditTrailForSelfApprovalForEPCS(AuditAction.SELF_APPROVE_FOR_EPCS)
				.logout();
			}
		}
	} 	

}
