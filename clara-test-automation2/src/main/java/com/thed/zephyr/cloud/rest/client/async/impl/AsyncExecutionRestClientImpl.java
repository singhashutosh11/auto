package com.thed.zephyr.cloud.rest.client.async.impl;

import com.atlassian.httpclient.api.ResponsePromise;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;
import com.thed.zephyr.cloud.rest.client.async.AsyncExecutionRestClient;
import com.thed.zephyr.cloud.rest.client.async.GenericEntityBuilder;
import com.thed.zephyr.cloud.rest.constant.ApplicationConstants;
import com.thed.zephyr.cloud.rest.model.Execution;
import com.thed.zephyr.cloud.rest.model.enam.FromCycleFilter;
import com.thed.zephyr.cloud.rest.model.enam.SortOrder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aliakseimatsarski on 3/25/16.
 */
public class AsyncExecutionRestClientImpl implements AsyncExecutionRestClient {

    private final DisposableHttpClient httpClient;

    private final URI baseUri;

    private Logger log = LoggerFactory.getLogger(AsyncExecutionRestClientImpl.class);


    public AsyncExecutionRestClientImpl(URI baseUri, DisposableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.baseUri = baseUri;
    }

    public ResponsePromise createExecution(Execution execution) {
        URI uri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_EXECUTION).build();
        log.debug("Sent request create execution path:{} execution:{}", uri.toString(), execution.toString());

        return httpClient.newRequest(uri).setEntity(new GenericEntityBuilder(execution)).setAccept("application/json").post();
    }

    public ResponsePromise getExecution(Long projectId, Long issueId, String executionId) {
        URI uri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_EXECUTION).path(executionId).queryParam(ApplicationConstants.QUERY_PARAM_PROJECT_ID, projectId).queryParam(ApplicationConstants.QUERY_PARAM_ISSUE_ID, issueId).build();
        //log.debug("Sent request get execution path:{} projectId:{} issueId:{} executionId:{}", uri.toString(), projectId, issueId, executionId);

        return httpClient.newRequest(uri).get();
    }

    public ResponsePromise updateExecution(Execution execution) {
        URI uri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_EXECUTION).path(execution.id).build();
        log.debug("Sent request update execution path:{} execution:{}", uri.toString(), execution.toString());

        return httpClient.newRequest(uri).setEntity(new GenericEntityBuilder(execution)).setAccept("application/json").put();
    }

    public ResponsePromise deleteExecution(Long issueId, String executionId) {
        URI uri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_EXECUTION)
                .path(executionId)
                .queryParam(ApplicationConstants.QUERY_PARAM_ISSUE_ID, issueId)
                .build();
        //log.debug("Sent request delete execution path:{} issueId:{} executionId:{}", uri.toString(), issueId, executionId);

        return httpClient.newRequest(uri).setAccept("application/json").delete();
    }

    public ResponsePromise getExecutions(Long projectId, Long issueId, int offset, int size) {
        URI uri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_EXECUTIONS)
                .queryParam(ApplicationConstants.QUERY_PARAM_PROJECT_ID, projectId)
                .queryParam(ApplicationConstants.QUERY_PARAM_ISSUE_ID, issueId)
                .queryParam(ApplicationConstants.QUERY_PARAM_OFFSET, offset)
                .queryParam(ApplicationConstants.QUERY_PARAM_SIZE, size)
                .build();
        //log.debug("Sent request get executions path:{} projectId:{} issueId:{} offset:{} size:{}", uri.toString(), projectId, issueId, offset, size);

        return httpClient.newRequest(uri).setAccept("application/json").get();
    }

    public ResponsePromise getExecutionsByCycle(Long projectId, Long versionId, String cycleId, int offset, int size, String sortBy, SortOrder sortOrder) {
        URI uri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_EXECUTIONS)
                .path(ApplicationConstants.URL_PATH_SEARCH)
                .path(ApplicationConstants.URL_PATH_CYCLE)
                .path(cycleId)
                .queryParam(ApplicationConstants.QUERY_PARAM_PROJECT_ID, projectId)
                .queryParam(ApplicationConstants.QUERY_PARAM_VERSION_ID, versionId)
                .queryParam(ApplicationConstants.QUERY_PARAM_OFFSET, offset)
                .queryParam(ApplicationConstants.QUERY_PARAM_SIZE, size)
                .queryParam(ApplicationConstants.QUERY_PARAM_SORT_BY, sortBy)
                .queryParam(ApplicationConstants.QUERY_PARAM_SORT_ORDER, sortOrder.order)
                .build();
        //log.debug("Sent request get executions by cycle path:{} projectId:{} versionId:{} cycleId:{}", uri.toString(), projectId, versionId, cycleId);

        return httpClient.newRequest(uri).setAccept("application/json").get();
    }

    public ResponsePromise addTestsToCycle(Long projectId, Long versionId, String cycleId, List<Long> issueIds, String fromCycleId,
                                           Long fromVersionId, int method, Map<FromCycleFilter, List<String>> filter, String zql) {
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("projectId", projectId);
        entityMap.put("versionId", versionId);
        entityMap.put("issues", issueIds != null ? issueIds : new ArrayList());
        entityMap.put("fromCycleId", StringUtils.isNotBlank(fromCycleId) ? fromCycleId : "");
        entityMap.put("fromVersionId", fromVersionId);
        entityMap.put("method", method);
        entityMap.put("jql", StringUtils.isNotBlank(zql) ? zql : "");
        Map<String, String> convertedMap = evaluateFromCycleFilter(filter);
        entityMap.putAll(convertedMap);
        if (convertedMap.containsKey(FromCycleFilter.DEFECT_STATUSES.id)){
            entityMap.put("hasDefects", true);
        } else {
            entityMap.put("hasDefects", false);
        }
        URI uri = UriBuilder.fromUri(baseUri)
                .path(ApplicationConstants.URL_PATH_EXECUTIONS)
                .path(ApplicationConstants.URL_PATH_ADD)
                .path(ApplicationConstants.URL_PATH_CYCLE)
                .path(cycleId).build();
       /*log.debug("Sent request add tests to cycle path:{} projectId:{} versionId:{} cycleId:{} issueIds:{} fromCycleId:{} fromVersionId:{}",
                uri.toString(),
                projectId,
                versionId,
                cycleId,
                issueIds != null ? issueIds.toArray().toString():"null",
                fromCycleId,
                fromVersionId);*/

        return httpClient.newRequest(uri).setEntity(new GenericEntityBuilder(entityMap)).setAccept("application/json").post();
    }


    public ResponsePromise bulkUpdateStatus(List<String> executionIds, Integer statusId, Integer stepStatusId, Boolean testStepStatusChangeFlag) {
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("executions", executionIds);
        entityMap.put("status", statusId);
        entityMap.put("stepStatus", stepStatusId);
        entityMap.put("testStepStatusChangeFlag", testStepStatusChangeFlag);
        URI uri = UriBuilder.fromUri(baseUri).path(ApplicationConstants.URL_PATH_EXECUTIONS).build();
        //log.debug("Sent request bulk update status  path:{} statusId:{} stepStatusId{} testStepStatusChangeFlag:{} clearDefectMappingFlag:{} executionIds:{}", uri.toString(), statusId, stepStatusId, testStepStatusChangeFlag, executionIds.toArray().toString());

        return httpClient.newRequest(uri).setEntity(new GenericEntityBuilder(entityMap)).setAccept("application/json").post();
    }


    public ResponsePromise bulkDeleteExecutions(List<String> executionIds) {
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("executions", executionIds);
        URI uri = UriBuilder.fromUri(baseUri)
                .path(ApplicationConstants.URL_PATH_EXECUTIONS)
                .path(ApplicationConstants.URL_PATH_DELETE)
                .build();
        log.debug("Sent request bulk delete executions  path:{} executionIds:{}", uri.toString(), executionIds.toArray().toString());

        return httpClient.newRequest(uri).setEntity(new GenericEntityBuilder(entityMap)).setAccept("application/json").post();
    }

    private Map<String, String> evaluateFromCycleFilter(Map<FromCycleFilter, List<String>> filter){
        Map<String, String> result = new HashMap<String, String>();
        if (filter == null){
            return result;
        }
        for (Map.Entry<FromCycleFilter, List<String>> entry:filter.entrySet()){
            String value = String.join(",", entry.getValue());
            result.put(entry.getKey().id, value);
        }

        return result;
    }
}
