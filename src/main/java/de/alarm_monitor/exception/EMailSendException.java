package de.alarm_monitor.exception;

public class EMailSendException extends Exception {
    public EMailSendException() {
    }

    public EMailSendException(String message) {
        super(message);
    }

    public EMailSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public EMailSendException(Throwable cause) {
        super(cause);
    }

    public EMailSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
