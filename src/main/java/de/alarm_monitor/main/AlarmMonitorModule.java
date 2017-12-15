package de.alarm_monitor.main;

import com.google.inject.AbstractModule;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import de.alarm_monitor.correcting.TextCorrecter;
import de.alarm_monitor.correcting.TextCorrecterImpl;
import de.alarm_monitor.email.EMailConfiguration;
import de.alarm_monitor.email.EMailConfigurationLoader;
import de.alarm_monitor.extracting.Extractor;
import de.alarm_monitor.extracting.ExtractorImpl;
import de.alarm_monitor.parsing.OCRProcessor;
import de.alarm_monitor.parsing.OCRProcessorImpl1;


public class AlarmMonitorModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(OCRProcessor.class).to(OCRProcessorImpl1.class);
        bind(TextCorrecter.class).to(TextCorrecterImpl.class);
        bind(Extractor.class).to(ExtractorImpl.class);
        bind(SystemInformation.class).to(SystemInformationImpl.class);
        bind(MainConfiguration.class).toProvider(MainConfigurationLoader.class);
        bind(EMailConfiguration.class).toProvider(EMailConfigurationLoader.class);
    }
}
