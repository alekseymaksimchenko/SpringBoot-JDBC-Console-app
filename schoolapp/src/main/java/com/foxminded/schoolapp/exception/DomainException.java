package com.foxminded.schoolapp.exception;

public final class DomainException extends Exception {

    private static final long serialVersionUID = 5318154847303113522L;

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

}
