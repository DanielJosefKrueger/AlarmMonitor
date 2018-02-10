package de.alarm_monitor.email;


import com.google.inject.Inject;
import com.google.inject.Provider;
import de.alarm_monitor.main.SystemInformation;
import org.aeonbits.owner.ConfigCache;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class EMailConfigurationLoader implements Provider<EMailConfiguration> {

    public final static String EMAIL_CONFIG_FILR = "email_config.properties";
    public final static String EMAIL_List_PATH = "email_list.txt";
    final static Logger log = LogManager.getLogger(EMailConfigurationLoader.class);
    private final SystemInformation systemInformation;


    @Inject
    private EMailConfigurationLoader(SystemInformation systemInformation) {
        this.systemInformation = systemInformation;
        ConfigFactory.setProperty("emailconfig", new File(systemInformation.getConfigFolder(), EMAIL_CONFIG_FILR).toURI().getRawPath());
    }


    public EMailConfiguration get() {
        return ConfigCache.getOrCreate(EMailConfiguration.class);
    }

}
