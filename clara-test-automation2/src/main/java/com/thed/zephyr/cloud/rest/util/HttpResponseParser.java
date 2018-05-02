package com.thed.zephyr.cloud.rest.util;

import com.atlassian.httpclient.api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by aliakseimatsarski on 3/25/16.
 */
public class HttpResponseParser {

    private Logger log = LoggerFactory.getLogger(HttpResponseParser.class);

    public JSONObject parseJsonResponse(Response response) throws JSONException, HttpException {
        String responseBody = response.getEntity();
        log.debug("Response status:{}, body:{}", response.getStatusCode(), responseBody);
        validateHttpResponse(response, responseBody);
        JSONObject jsonResponse = new ObjectMapper().convertValue(responseBody, JSONObject.class);

        return jsonResponse;
    }

    public JSONArray parseJsonArrayResponse(Response response) throws JSONException, HttpException {
        String responseBody = response.getEntity();
        log.debug("Response status:{}, body:{}", response.getStatusCode(), responseBody);
        validateHttpResponse(response, responseBody);
        JSONArray jsonResponse = new ObjectMapper().convertValue(responseBody, JSONArray.class);

        return jsonResponse;
    }

    public Boolean parseBooleanResponse(Response response) throws HttpException {
        String responseBody = response.getEntity();
        log.debug("Response status:{}, body:{}", response.getStatusCode(), responseBody);
        validateHttpResponse(response, responseBody);
        Boolean result = Boolean.valueOf(responseBody);

        return result;
    }

    public String parseStringResponse(Response response) throws HttpException {
        String responseBody = response.getEntity();
        log.debug("Response status:{}, body:{}", response.getStatusCode(), responseBody);
        validateHttpResponse(response, responseBody);

        return responseBody;
    }

    public InputStream parseInputStrem(Response response) throws HttpException {
        validateHttpResponse(response, "file body");

        return response.getEntityStream();
    }

    private void validateHttpResponse(Response response, String entity) throws HttpException{
        if (!response.isOk()) {
            String errorMessage = "Error:" + response.getStatusCode() + " - " + response.getStatusText() + ". Server response: " + entity;
            throw new HttpException(errorMessage);
        }
    }
}
