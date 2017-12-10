package de.alarm_monitor.security;

import com.ctc.wstx.util.ExceptionUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.email.EMailList;
import de.alarm_monitor.main.SystemInformation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Singleton
public class CriticalAdminReporter {


        private static final Logger logger = LogManager.getLogger(de.alarm_monitor.security.PeriodicalAdminReporter.class);
        private final SystemInformation systemInformation;
        private MainConfiguration mainConfiguration;

        @Inject
        CriticalAdminReporter(SystemInformation systemInformation, Provider<MainConfiguration> provider) {
            this.systemInformation = systemInformation;
            mainConfiguration = provider.get();

        }

    public void sendCriticalEmailToAdmin(String message, Throwable throwable){
        logger.info("Sende Email zum Admin wegen eines kritischen Problems");
        File dir = systemInformation.getLoggingFolder();
        logger.debug("Logging-Folder is {}", dir.getAbsoluteFile().getAbsolutePath());
        File[] files = dir.listFiles((dir1, name) -> name.equals("alarmmonitor.log"));
        File log = files[0];


        StringBuilder contentSb = new StringBuilder();
        contentSb.append("Folgendes Problem ist aufgtreten:\nNachricht:");
        contentSb.append(message).append("\n");
        if(throwable !=null){
            contentSb.append("Stacktrace:\n").append(ExceptionUtils.getStackTrace(throwable));
        }
        String emailAdresses = mainConfiguration.getEmailAdmin();
        logger.debug("Sending critical EMail notification to admin");
        EMailList.sendAdminEmail(emailAdresses, contentSb.toString(), "KRITISCHER FEHLER Alarmmonitor", log.getAbsoluteFile().getAbsolutePath());
    }

    public void sendCriticalEmailToAdmin(String message){
        sendCriticalEmailToAdmin(message, null);
    }

}
