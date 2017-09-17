package de.alarm_monitor.email;


import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.alarm_monitor.test.InvalidConfigurationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EMailConfigurationLoader {

    public final static String EMAIL_CONFIG_PATH = "email_config.properties";
    public final static String EMAIL_List_PATH = "email_list.txt";
    final static Logger log = LoggerFactory.getLogger(EMailConfigurationLoader.class);
    private static EMailConfiguration singleton;

    private EMailConfigurationLoader() {


    }


    public static EMailConfiguration getConfig() {

        if (singleton != null) {
            return singleton;
        }
        try (FileReader in = new FileReader(new File(EMAIL_CONFIG_PATH))) {
            Properties props = new Properties();
            props.load(in);
            EMailConfiguration cfg = ConfigFactory.create(EMailConfiguration.class, props);
            singleton = cfg;
            testSense(cfg);
        } catch (IOException e) {
            log.error("Die Konfigurationsdatei kann nicht geladen werden", e);
        }
        return singleton;
    }


    private static void testSense(EMailConfiguration configuration)  {


        try {
            testSmtpAuth(configuration);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }


    }


    private static void testSmtpAuth(EMailConfiguration configuration) throws InvalidConfigurationException{

        try{
            configuration.smtpAuth();
        }catch(Exception e){
            throw new InvalidConfigurationException("Fehler beim Feld smtpAuth");
        }

        String auth =configuration.smtpAuth();
        if (auth == null) {
            throw new InvalidConfigurationException("\"Fehler beim Feld smtpAuth\"");
        }
    }





}
