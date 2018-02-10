package de.alarm_monitor.main;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.net.URISyntaxException;

@Singleton
public class SystemInformationImpl implements SystemInformation {


    private static final String configFolderSuffix = "config" + File.separator;
    private static final String workingFolderSuffix = "working" + File.separator;
    private static final String logFolderSuffix = "logs" + File.separator;
    private final File projectPath;
    private final File configFolder;
    private final File workingFolder;
    private final File logFolder;

    @Inject
    SystemInformationImpl() {
        projectPath = calcProjectPath();
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

    private File calcProjectPath() {
        try {
            return new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            // no logging because at this point the logger is not configured yet
            e.printStackTrace();
        }
        return null;
    }
}
