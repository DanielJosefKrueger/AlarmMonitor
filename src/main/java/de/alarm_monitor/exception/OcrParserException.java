package de.alarm_monitor.exception;

public class OcrParserException extends Exception {
    public OcrParserException(String message) {
        super(message);
    }

    public OcrParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public OcrParserException(Throwable cause) {
        super(cause);
    }
}
