package de.alarm_monitor.test;

public class BadOrcFileException extends Exception {

    private static final long serialVersionUID = 8656571236792820060L;

    public BadOrcFileException() {
        super();
    }

    public BadOrcFileException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public BadOrcFileException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BadOrcFileException(String arg0) {
        super(arg0);
    }

    public BadOrcFileException(Throwable arg0) {
        super(arg0);
    }


}
