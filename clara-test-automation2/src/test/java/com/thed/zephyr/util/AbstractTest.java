package com.thed.zephyr.util;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aliakseimatsarski on 3/17/16.
 */
public class AbstractTest {

    final static String accessKey = "NDU3ZWVlNzMtMDhlZS0zZjk5LTk0YjgtZWM2MTdjODE2ZTkxIGFkbWluIGNp";/*replace with you credentials */
    final static String secretKey = "AmXr6Aye8g0O8rQngvrTXY_0mXJ1ftIpbZ0cCeNYamI";/*replace with you credentials */
    final static String userName = "admin";
    final static String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
    public static ZFJCloudRestClient client;

    public Logger log = LoggerFactory.getLogger(AbstractTest.class);

    static {
        client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName).build();
    }
}
