package com.thed.zephyr.cloud.rest.model;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * Created by Kavya on 18-02-2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class Cycle {

    public String id;
    public String name;
    public String environment;
    public String build;
    public Long versionId;
    public Long projectId;
    public Date startDate;
    public Date endDate;
    public String description;
    public Long sprintId;

    public Cycle() {
    }

    public Cycle(String id,
                 String name,
                 String environment,
                 String build,
                 Long versionId,
                 Long projectId,
                 Date startDate,
                 Date endDate,
                 String description) {
        this.id = id;
        this.name = name;
        this.environment = environment;
        this.build = build;
        this.versionId = versionId;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @Override
    public String toString() {
        return "{\"cycle\" {" +
                "\"id\" : \"" + id + "\"" +
                ", \"name\" : " + name +
                ", \"environment\" : " + environment +
                ", \"build\" : " + build +
                ", \"versionId\" : \"" + versionId + "\"" +
                ", \"projectId\" : " + projectId +
                ", \"startDate\" : \"" + startDate + "\"" +
                ", \"endDate\" : \"" + endDate + "\"" +
                ", \"description\" : " + description +
                "}";
    }
}
