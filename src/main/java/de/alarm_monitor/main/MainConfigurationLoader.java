package de.alarm_monitor.main;

import com.google.inject.Provides;
import de.alarm_monitor.email.EMailConfigurationLoader;
import org.aeonbits.owner.ConfigCache;
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

    @Provides
    public static MainConfiguration getConfig() {
        File file = new File(SystemInformationenImpl.get().getConfigFolder(),CONFIG_PATH);
        if (!file.canRead()) {
            logger.error("Die Konfigurationsdatei {} konnte nicht geladen werden", file.getAbsoluteFile());
        }
        return ConfigCache.getOrCreate(MainConfiguration.class);
    }
}
