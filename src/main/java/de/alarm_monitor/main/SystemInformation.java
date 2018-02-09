package de.alarm_monitor.main;

import java.io.File;

public interface SystemInformation {

    File getWorkingFolder();

    File getConfigFolder();

    File getLoggingFolder();

    File getProjectDirectory();

}
