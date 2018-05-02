package com.thed.zephyr.cloud.rest.model.enam;

/**
 * Created by aliakseimatsarski on 3/19/16.
 */
public enum ExecutionFieldId {
    ID("id"),
    ISSUE_ID("issueId"),
    VERSION_ID("versionId"),
    PROJECT_ID("projectId"),
    CYCLE_ID("cycleId"),
    ORDER_ID("orderId"),
    COMMENT("comment"),
    EXECUTED_BY("executedBy"),
    EXECUTED_ON("executedOn"),
    MODIFIED_BY("modifiedBy"),
    CREATED_BY("createdBy"),
    CYCLE_NAME("cycleName"),
    STATUS("status"),
    DEFECTS("defects"),
    STEP_DEFECTS("stepDefects"),
    CREATION_DATE("creationDate");

    public final String id;

    private ExecutionFieldId(String id) {
        this.id = id;
    }
}
