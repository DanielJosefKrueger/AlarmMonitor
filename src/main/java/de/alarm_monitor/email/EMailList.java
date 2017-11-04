package de.alarm_monitor.email;

import de.alarm_monitor.main.MainConfiguration;
import de.alarm_monitor.main.MainConfigurationLoader;
import de.alarm_monitor.main.SystemInformationenImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class EMailList {

    private static final String EMAIL_CONFIG_PATH = "email_config.txt";
    private static final String EMAIL_List_PATH = "email_list.txt";
    private final EMailConfiguration config;
    private final List<String> receivers = new ArrayList<>();
    private final static Logger log = LogManager.getLogger(EMailList.class);


    public EMailList() {
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
        log.info("Sending Broadcast to Receivers");
        log.trace("EMail-Content\n"+ msg);
        StringBuilder sb = new StringBuilder();
        for (String receiver : receivers) {
           sb.append(receiver).append(",");
        }
        log.info("Send Email to: " + sb);
        sendEmail(sb.toString(), msg, subject);
    }



    public static  boolean sendEmail(String receiver, String msg, String subject) {
        EMailConfiguration config = EMailConfigurationLoader.getConfig();
        Properties props = new Properties();
            /*props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp-mail.outlook.com");
			props.put("mail.smtp.port", "587");*/

        props.put("mail.smtp.auth", config.smtpAuth());
        props.put("mail.smtp.starttls.enable", config.startTls());
        props.put("mail.smtp.host", config.smtpHost());
        props.put("mail.smtp.port", config.smtpPort());

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.username(), config.password());
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.username()));
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(receiver));
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }
}
