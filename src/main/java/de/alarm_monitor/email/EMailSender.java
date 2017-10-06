package de.alarm_monitor.email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EMailSender {

    final static Logger log = LogManager.getLogger(EMailSender.class);
    private final EMailConfiguration config;

    public EMailSender() {
        this.config = EMailConfigurationLoader.getConfig();
    }

    public boolean sendEmail(String receiver, String msg, String subject) {

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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
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
