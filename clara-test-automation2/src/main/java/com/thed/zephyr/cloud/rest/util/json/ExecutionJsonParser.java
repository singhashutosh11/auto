package com.thed.zephyr.cloud.rest.util.json;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.thed.zephyr.cloud.rest.model.Defect;
import com.thed.zephyr.cloud.rest.model.Execution;
import com.thed.zephyr.cloud.rest.model.ExecutionStatus;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.thed.zephyr.cloud.rest.model.enam.ExecutionFieldId.*;

/**
 * Created by aliakseimatsarski on 3/19/16.
 */
public class ExecutionJsonParser implements JsonObjectParser<Execution> {

    private final ExecutionStatusJsonParser executionStatusJsonParser = new ExecutionStatusJsonParser();

    private final DefectJsonParser defectJsonParser = new DefectJsonParser();


    public Execution parse(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.optString(ID.id);
        Long issueId = jsonObject.optLong(ISSUE_ID.id) != 0L ? jsonObject.optLong(ISSUE_ID.id) : null;
        Long versionId = jsonObject.optLong(VERSION_ID.id) != 0L ? jsonObject.optLong(VERSION_ID.id) : null;
        Long projectId = jsonObject.optLong(PROJECT_ID.id) != 0L ? jsonObject.optLong(PROJECT_ID.id) : null;
        String cycleId = jsonObject.optString(CYCLE_ID.id);
        Long orderId = jsonObject.optLong(ORDER_ID.id) != 0L ? jsonObject.optLong(ORDER_ID.id) : null;
        String comment = jsonObject.optString(COMMENT.id);
        String executedBy = jsonObject.optString(EXECUTED_BY.id);
        Date executedOn = jsonObject.optLong(EXECUTED_ON.id) != 0L ? new Date(jsonObject.optLong(EXECUTED_ON.id)) : null;
        String modifiedBy = jsonObject.optString(MODIFIED_BY.id);
        String createdBy = jsonObject.optString(CREATED_BY.id);
        String cycleName = jsonObject.optString(CYCLE_NAME.id);
        ExecutionStatus status = jsonObject.optJSONObject(STATUS.id) != null ? executionStatusJsonParser.parse(jsonObject.getJSONObject(STATUS.id)) : null;
        Collection<Defect> defects = parseDefectsArray(jsonObject, DEFECTS.id);
        Collection<Defect> stepDefects = parseDefectsArray(jsonObject, STEP_DEFECTS.id);
        Date creationDate = jsonObject.optLong(CREATION_DATE.id) != 0L ? new Date(jsonObject.optLong(CREATION_DATE.id)) : null;

        Execution execution = new Execution(id, issueId,versionId, projectId, cycleId, orderId, comment, executedBy, executedOn, modifiedBy, createdBy, cycleName, status, defects, stepDefects, creationDate);

        return execution;
    }

    private Collection<Defect> parseDefectsArray(JSONObject jsonObject, String key) throws JSONException{
        final JSONArray valueObject = jsonObject.optJSONArray(key);
        if (valueObject == null) {
            return new ArrayList<Defect>();
        }
        Collection<Defect> result = new ArrayList<Defect>(valueObject.length());
        for (int i = 0; i < valueObject.length(); i++) {
            JSONObject json = valueObject.optJSONObject(i);
            if (json != null){
                result.add(defectJsonParser.parse(json));
            }

        }
        return result;
    }
}
