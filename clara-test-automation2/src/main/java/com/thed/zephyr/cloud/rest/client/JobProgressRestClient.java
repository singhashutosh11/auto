package com.thed.zephyr.cloud.rest.client;

import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.JobProgress;

import java.util.function.Function;

/**
 * Created by Kavya on 01-03-2016.
 */
public interface JobProgressRestClient {
    JobProgress getJobProgress(String jobProgressToken) throws JobProgressException;

    /*JobProgress getJobProgress(String jobProgressToken, Function done, Function error) throws JobProgressException;*/
}
