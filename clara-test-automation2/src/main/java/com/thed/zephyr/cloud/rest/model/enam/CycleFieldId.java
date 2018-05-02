package com.thed.zephyr.cloud.rest.model.enam;

/**
 * Created by Kavya on 21-03-2016.
 */
public enum CycleFieldId {
    ID("id"),
    NAME("name"),
    ENVIRONMENT("environment"),
    BUILD("build"),
    VERSION_ID("versionId"),
    PROJECT_ID("projectId"),
    START_DATE("startDate"),
    END_DATE("endDate"),
    DESCRIPTION("description"),
    SPRINT_ID("sprintId");

    public final String id;

    CycleFieldId(String id) {
        this.id = id;
    }
}
