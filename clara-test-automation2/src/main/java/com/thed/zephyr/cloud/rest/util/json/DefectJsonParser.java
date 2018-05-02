package com.thed.zephyr.cloud.rest.util.json;

import com.atlassian.jira.rest.client.api.domain.Resolution;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.jira.rest.client.internal.json.ResolutionJsonParser;
import com.atlassian.jira.rest.client.internal.json.StatusJsonParser;
import com.thed.zephyr.cloud.rest.model.Defect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import static com.thed.zephyr.cloud.rest.model.enam.DefectFieldId.*;

/**
 * Created by aliakseimatsarski on 3/20/16.
 */
public class DefectJsonParser implements JsonObjectParser<Defect> {

    private final StatusJsonParser statusJsonParser = new StatusJsonParser();

    private final ResolutionJsonParser resolutionJsonParser = new ResolutionJsonParser();

    public Defect parse(JSONObject jsonObject) throws JSONException {
        Long id = jsonObject.optLong(ID.id) != 0L ? jsonObject.optLong(ID.id) : null;
        String key = jsonObject.optString(KEY.id);
        String summary = jsonObject.optString(SUMMARY.id);
        Status status = jsonObject.optJSONObject(STATUS.id) != null ? statusJsonParser.parse(jsonObject.optJSONObject(STATUS.id)) : null;
        Resolution resolution = jsonObject.optJSONObject(RESOLUTION.id) != null ? resolutionJsonParser.parse(jsonObject.optJSONObject(RESOLUTION.id)) : null;

        Defect defect = new Defect(id, key, summary, status, resolution);

        return defect;
    }
}
