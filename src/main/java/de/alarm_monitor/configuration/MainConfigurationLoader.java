package de.alarm_monitor.configuration;

import com.google.inject.Inject;
import com.google.inject.Provides;
import de.alarm_monitor.email.EMailConfigurationLoader;
import de.alarm_monitor.main.SystemInformation;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class MainConfigurationLoader {

    public final static String CONFIG_PATH = "config.properties";
    final static Logger logger = LogManager.getLogger(EMailConfigurationLoader.class);
    @Inject
    static SystemInformation systemInformation;

    @Provides
    public static MainConfiguration getConfig() {
        File file = new File(systemInformation.getConfigFolder(), CONFIG_PATH);
        if (!file.canRead()) {
            logger.error("Die Konfigurationsdatei {} konnte nicht geladen werden", file.getAbsoluteFile());
        }
        return ConfigCache.getOrCreate(MainConfiguration.class);
    }
}
