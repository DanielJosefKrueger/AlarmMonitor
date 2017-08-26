package de.alarm_monitor.main;

import de.alarm_monitor.email.EMailConfigurationLoader;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainConfigurationLoader {

    public final static String CONFIG_PATH = "config.properties";
    final static Logger log = LoggerFactory.getLogger(EMailConfigurationLoader.class);
    private static MainConfiguration singleton;

    private MainConfigurationLoader() {
    }

    public static MainConfiguration getConfig() {

        File file = new File(CONFIG_PATH);

        if (!file.canRead()) {
            log.error("Die Konfigurationsdatei {} konnte nicht geladen werden", file.getAbsoluteFile());
        }

        if (singleton != null) {
            return singleton;
        }
        try (FileReader in = new FileReader(new File(CONFIG_PATH))) {
            Properties props = new Properties();
            props.load(in);
            System.out.println(props);
            MainConfiguration cfg = ConfigFactory.create(MainConfiguration.class, props);
            singleton = cfg;
            //TODO testing senseful sets for config
        } catch (IOException e) {
            log.error("Die Konfigurationsdatei kann nicht geladen werden", e);
        }
        return singleton;
    }


}
