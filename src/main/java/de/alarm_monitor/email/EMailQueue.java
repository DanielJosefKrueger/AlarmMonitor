package de.alarm_monitor.email;

import de.alarm_monitor.main.SystemInformationenImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class EMailQueue {

    private static final String EMAIL_CONFIG_PATH = "email_config.txt";
    private static final String EMAIL_List_PATH = "email_list.txt";
    private final EMailConfiguration config;
    private final List<String> receivers = new ArrayList<>();
    Logger log = LogManager.getLogger(EMailQueue.class);


    public EMailQueue() {
        config = EMailConfigurationLoader.getConfig();
        loadReceiverList();
    }

    public void loadReceiverList() {
        try (BufferedReader in = new BufferedReader(new FileReader(new File(SystemInformationenImpl.get().getConfigFolder(),EMAIL_List_PATH)))) {
            String line = in.readLine();
            String[] split = line.split(";");
            for (String s : split) {
                if (s.length() > 2) {
                    log.info("Adding {} to Recervers", s);
                    receivers.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
    }


    public void broadcast(String msg) {


        String subject = config.getEmailTopic();
        EMailSender sender = new EMailSender();
        log.info("Sending Broadcast to Receivers");
        log.trace("EMail-Content\n"+ msg);
        for (String receiver : receivers) {
            log.info("Send Email to" + receiver);
            sender.sendEmail(receiver, msg, subject);
        }
    }
}
