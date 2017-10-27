package de.alarm_monitor.exception;

public class UnrecoverableException extends Exception {
    public UnrecoverableException() {
    }

    public UnrecoverableException(String message) {
        super(message);
    }

    public UnrecoverableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnrecoverableException(Throwable cause) {
        super(cause);
    }

    public UnrecoverableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
