package com.foxminded.schoolapp.exception;

public final class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 5318154847303113522L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
