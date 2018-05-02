package com.thed.zephyr.cloud.rest.util.json;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.thed.zephyr.cloud.rest.model.Cycle;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.thed.zephyr.cloud.rest.model.enam.CycleFieldId.*;

/**
 * Created by Kavya on 21-03-2016.
 */
public class CycleJsonParser implements JsonObjectParser<Cycle> {
    Logger log = LoggerFactory.getLogger(CycleJsonParser.class);

    public Cycle parse(JSONObject jsonObject) throws JSONException {
        try {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String id = jsonObject.optString(ID.id);
            String name = jsonObject.optString(NAME.id);
            String environment = jsonObject.optString(ENVIRONMENT.id);
            String build = jsonObject.optString(BUILD.id);
            Long versionId = jsonObject.optLong(VERSION_ID.id) != 0L ? jsonObject.optLong(VERSION_ID.id) : null;
            Long projectId = jsonObject.optLong(PROJECT_ID.id) != 0L ? jsonObject.optLong(PROJECT_ID.id) : null;
            Date startDate = !jsonObject.optString(START_DATE.id).equals("null") ? dateFormatter.parse(jsonObject.optString(START_DATE.id)) : null;
            Date endDate = !jsonObject.optString(END_DATE.id).equals("null") ? dateFormatter.parse(jsonObject.optString(END_DATE.id)) : null;
            String description = jsonObject.optString(DESCRIPTION.id);

            Cycle cycle = new Cycle(id, name, environment, build, versionId, projectId, startDate, endDate, description);

            return cycle;
        } catch (ParseException e) {
            log.error("Error in parsing the date format of start date or end date of the cycle");
        }
        return null;
    }
}
