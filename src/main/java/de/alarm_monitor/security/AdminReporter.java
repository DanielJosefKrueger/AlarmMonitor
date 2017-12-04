package de.alarm_monitor.security;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import de.alarm_monitor.email.EMailList;
import de.alarm_monitor.main.SystemInformation;
import de.alarm_monitor.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Singleton
public class AdminReporter extends Thread {
    private static final Logger logger = LogManager.getLogger(AdminReporter.class);
    private final SystemInformation systemInformation;
    private MainConfiguration mainConfiguration;

    @Inject
    AdminReporter(SystemInformation systemInformation) {
        this.systemInformation = systemInformation;
        mainConfiguration = MainConfigurationLoader.getConfig();

    }


    @Override
    public void run() {

        logger.debug("Starting Adminreporter");

        while (true) {
            File dir = systemInformation.getLoggingFolder();
            logger.debug("Logging-Folder is {}", dir.getAbsoluteFile().getAbsolutePath());
            File[] files = dir.listFiles((dir1, name) -> name.equals("alarmmonitor.log"));
            File log = files[0];


            String content = createEmailForAdmin();
            String emailAdresses = mainConfiguration.getEmailAdmin();
            logger.debug("Sending regular notification to admin");
            // EMailList.sendEmail(emailAdresses, content, "Status Alarmmonitor");
            EMailList.sendAdminEmail(emailAdresses, content, "Status Alarmmonitor", log.getAbsoluteFile().getAbsolutePath());

            try {
                TimeUnit.MINUTES.sleep(mainConfiguration.getIntervalEmailAdmin());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private String createEmailForAdmin() {

        File dir = systemInformation.getLoggingFolder();

        logger.debug("Logging-Folder is {}", dir.getAbsoluteFile().getAbsolutePath());
        File[] files = dir.listFiles((dir1, name) -> name.equals("alarmmonitor.log"));
        File log = files[0];
        return FileUtil.getLastLinesOfFile(200, log);

    }
}
