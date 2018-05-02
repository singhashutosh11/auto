package com.thed.zephyr.cloud.rest.model.enam;

/**
 * Created by aliakseimatsarski on 3/20/16.
 */
public enum ExecutionStatusFieldId {
    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    COLOR("color"),
    TYPE("type");

    public final String id;

    private ExecutionStatusFieldId(String id) {
        this.id = id;
    }
}
