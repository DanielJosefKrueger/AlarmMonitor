package de.alarm_monitor.email;


import de.alarm_monitor.exception.InvalidConfigurationException;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EMailConfigurationLoader {

    public final static String EMAIL_CONFIG_FILR = "email_config.properties";
    public final static String EMAIL_List_PATH = "email_list.txt";
    final static Logger log = LogManager.getLogger(EMailConfigurationLoader.class);


    private EMailConfigurationLoader() {
    }


    public static EMailConfiguration getConfig() {
        return ConfigCache.getOrCreate(EMailConfiguration.class);
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
