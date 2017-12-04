package de.alarm_monitor.configuration;

import com.google.inject.Inject;
import com.google.inject.Provides;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.email.EMailConfigurationLoader;
import de.alarm_monitor.main.SystemInformationen;
import de.alarm_monitor.main.SystemInformationenImpl;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class MainConfigurationLoader {

    public final static String CONFIG_PATH = "config.properties";
    final static Logger logger = LogManager.getLogger(EMailConfigurationLoader.class);
    @Inject
    static SystemInformationen systemInformationen;

    @Provides
    public static MainConfiguration getConfig() {
        File file = new File(systemInformationen.getConfigFolder(), CONFIG_PATH);
        if (!file.canRead()) {
            logger.error("Die Konfigurationsdatei {} konnte nicht geladen werden", file.getAbsoluteFile());
        }
        return ConfigCache.getOrCreate(MainConfiguration.class);
    }
}
