package com.thed.zephyr.cloud.rest.model.enam;

/**
 * Created by aliakseimatsarski on 3/29/16.
 */
public enum FromCycleFilter {
    COMPONENTS("components"),
    PRIORITIES("priorities"),
    EXECUTION_STATUSES("statuses"),
    DEFECT_STATUSES("withStatuses");

    public final String id;

    FromCycleFilter(String id) {
        this.id = id;
    }
}
