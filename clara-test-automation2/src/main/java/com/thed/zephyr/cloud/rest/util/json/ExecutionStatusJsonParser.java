package com.thed.zephyr.cloud.rest.util.json;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.thed.zephyr.cloud.rest.model.ExecutionStatus;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import static com.thed.zephyr.cloud.rest.model.enam.ExecutionStatusFieldId.*;

/**
 * Created by aliakseimatsarski on 3/20/16.
 */
public class ExecutionStatusJsonParser implements JsonObjectParser<ExecutionStatus> {

    public ExecutionStatus parse(JSONObject jsonObject) throws JSONException {
        Integer id = jsonObject.optInt(ID.id) != 0 ? jsonObject.optInt(ID.id) : null;
        String name = jsonObject.optString(NAME.id);
        String description = jsonObject.optString(DESCRIPTION.id);
        String color = jsonObject.optString(COLOR.id);
        Integer type = jsonObject.optInt(TYPE.id) != 0 ? jsonObject.optInt(TYPE.id) : null;

        ExecutionStatus executionStatus = new ExecutionStatus(id, name, description, color, type);

        return executionStatus;
    }
}
