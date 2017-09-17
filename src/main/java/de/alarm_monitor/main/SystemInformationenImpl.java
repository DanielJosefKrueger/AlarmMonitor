package de.alarm_monitor.main;

import java.io.File;

public class SystemInformationenImpl implements SystemInformationen {

    private final File projectPath;
    private final File configFolder;
    private final File workingFolder;
    private final File logFolder;

    private final String configFolderSuffix = "config";
    private final String workingFolderSuffix = "working";
    private final String logFolderSuffix = "log";


    SystemInformationenImpl() {
        projectPath = null;
        this.configFolder = new File(projectPath, configFolderSuffix);
        this.workingFolder = new File(projectPath, workingFolderSuffix);
        this.logFolder = new File(projectPath, logFolderSuffix);
    }


    @Override
    public File getWorkingFolder() {
        return this.workingFolder;
    }

    @Override
    public File getConfigFolder() {
        return this.configFolder;
    }

    @Override
    public File getLoggingFolder() {
        return this.logFolder;
    }

    public File getProjectDirectory() {
        return projectPath;
    }


}
