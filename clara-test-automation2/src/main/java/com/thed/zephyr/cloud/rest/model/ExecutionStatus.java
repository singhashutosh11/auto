package com.thed.zephyr.cloud.rest.model;

/**
 * Created by Kavya on 29-02-2016.
 */
public class ExecutionStatus {

    public Integer id;
    public String name;
    public String description;
    public String color;
    public Integer type;

    public ExecutionStatus() {
    }

    public ExecutionStatus(Integer id, String name, String description, String color, Integer type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : \"" + id + "\"" +
                ", \"name\" : \"" + name + "\"" +
                ", \"description\" : \"" + description + "\"" +
                ", \"color\" : \"" + color + "\"" +
                ", \"type\" : \"" + type + "\"" +
                "}";
    }
}
