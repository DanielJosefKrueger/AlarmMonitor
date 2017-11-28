package de.alarm_monitor.TextCorrecting;

import de.alarm_monitor.exception.CorrectingException;

import java.io.IOException;

public interface TextCorrecter {

    public String correct (String text) throws CorrectingException;
}
