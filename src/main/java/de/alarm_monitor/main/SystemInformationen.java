package de.alarm_monitor.main;

import java.io.File;

public interface SystemInformationen {

    public File getWorkingFolder();

    public File getConfigFolder();

    public File getLoggingFolder();

    public File getProjectDirectory();
}
