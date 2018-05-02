package com.thed.zephyr.cloud.rest.client.impl;

import com.atlassian.httpclient.api.Response;
import com.atlassian.httpclient.api.ResponsePromise;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.thed.zephyr.cloud.rest.client.CycleRestClient;
import com.thed.zephyr.cloud.rest.client.JobProgressRestClient;
import com.thed.zephyr.cloud.rest.client.async.AsyncCycleRestClient;
import com.thed.zephyr.cloud.rest.constant.ApplicationConstants;
import com.thed.zephyr.cloud.rest.exception.BadRequestParamException;
import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.Cycle;
import com.thed.zephyr.cloud.rest.model.JobProgress;
import com.thed.zephyr.cloud.rest.util.HttpResponseParser;
import com.thed.zephyr.cloud.rest.util.ValidateUtil;
import com.thed.zephyr.cloud.rest.util.json.CycleJsonParser;
import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kavya on 18-02-2016.
 */
public class CycleRestClientImpl implements CycleRestClient {

    private AsyncCycleRestClient asyncCycleRestClient;

    private JsonObjectParser<Cycle> cycleJsonObjectParser;

    private HttpResponseParser httpResponseParser;

    private JobProgressRestClient jobProgressRestClient;

    Logger log = LoggerFactory.getLogger(CycleRestClientImpl.class);


    public CycleRestClientImpl(URI baseUri, DisposableHttpClient httpClient) {
        httpResponseParser = new HttpResponseParser();
        this.cycleJsonObjectParser = new CycleJsonParser();
        this.jobProgressRestClient = new JobProgressRestClientImpl(baseUri, httpClient);
    }

    public Cycle createCycle(Cycle cycle) throws JSONException, HttpException, BadRequestParamException {
        return createCycle(cycle, cycleJsonObjectParser);
    }

    public <T> T createCycle(Cycle cycle, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
        try {
            ValidateUtil.validate(cycle);

            ResponsePromise responsePromise = asyncCycleRestClient.createCycle(cycle);
            Response response = responsePromise.claim();
            JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);
            return jsonParser.parse(jsonResponse);
        } catch (JSONException exception) {
            log.error("Error during parse response from server.", exception);
            throw exception;
        } catch (HttpException exception) {
            log.error("Http error from server.", exception);
            throw exception;
        } catch (BadRequestParamException e) {
            log.error("Error in request", e);
            throw e;
        }
    }


    public Cycle getCycle(Long projectId, Long versionId, String cycleId) throws JSONException, HttpException, BadRequestParamException {
        return getCycle(projectId, versionId, cycleId, cycleJsonObjectParser);
    }

    public <T> T getCycle(Long projectId, Long versionId, String cycleId, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
        try {
            ValidateUtil.validate(projectId, versionId, cycleId);

            ResponsePromise responsePromise = asyncCycleRestClient.getCycle(projectId, versionId, cycleId);
            Response response = responsePromise.claim();
            JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);
            return jsonParser.parse(jsonResponse);
        } catch (JSONException exception) {
            log.error("Error during parse response from server.", exception);
            throw exception;
        } catch (HttpException exception) {
            log.error("Http error from server.", exception);
            throw exception;
        } catch (BadRequestParamException exception) {
            log.error("Required parameter is missing", exception);
            throw  exception;
        }
    }

    public Cycle updateCycle(String cycleId, Cycle newCycle) throws JSONException, HttpException, BadRequestParamException {
        return updateCycle(cycleId, newCycle, cycleJsonObjectParser);
    }

    public <T> T updateCycle(String cycleId, Cycle newCycle, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
        try {
            ValidateUtil.validate(cycleId, newCycle);
            if(cycleId.equals(ApplicationConstants.AD_HOC_CYCLE_ID))
                throw new BadRequestParamException("Can not update adhoc cycle");
            ResponsePromise responsePromise = asyncCycleRestClient.updateCycle(cycleId, newCycle);
            Response response = responsePromise.claim();
            JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);
            return jsonParser.parse(jsonResponse);
        } catch (JSONException exception) {
            log.error("Error during parse response from server.", exception);
            throw exception;
        } catch (HttpException exception) {
            log.error("Http error from server.", exception);
            throw exception;
        } catch (BadRequestParamException e) {
            log.error("Error in request", e);
            throw e;
        }
    }

    public Boolean deleteCycle(Long projectId, Long versionId, String cycleId) throws HttpException, BadRequestParamException {
        try {
            ValidateUtil.validate(projectId, versionId, cycleId);
            if(cycleId.equals(ApplicationConstants.AD_HOC_CYCLE_ID))
                throw new BadRequestParamException("Can not delete adhoc cycle");
            ResponsePromise responsePromise = asyncCycleRestClient.deleteCycle(projectId, versionId, cycleId);
            Response response = responsePromise.claim();
            return httpResponseParser.parseBooleanResponse(response);
        } catch (HttpException exception) {
            log.error("Http error from server.", exception);
            throw exception;
        } catch (BadRequestParamException e) {
            log.error("Error in request", e);
            throw e;
        }
    }

    public List<Cycle> getCycles(Long projectId, Long versionId) throws JSONException, HttpException, BadRequestParamException {
        return getCycles(projectId, versionId, cycleJsonObjectParser);
    }

    public <T> List<T> getCycles(Long projectId, Long versionId, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException {
        try {
            ValidateUtil.validate(projectId, versionId);

            ResponsePromise responsePromise = asyncCycleRestClient.getCycles(projectId, versionId);
            Response response = responsePromise.claim();
            JSONArray jsonResponse = httpResponseParser.parseJsonArrayResponse(response);
            return parseCycleArray(jsonResponse, parser);
        } catch (JSONException exception) {
            log.error("Error during parse response from server.", exception);
            throw exception;
        } catch (HttpException exception) {
            log.error("Http error from server.", exception);
            throw exception;
        } catch (BadRequestParamException e) {
            log.error("Error in request", e);
            throw e;
        }
    }

    private <T> List<T> parseCycleArray(JSONArray jsonArray, JsonObjectParser<T> parser) throws JSONException{
        List<T> result = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.optJSONObject(i);
            if (json != null){
                result.add(parser.parse(json));
            }
        }
        return result;
    }
}
