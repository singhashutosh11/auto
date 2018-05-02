package com.thed.zephyr.cloud.rest.exception;

/**
 * Created by Kavya on 19-04-2016.
 */
public class BadRequestParamException extends Exception {
    public BadRequestParamException(Throwable cause) {
        super(cause);
    }

    public BadRequestParamException(String message, Throwable cause) {

        super(message, cause);
    }

    public BadRequestParamException(String message) {

        super(message);
    }
}
