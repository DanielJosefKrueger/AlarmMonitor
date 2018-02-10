package de.alarm_monitor.configuration;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.alarm_monitor.email.EMailConfigurationLoader;
import de.alarm_monitor.main.SystemInformation;
import org.aeonbits.owner.ConfigCache;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class MainConfigurationLoader implements Provider<MainConfiguration> {

    public final static String CONFIG_PATH = "config.properties";
    final static Logger logger = LogManager.getLogger(EMailConfigurationLoader.class);
    private final SystemInformation systemInformation;

    @Inject
    MainConfigurationLoader(final SystemInformation systemInformation) {
        ConfigFactory.setProperty("mainconfig", new File(systemInformation.getConfigFolder(), "config.properties").toURI().getRawPath());
        this.systemInformation = systemInformation;
    }


    private MainConfiguration getConfig() {
        File file = new File(systemInformation.getConfigFolder(), CONFIG_PATH);
        if (!file.canRead()) {
            logger.error("Die Konfigurationsdatei {} konnte nicht geladen werden", file.getAbsoluteFile());
        }
        return ConfigCache.getOrCreate(MainConfiguration.class);
    }

    @Override
    public MainConfiguration get() {
        return getConfig();
    }
}
