package com.thed.zephyr.cloud.rest.client.impl;

import com.atlassian.httpclient.api.Response;
import com.atlassian.httpclient.api.ResponsePromise;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.thed.zephyr.cloud.rest.client.ExecutionRestClient;
import com.thed.zephyr.cloud.rest.client.JobProgressRestClient;
import com.thed.zephyr.cloud.rest.client.async.AsyncExecutionRestClient;
import com.thed.zephyr.cloud.rest.client.async.impl.AsyncExecutionRestClientImpl;
import com.thed.zephyr.cloud.rest.exception.BadRequestParamException;
import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.Execution;
import com.thed.zephyr.cloud.rest.model.JobProgress;
import com.thed.zephyr.cloud.rest.model.enam.FromCycleFilter;
import com.thed.zephyr.cloud.rest.model.enam.SortOrder;
import com.thed.zephyr.cloud.rest.util.HttpResponseParser;
import com.thed.zephyr.cloud.rest.util.ValidateUtil;
import com.thed.zephyr.cloud.rest.util.ZFJConnectResults;
import com.thed.zephyr.cloud.rest.util.json.ExecutionJsonParser;
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
import java.util.Map;

/**
 * Created by Kavya on 18-02-2016.
 */

public class ExecutionRestClientImpl implements ExecutionRestClient {

	private final AsyncExecutionRestClient asyncExecutionRestClient;

	private JsonObjectParser<Execution> executionJsonObjectParser;

	private HttpResponseParser httpResponseParser;

	private JobProgressRestClient jobProgressRestClient;

	private Logger log = LoggerFactory.getLogger(ExecutionRestClientImpl.class);


	public ExecutionRestClientImpl(URI baseUri, DisposableHttpClient httpClient) {
		this.asyncExecutionRestClient = new AsyncExecutionRestClientImpl(baseUri, httpClient);
		this.executionJsonObjectParser = new ExecutionJsonParser();
		this.httpResponseParser = new HttpResponseParser();
		this.jobProgressRestClient = new JobProgressRestClientImpl(baseUri, httpClient);
	}

	public Execution createExecution(Execution execution) throws JSONException, HttpException, BadRequestParamException {
		return createExecution(execution, executionJsonObjectParser);
	}

	public <T> T createExecution(Execution execution, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(execution);

			ResponsePromise responsePromise = asyncExecutionRestClient.createExecution(execution);
			Response response = responsePromise.claim();
			JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);

			return jsonParser.parse(jsonResponse.getJSONObject("execution"));
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

	public Execution getExecution(Long projectId, Long issueId, String executionId) throws JSONException, HttpException, BadRequestParamException {
		return getExecution(projectId, issueId, executionId, executionJsonObjectParser);
	}

	public <T> T getExecution(Long projectId, Long issueId, String executionId, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(projectId, issueId, executionId);

			ResponsePromise responsePromise = asyncExecutionRestClient.getExecution(projectId, issueId, executionId);
			Response response = responsePromise.claim();
			JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);
			return jsonParser.parse(jsonResponse.getJSONObject("execution").getJSONObject("execution"));
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

	public Execution updateExecution(Execution execution) throws JSONException, HttpException, BadRequestParamException {
		return updateExecution(execution, executionJsonObjectParser);
	}

	public <T> T updateExecution(Execution execution, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(execution.id, execution);

			ResponsePromise responsePromise = asyncExecutionRestClient.updateExecution(execution);
			Response response = responsePromise.claim();
			JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);
			return jsonParser.parse(jsonResponse.getJSONObject("execution"));
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

	public Boolean deleteExecution(Long issueId, String executionId) throws HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(issueId, executionId);

			ResponsePromise responsePromise = asyncExecutionRestClient.deleteExecution(issueId, executionId);
			Response response = responsePromise.claim();
			Boolean booleanResponse = httpResponseParser.parseBooleanResponse(response);

			return booleanResponse;
		} catch (HttpException exception) {
			log.error("Http error from server.", exception);
			throw exception;
		} catch (BadRequestParamException e) {
			log.error("Error in request", e);
			throw e;
		}
	}

	public ZFJConnectResults<Execution> getExecutions(Long projectId, Long issueId, int offset, int size) throws JSONException, HttpException, BadRequestParamException {
		return getExecutions(projectId, issueId, offset, size, executionJsonObjectParser);
	}

	public <T> ZFJConnectResults<T> getExecutions(Long projectId, Long issueId, int offset, int size, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(projectId, issueId);
			ValidateUtil.validateNegativeValue(offset, "offset");
			ValidateUtil.validateNegativeValue(size, "size");

			ResponsePromise responsePromise = asyncExecutionRestClient.getExecutions(projectId, issueId, offset, size);
			Response response = responsePromise.claim();
			JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);
			List<T> resultList = parseExecutionArray(jsonResponse.getJSONArray("executions"), "execution", jsonParser);
			ZFJConnectResults<T> results = new ZFJConnectResults<T>(resultList, offset, jsonResponse.getInt("totalCount"), jsonResponse.getInt("maxAllowed"));

			return results;
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

	public ZFJConnectResults<Execution> getExecutionsByCycle(Long projectId, Long versionId, String cycleId, int offset, int size, String sortBy, SortOrder sortOrder) throws JSONException, HttpException, BadRequestParamException {
		return getExecutionsByCycle(projectId, versionId, cycleId, offset, size, sortBy, sortOrder, executionJsonObjectParser);
	}

	public <T> ZFJConnectResults<T> getExecutionsByCycle(Long projectId, Long versionId, String cycleId, int offset, int size, String sortBy, SortOrder sortOrder, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(projectId, versionId, cycleId);
			ValidateUtil.validateNegativeValue(offset, "offset");
			ValidateUtil.validateNegativeValue(size, "size");

			ResponsePromise responsePromise = asyncExecutionRestClient.getExecutionsByCycle(projectId, versionId, cycleId, offset, size, sortBy, sortOrder);
			Response response = responsePromise.claim();
			JSONObject jsonResponse = httpResponseParser.parseJsonResponse(response);
			List<T> resultList = parseExecutionArray(jsonResponse.getJSONArray("searchObjectList"), "execution", jsonParser);
			ZFJConnectResults<T> results = new ZFJConnectResults<T>(resultList, offset, jsonResponse.getInt("totalCount"), jsonResponse.getInt("maxAllowed"));

			return results;
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

	public JobProgress addTestsToCycle(Long projectId, Long versionId, String cycleId, List<Long> issueIds) throws HttpException, JobProgressException, BadRequestParamException {
		try {
			ValidateUtil.validate(projectId, cycleId, issueIds);

			ResponsePromise responsePromise = asyncExecutionRestClient.addTestsToCycle(projectId, versionId, cycleId, issueIds, null, null, 1, null, null);
			Response response = responsePromise.claim();
			String jobProgressTicket = httpResponseParser.parseStringResponse(response);
			JobProgress jobProgress = jobProgressRestClient.getJobProgress(jobProgressTicket);

			return jobProgress;
		} catch (HttpException exception) {
			log.error("Http error from server.", exception);
			throw exception;
		} catch (JobProgressException exception){
			log.error("Error during proceed remote job in server.",exception);
			throw exception;
		} catch (BadRequestParamException e) {
			log.error("Error in request", e);
			throw e;
		}
	}

	public JobProgress bulkUpdateStatus(List<String> executionIds, Integer statusId, Integer stepStatusId, Boolean testStepStatusChangeFlag) throws JobProgressException, HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(executionIds, statusId);

			ResponsePromise responsePromise = asyncExecutionRestClient.bulkUpdateStatus(executionIds, statusId, stepStatusId, testStepStatusChangeFlag);
			Response response = responsePromise.claim();
			String jobProgressTicket = httpResponseParser.parseStringResponse(response);
			JobProgress jobProgress = jobProgressRestClient.getJobProgress(jobProgressTicket);

			return jobProgress;
		} catch (HttpException exception) {
			log.error("Http error from server.", exception);
			throw exception;
		}  catch (JobProgressException exception){
			log.error("Error during proceed remote job in server.",exception);
			throw exception;
		} catch (BadRequestParamException e) {
			log.error("Error in request", e);
			throw e;
		}
	}

	public JobProgress bulkDeleteExecutions(List<String> executionIds) throws JobProgressException, HttpException, BadRequestParamException {
		try {
			ValidateUtil.validate(executionIds);

			ResponsePromise responsePromise = asyncExecutionRestClient.bulkDeleteExecutions(executionIds);
			Response response = responsePromise.claim();
			String jobProgressTicket = httpResponseParser.parseStringResponse(response);
			JobProgress jobProgress = jobProgressRestClient.getJobProgress(jobProgressTicket);
			return jobProgress;
		} catch (HttpException exception) {
			log.error("Http error from server.", exception);
			throw exception;
		}  catch (JobProgressException exception){
			log.error("Error during proceed remote job in server.",exception);
			throw exception;
		} catch (BadRequestParamException exception) {
			log.error("Error in request", exception);
			throw exception;
		}
	}

	private <T> List<T> parseExecutionArray(JSONArray jsonArray, String key, JsonObjectParser<T> parser) throws JSONException{
		List<T> result = new ArrayList<T>();
		T c;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.optJSONObject(i).optJSONObject(key);
			if (json != null){
				result.add(parser.parse(json));
			}
		}

		return result;
	}
}
