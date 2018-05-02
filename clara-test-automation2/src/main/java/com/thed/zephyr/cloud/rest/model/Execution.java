package com.thed.zephyr.cloud.rest.model;

import com.thed.zephyr.cloud.rest.util.json.serializer.ExecutionSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Kavya on 18-02-2016.
 */

@JsonSerialize(using = ExecutionSerializer.class)
public class Execution {

    public String id;
    public Long issueId;
    public Long versionId;
    public Long projectId;
    public String cycleId;
    public Long orderId;
    public String comment;
    public String executedBy;
    public Date executedOn;
    public String modifiedBy;
    public String createdBy;
    public String cycleName;
    public ExecutionStatus status;
    public Collection<Defect> defects;
    public Collection<Defect> stepDefects;
    public Date creationDate;

    public Execution () {}

    public Execution(String id, Long issueId,
                     Long versionId, Long projectId,
                     String cycleId, Long orderId, String comment,
                     String executedBy, Date executedOn, String modifiedBy,
                     String createdBy, String cycleName, ExecutionStatus status,
                     Collection<Defect> defects, Collection<Defect> stepDefects,
                     Date creationDate) {
        this.id = id;
        this.issueId = issueId;
        this.versionId = versionId;
        this.projectId = projectId;
        this.cycleId = cycleId;
        this.orderId = orderId;
        this.comment = comment;
        this.executedBy = executedBy;
        this.executedOn = executedOn;
        this.modifiedBy = modifiedBy;
        this.createdBy = createdBy;
        this.cycleName = cycleName;
        this.status = status;
        this.defects = defects;
        this.stepDefects = stepDefects;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "{\"execution\" : {" +
                "\"id\" : \"" + id + "\"" +
                ", \"issueId\" : " + issueId +
                ", \"versionId\" : " + versionId +
                ", \"projectId\" : " + projectId +
                ", \"cycleId\" : \"" + cycleId + "\"" +
                ", \"orderId\" : " + orderId +
                ", \"comment\" : \"" + comment + "\"" +
                ", \"executedBy\" : \"" + executedBy + "\"" +
                ", \"executedOn\" : " + executedOn +
                ", \"modifiedBy\" : \"" + modifiedBy + "\"" +
                ", \"createdBy\" : \"" + createdBy + "\"" +
                ", \"cycleName\" : \"" + cycleName + "\"" +
                ", \"status\" : " + status +
                ", \"defects\" : " + defects +
                ", \"stepDefects\" : " + stepDefects +
                ", \"creationDate\" : \"" + creationDate + "\"" +
                "}}";
    }
}
