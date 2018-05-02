package com.thed.zephyr.cloud.rest.model;

import com.atlassian.jira.rest.client.api.domain.Resolution;
import com.atlassian.jira.rest.client.api.domain.Status;

/**
 * Created by Kavya on 29-02-2016.
 */
public class Defect {
    public Long id;
    public String key;
    public String summary;
    public Status status;
    public Resolution resolution;

    public Defect() {
    }

    public Defect(Long id, String key, String summary, Status status, Resolution resolution) {
        this.id = id;
        this.key = key;
        this.summary = summary;
        this.status = status;
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : \"" + id + "\"" +
                ", \"key\" : \"" + key + "\"" +
                ", \"summary\" : \"" + summary + "\"" +
                ", \"status\" : \"" + status + "\"" +
                ", \"resolution\" : \"" + resolution + "\"" +
                "}";
    }
}
