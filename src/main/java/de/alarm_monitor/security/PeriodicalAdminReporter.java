package de.alarm_monitor.security;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.email.EMailList;
import de.alarm_monitor.main.SystemInformation;
import de.alarm_monitor.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Singleton
public class PeriodicalAdminReporter extends Thread {
    private static final Logger logger = LogManager.getLogger(AlertAdminReporter.class);
    private final SystemInformation systemInformation;
    private final EMailList eMailList;
    private MainConfiguration mainConfiguration;

    @Inject
    PeriodicalAdminReporter(SystemInformation systemInformation, Provider<MainConfiguration> provider, EMailList eMailList) {
        this.systemInformation = systemInformation;
        mainConfiguration = provider.get();

        this.eMailList = eMailList;
    }


    @Override
    public void run() {

        logger.debug("Starting Adminreporter");

        while (true) {
            File dir = systemInformation.getLoggingFolder();
            File[] files = dir.listFiles((dir1, name) -> name.equals("alarmmonitor.log"));

            File log;
            if (files != null) {
                log = files[0];
                String content = createEmailForAdmin();
                String emailAdresses = mainConfiguration.getEmailAdmin();
                logger.debug("Sending regular notification to admin");
                // EMailList.sendEmail(emailAdresses, content, "Status Alarmmonitor");
                eMailList.sendAdminEmail(emailAdresses, content, "Status Alarmmonitor", log.getAbsoluteFile().getAbsolutePath());
            } else {
                logger.warn("Could not find log file for sending to the admin in directory {}", dir);
            }
            try {
                TimeUnit.MINUTES.sleep(mainConfiguration.getIntervalEmailAdmin());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private String createEmailForAdmin() {

        File dir = systemInformation.getLoggingFolder();
        File[] files = dir.listFiles((dir1, name) -> name.equals("alarmmonitor.log"));
        if (files != null) {
            File log = files[0];
            return FileUtil.getLastLinesOfFile(200, log);
        }
        logger.error("Could not find log file to send to admin in directory {}", dir);
        return "Fehler: Log-Datei konnte nicht gefunden werden";
    }
}
