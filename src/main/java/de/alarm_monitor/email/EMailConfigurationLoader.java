package de.alarm_monitor.email;


import de.alarm_monitor.main.SystemInformationenImpl;
import de.alarm_monitor.test.InvalidConfigurationException;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EMailConfigurationLoader {

    public final static String EMAIL_CONFIG_FILR = "email_config.properties";
    public final static String EMAIL_List_PATH = "email_list.txt";
    final static Logger log = LogManager.getLogger(EMailConfigurationLoader.class);
    private static EMailConfiguration singleton;

    private EMailConfigurationLoader() {


    }


    public static EMailConfiguration getConfig() {

        if (singleton != null) {
            return singleton;
        }
        try (FileReader in = new FileReader(new File(SystemInformationenImpl.get().getConfigFolder(), EMAIL_CONFIG_FILR))) {
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


    private static void testSense(EMailConfiguration configuration) {


        try {
            testSmtpAuth(configuration);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }


    }


    private static void testSmtpAuth(EMailConfiguration configuration) throws InvalidConfigurationException {

        try {
            configuration.smtpAuth();
        } catch (Exception e) {
            throw new InvalidConfigurationException("Fehler beim Feld smtpAuth");
        }

        String auth = configuration.smtpAuth();
        if (auth == null) {
            throw new InvalidConfigurationException("\"Fehler beim Feld smtpAuth\"");
        }
    }


}
