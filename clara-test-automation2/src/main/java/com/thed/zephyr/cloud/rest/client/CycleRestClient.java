package com.thed.zephyr.cloud.rest.client;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.thed.zephyr.cloud.rest.exception.BadRequestParamException;
import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.Cycle;
import com.thed.zephyr.cloud.rest.model.JobProgress;
import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONException;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Kavya on 18-02-2016.
 */
public interface CycleRestClient {

    public abstract Cycle createCycle(Cycle cycle) throws JSONException, HttpException, BadRequestParamException;
    public abstract <T> T createCycle(Cycle cycle, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    public abstract Cycle getCycle(Long projectId, Long versionId, String cycleId) throws JSONException, HttpException, BadRequestParamException;
    public abstract <T> T getCycle(Long projectId, Long versionId, String cycleId, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    public abstract Cycle updateCycle(String cycleId, Cycle newCycle) throws JSONException, HttpException, BadRequestParamException;
    public abstract <T> T updateCycle(String cycleId, Cycle newCycle, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    public abstract List<Cycle> getCycles(Long projectId, Long versionId) throws JSONException, HttpException, BadRequestParamException;
    public abstract <T> List<T> getCycles(Long projectId, Long versionId, JsonObjectParser<T> parser) throws JSONException, HttpException, BadRequestParamException;

    public abstract Boolean deleteCycle(Long projectId, Long versionId, String cycleId) throws JSONException, HttpException, BadRequestParamException;

}
