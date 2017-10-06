package de.alarm_monitor.main;

import java.io.File;
import java.net.URISyntaxException;

public class SystemInformationenImpl implements SystemInformationen {

    private final File projectPath;
    private final File configFolder;
    private final File workingFolder;
    private final File logFolder;

    private static final String configFolderSuffix = "config"+ File.separator;
    private static final String workingFolderSuffix = "working"+ File.separator;
    private static final String logFolderSuffix = "log"+ File.separator;

    private static SystemInformationenImpl singleton;

    SystemInformationenImpl() {
        projectPath = calcProjectPath();
        System.out.println(projectPath);
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


    private File calcProjectPath(){
        try {
            return new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static SystemInformationen get(){
        if(singleton == null){
            singleton = new SystemInformationenImpl();
        }
        return singleton;
    }
}
