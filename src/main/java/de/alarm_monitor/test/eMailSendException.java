package de.alarm_monitor.test;

public class eMailSendException extends Exception {
    public eMailSendException() {
    }

    public eMailSendException(String message) {
        super(message);
    }

    public eMailSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public eMailSendException(Throwable cause) {
        super(cause);
    }

    public eMailSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
