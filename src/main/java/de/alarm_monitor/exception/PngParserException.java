package de.alarm_monitor.exception;

public class PngParserException extends Exception {

    public PngParserException() {
    }

    public PngParserException(String message) {
        super(message);
    }

    public PngParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public PngParserException(Throwable cause) {
        super(cause);
    }

    public PngParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
