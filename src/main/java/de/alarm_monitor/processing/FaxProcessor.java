package de.alarm_monitor.processing;

import java.io.File;

public interface FaxProcessor {
    void processAlarmFax(File pdf);
}
