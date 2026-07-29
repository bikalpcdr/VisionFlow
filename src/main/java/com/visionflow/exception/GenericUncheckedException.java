package com.visionflow.exception;

import lombok.Getter;
/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */
@Getter
public class GenericUncheckedException extends RuntimeException {

    private String errorCode;
    private Integer status;

    public GenericUncheckedException(String message) {
        super(message);
    }

    public GenericUncheckedException(String message, String errorCode, Integer status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public GenericUncheckedException(String resourceName, Integer id) {
        super(String.format("%s not found with id: %d", resourceName, id));
        this.errorCode = "RESOURCE_NOT_FOUND";
        this.status = 404;
    }
}