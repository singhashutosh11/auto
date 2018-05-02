package com.thed.zephyr.cloud.rest.model.enam;

/**
 * Created by aliakseimatsarski on 3/20/16.
 */
public enum DefectFieldId {
    ID("id"),
    KEY("key"),
    SUMMARY("summary"),
    STATUS("status"),
    RESOLUTION("resolution");

    public final String id;

    private DefectFieldId(String id) {
        this.id = id;
    }
}
