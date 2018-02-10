package de.alarm_monitor.email;

import com.google.inject.Provider;
import de.alarm_monitor.main.SystemInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class EMailList {


    private static final String EMAIL_List_PATH = "email_list.txt";
    private final static Logger log = LogManager.getLogger(EMailList.class);
    private final EMailConfiguration config;
    private final List<String> receivers = new ArrayList<>();
    private final SystemInformation systemInformation;


    @Inject
    public EMailList(SystemInformation systemInformation, Provider<EMailConfiguration> provider) {
        this.systemInformation = systemInformation;
        config = provider.get();
        loadReceiverList();
    }

    public boolean sendEmail(String receiver, String msg, String subject, boolean isHtml) {

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
            //message.setText(msg);

            if (isHtml) {
                message.setContent(msg, "text/html");
            }


            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }


    public boolean sendAdminEmail(String receiver, String message, String subject, String filename) {

        Properties props = new Properties();
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
            Message email = new MimeMessage(session);

            MimeMultipart content = new MimeMultipart("mixed");

            MimeBodyPart text = new MimeBodyPart();
            text.setText(message);
            content.addBodyPart(text);


            if (filename != null) {
                try {
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(
                            new DataHandler(new FileDataSource(filename)));
                    messageBodyPart.setFileName(new File(filename).getName());
                    content.addBodyPart(messageBodyPart);
                } catch (Exception e) {
                    log.error("Fehler beim Erstellen des Anhangs");
                }
            }


            email.setContent(content);


            email.setFrom(new InternetAddress(config.username()));
            email.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(receiver));
            email.setSubject(subject);

            Transport.send(email);
            return true;

        } catch (MessagingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }


    public void loadReceiverList() {
        try (BufferedReader in = new BufferedReader(new FileReader(new File(systemInformation.getConfigFolder(), EMAIL_List_PATH)))) {
            String line = in.readLine();
            String[] split = line.split(";");
            for (String s : split) {
                if (s.length() > 2) {
                    log.trace("Adding {} to Recervers", s);
                    receivers.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public void broadcast(String msg, boolean isHtml) {

        String subject = config.getEmailTopic();
        log.info("Sending Broadcast to Receivers");
        log.trace("EMail-Content\n" + msg);
        StringBuilder sb = new StringBuilder();
        for (String receiver : receivers) {
            sb.append(receiver).append(",");
        }
        log.info("Send Email to: " + sb);
        sendEmail(sb.toString(), msg, subject, isHtml);
    }


}
