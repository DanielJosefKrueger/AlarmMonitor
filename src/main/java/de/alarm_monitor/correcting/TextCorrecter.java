package de.alarm_monitor.correcting;

import de.alarm_monitor.exception.CorrectingException;

public interface TextCorrecter {

    String correct(String text) throws CorrectingException;
}
