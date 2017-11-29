package de.alarm_monitor.security;

import de.alarm_monitor.email.EMailList;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import de.alarm_monitor.main.SystemInformationenImpl;
import de.alarm_monitor.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class AdminEmailSender {

    private static final Logger logger = LogManager.getLogger(AdminEmailSender.class);

    public static void sendEmailToAdmin(String topic, String content) {
        String emailAdresses = MainConfigurationLoader.getConfig().getEmailAdmin();
        logger.debug("Sending important notification to admin");

        content += "\n Last Logs:\n" + createEmailForAdmin();

        EMailList.sendEmail(emailAdresses, content, topic);
    }

    private static String createEmailForAdmin() {

        File dir = SystemInformationenImpl.get().getLoggingFolder();
        File[] files = dir.listFiles((dir1, name) -> name.equals("alarmmonitor.log"));
        File log = files[0];
        return FileUtil.getLastLinesOfFile(400, log);

    }
}
