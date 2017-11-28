package de.alarm_monitor.exception;

public class CorrectingException extends Exception {
    public CorrectingException() {
    }

    public CorrectingException(String message) {
        super(message);
    }

    public CorrectingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CorrectingException(Throwable cause) {
        super(cause);
    }

    public CorrectingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
