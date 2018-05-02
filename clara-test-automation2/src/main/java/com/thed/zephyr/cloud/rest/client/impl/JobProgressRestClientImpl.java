package com.thed.zephyr.cloud.rest.client.impl;

import com.atlassian.httpclient.api.Response;
import com.atlassian.httpclient.api.ResponsePromise;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thed.zephyr.cloud.rest.client.JobProgressRestClient;
import com.thed.zephyr.cloud.rest.constant.ApplicationConstants;
import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.JobProgress;
import com.thed.zephyr.cloud.rest.util.HttpResponseParser;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.function.Function;

/**
 * Created by Kavya on 01-03-2016.
 */
public class JobProgressRestClientImpl implements JobProgressRestClient {

    private final DisposableHttpClient httpClient;

    private final URI baseUri;

    private HttpResponseParser httpResponseParser;

    private Logger log = LoggerFactory.getLogger(JobProgressRestClientImpl.class);

    private long CALL_DELAY = 1000l;


    public JobProgressRestClientImpl(URI baseUri, DisposableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.baseUri = baseUri;
        this.httpResponseParser = new HttpResponseParser();
    }

    public JobProgress getJobProgress(String jobProgressToken) throws JobProgressException {
        final URI getExecutionsUri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_JOB_PROGRESS).path(jobProgressToken).build();
        JobProgress jobProgress;
        try {
            do {
                Thread.sleep(CALL_DELAY);
                ResponsePromise responsePromise = httpClient.newRequest(getExecutionsUri).setAccept("application/json").get();
                Response response = responsePromise.claim();
                String jsonString = httpResponseParser.parseStringResponse(response);
                ObjectMapper objectMapper = new ObjectMapper();
                jobProgress = objectMapper.readValue(jsonString, JobProgress.class);
            } while (jobProgress.progress != null && !jobProgress.progress.equals(1.0));

            return jobProgress;
        } catch (Throwable exception) {
            throw new JobProgressException(exception);
        }
    }
}
