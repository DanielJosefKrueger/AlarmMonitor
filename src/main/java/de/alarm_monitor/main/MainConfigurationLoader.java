package de.alarm_monitor.main;

import de.alarm_monitor.email.EMailConfigurationLoader;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainConfigurationLoader {

    public final static String CONFIG_PATH = "config.properties";
    final static Logger logger = LogManager.getLogger(EMailConfigurationLoader.class);
    private static MainConfiguration singleton;

    private MainConfigurationLoader() {
    }

    public static MainConfiguration getConfig() {

        File file = new File(SystemInformationenImpl.get().getConfigFolder(),CONFIG_PATH);

        if (!file.canRead()) {
            logger.error("Die Konfigurationsdatei {} konnte nicht geladen werden", file.getAbsoluteFile());
        }

        if (singleton != null) {
            return singleton;
        }
        try (FileReader in = new FileReader(new File(SystemInformationenImpl.get().getConfigFolder(),CONFIG_PATH))) {
            Properties props = new Properties();
            props.load(in);
            logger.info("Der Alarmmonitor startet mit folgenden Einstellungen: " + props.toString());
            MainConfiguration cfg = ConfigFactory.create(MainConfiguration.class, props);
            singleton = cfg;
            //TODO testing senseful sets for config
        } catch (IOException e) {
            logger.error("Die Konfigurationsdatei kann nicht geladen werden", e);
        }
        return singleton;
    }


}
