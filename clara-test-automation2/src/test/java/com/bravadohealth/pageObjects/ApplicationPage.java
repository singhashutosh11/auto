package com.bravadohealth.pageObjects;

import com.bravadohealth.clara1.pageObjects.ClaraLogInPage;
import com.bravadohealth.clara1.pageObjects.constants.ApplicationConstatnts;
import com.bravadohealth.keystone.pageObjects.LogInPage;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class ApplicationPage extends AbstractPage implements ApplicationConstatnts{
	
	private String ClaraUrlAtLocalhost="localhost:8081/#!/login";
	
	public ApplicationPage(WebOperation webOp,PageDataProvider dataProvider) {
		super(webOp,dataProvider);
		// TODO Auto-generated constructor stub
	}
	
	public LogInPage navigateToKSHomePage(){
		webOp.driver().manage().window().maximize();
		webOp.driver().get(keyStoneUrl);
		return new LogInPage(webOp, dataProvider);
	}
	
	public ClaraLogInPage navigateToClaraPageAtMockProject(){
        webOp.driver().manage().window().maximize();
        webOp.driver().get(ClaraMockProjectURL);
        webOp.gethMap().put("mockProject", "Yes");
        return new ClaraLogInPage(webOp, dataProvider);
    }
	
	public ClaraLogInPage navigateToClaraPage(){
		webOp.driver().manage().window().maximize();
		webOp.driver().get(ClaraURL);
		return new ClaraLogInPage(webOp, dataProvider);
	}
	
	public ClaraLogInPage navigateToClaraPageAtStaging(){
		webOp.driver().manage().window().maximize();
		webOp.driver().get(ClaraStagingURL);
		return new ClaraLogInPage(webOp, dataProvider);
	}
	
	public ClaraLogInPage navigateToClaraPageAtLocalhost(){
        webOp.driver().manage().window().maximize();
        webOp.driver().get(ClaraUrlAtLocalhost);
        return new ClaraLogInPage(webOp, dataProvider);
    }

}
