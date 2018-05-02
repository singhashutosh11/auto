package com.thed.zephyr.cloud.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 * Created by Kavya on 01-03-2016.
 */

public class JobProgress {

    public String timeTaken;
    public String stepMessage;
    public String summaryMessage;
    public List<String> stepMessages;
    public Double progress;
    public String message;
    public String errorMessage;
    public String stepLabel;

    public JobProgress() {
    }

    @Override
    public String toString() {
        return "JobProgress{" +
                "timeTaken='" + timeTaken + '\'' +
                ", stepMessage='" + stepMessage + '\'' +
                ", summaryMessage='" + summaryMessage + '\'' +
                ", stepMessages=" + stepMessages +
                ", progress=" + progress +
                ", message='" + message + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", stepLabel='" + stepLabel + '\'' +
                '}';
    }
}
