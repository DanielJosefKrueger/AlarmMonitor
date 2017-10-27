package de.alarm_monitor.exception;

public class PrintingException extends Exception {

    public PrintingException() {
    }

    public PrintingException(String message) {
        super(message);
    }

    public PrintingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrintingException(Throwable cause) {
        super(cause);
    }

    public PrintingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
