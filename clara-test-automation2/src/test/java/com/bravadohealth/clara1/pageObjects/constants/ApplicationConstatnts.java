package com.bravadohealth.clara1.pageObjects.constants;

public interface ApplicationConstatnts {
	int DSTOffsetInUSInHrs = 1;
	String ClaraServiceDevUrl = "https://clara-services-dev.bravadocloud.com";
	String keyStoneUrl="http://clara-dev.bravadocloud.com";
	String ClaraMockProjectURL="http://www.bravadohealth.com/acorn-vale-03457798624180578193";
	String ClaraURL="http://clara-dev.bravadocloud.com";
	String ClaraStagingURL="https://clara-staging.bravadocloud.com";
	String accoliteIndiaEmailSuffix = "@accoliteindia.com";
	String nonInstitutionalEPCSNominationUrl = ClaraURL + "/#!/epcs-nomination/{0}";
	
	public interface DuoProxyUrls {
		String nintToken = ClaraServiceDevUrl + "/duoProxy/getNINToken/{0}"+"/0";
		String duoAuth = ClaraServiceDevUrl + "/duoProxy/enrollnewuser/{0}" + "/auth";
		String duoAllow = ClaraServiceDevUrl + "/duoProxy/enrollnewuser/{0}" + "/allow";
	}
	
	public interface MockData {
		String EmailOTP = "000000";
		String DUOPasscode = "000000";
	}
}
