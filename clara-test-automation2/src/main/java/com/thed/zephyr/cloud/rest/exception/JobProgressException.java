package com.thed.zephyr.cloud.rest.exception;

/**
 * Created by aliakseimatsarski on 3/27/16.
 */
public class JobProgressException  extends Exception {

    public JobProgressException(Throwable cause) {
        super(cause);
    }

    public JobProgressException(String message, Throwable cause) {

        super(message, cause);
    }

    public JobProgressException(String message) {

        super(message);
    }
}
