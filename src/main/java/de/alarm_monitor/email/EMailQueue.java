package de.alarm_monitor.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class EMailQueue {

    private static final String EMAIL_CONFIG_PATH = "email_config.txt";
    private static final String EMAIL_List_PATH = "email_list.txt";
    private final EMailConfiguration config;
    private final List<String> receivers = new ArrayList<>();
    Logger log = LoggerFactory.getLogger(EMailQueue.class);


    public EMailQueue() {
        config = EMailConfigurationLoader.getConfig();
        loadReceiverList();
    }

    public void loadReceiverList() {
        try (BufferedReader in = new BufferedReader(new FileReader(new File(EMAIL_List_PATH)))) {
            String line = in.readLine();
            String[] split = line.split(";");
            for (String s : split) {
                log.info("Adding {} to Recervers", s);
                receivers.add(s);
            }
        } catch (FileNotFoundException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
    }


    public void broadcast(String msg, String subject) {

        EMailSender sender = new EMailSender();
        System.out.println("Sending Broadcast to Recedivers");
        for (String receiver : receivers) {
            System.out.println("Send Email to" + receiver);
            sender.sendEmail(receiver, msg, subject);
        }
    }
}
