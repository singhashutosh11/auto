package com.thed.zephyr.cloud.rest.model.enam;

/**
 * Created by Kavya on 28-03-2016.
 */
public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    public final String order;

    SortOrder(String order) {
        this.order = order;
    }
}
