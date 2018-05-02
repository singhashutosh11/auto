package com.thed.zephyr.cloud.rest.client;

import java.io.Closeable;

/**
 * Created by Kavya on 18-02-2016.
 */
public interface ZephyrRestClient extends Closeable {

    ExecutionRestClient getExecutionRestClient();
    CycleRestClient getCycleRestClient();
    JobProgressRestClient getJobProgressRestClient();
}
