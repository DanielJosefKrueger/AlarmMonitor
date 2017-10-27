package de.alarm_monitor.exception;

public class DisplayChangeException extends Exception {
    public DisplayChangeException() {
    }

    public DisplayChangeException(String message) {
        super(message);
    }

    public DisplayChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisplayChangeException(Throwable cause) {
        super(cause);
    }

    public DisplayChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
