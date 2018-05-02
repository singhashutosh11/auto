package com.thed.zephyr.cloud.rest.client;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.thed.zephyr.cloud.rest.exception.BadRequestParamException;
import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.Execution;
import com.thed.zephyr.cloud.rest.model.JobProgress;
import com.thed.zephyr.cloud.rest.model.enam.FromCycleFilter;
import com.thed.zephyr.cloud.rest.model.enam.SortOrder;
import com.thed.zephyr.cloud.rest.util.ZFJConnectResults;
import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Kavya on 18-02-2016.
 */
public interface ExecutionRestClient {

    Execution createExecution(Execution execution) throws JSONException, HttpException, BadRequestParamException;
    <T> T createExecution(Execution execution, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    Execution getExecution(Long projectId, Long issueId, String executionId) throws JSONException, HttpException, BadRequestParamException;
    <T> T getExecution(Long projectId, Long issueId, String executionId, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    Execution updateExecution(Execution execution) throws JSONException, HttpException, BadRequestParamException;
    <T> T updateExecution(Execution execution, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    Boolean deleteExecution(Long issueId, String executionId) throws JSONException, HttpException, BadRequestParamException;

    ZFJConnectResults<Execution> getExecutions(Long projectId, Long issueId, int offset, int size) throws JSONException, HttpException, BadRequestParamException;
    <T> ZFJConnectResults<T> getExecutions(Long projectId, Long issueId, int offset, int size, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    ZFJConnectResults<Execution> getExecutionsByCycle(Long projectId, Long versionId, String cycleId, int offset, int size, String sortBy, SortOrder sortOrder) throws JSONException, HttpException, BadRequestParamException;
    <T> ZFJConnectResults<T> getExecutionsByCycle(Long projectId, Long versionId, String cycleId, int offset, int size, String sortBy, SortOrder sortOrder, JsonObjectParser<T> jsonParser) throws JSONException, HttpException, BadRequestParamException;

    JobProgress addTestsToCycle(Long projectId, Long versionId, String cycleId, List<Long> issueIds) throws JobProgressException, HttpException, BadRequestParamException;

    JobProgress bulkUpdateStatus(List<String> executionIds, Integer statusId, Integer stepStatusId, Boolean testStepStatusChangeFlag) throws JobProgressException, HttpException, BadRequestParamException;

    JobProgress bulkDeleteExecutions(List<String> executionIds) throws JobProgressException, HttpException, BadRequestParamException;

}
