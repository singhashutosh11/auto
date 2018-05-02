package com.thed.zephyr.cloud.rest.client.async;

import com.atlassian.httpclient.api.ResponsePromise;
import com.thed.zephyr.cloud.rest.model.Cycle;
import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONException;

import java.util.List;

/**
 * Created by aliakseimatsarski on 3/27/16.
 */
public interface AsyncCycleRestClient {

    ResponsePromise createCycle(Cycle cycle);

    ResponsePromise getCycle(Long projectId, Long versionId, String cycleId);

    ResponsePromise updateCycle(String cycleId, Cycle newCycle);

    ResponsePromise getCycles(Long projectId, Long versionId);

    ResponsePromise deleteCycle(Long projectId, Long versionId, String cycleId);

}
