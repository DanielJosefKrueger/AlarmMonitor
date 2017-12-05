package de.alarm_monitor.main;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.alarm_monitor.callback.NewPdfCallback;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import de.alarm_monitor.observing.Observer;
import de.alarm_monitor.printing.PrintingService;
import de.alarm_monitor.processing.FaxProcessor;
import de.alarm_monitor.processing.FaxProzessorImpl;
import de.alarm_monitor.security.AdminReporter;
import de.alarm_monitor.util.GraphicUtil;
import de.alarm_monitor.visual.IDisplay;
import de.alarm_monitor.visual.NewLayout;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import javax.swing.*;
import java.io.File;

public class Start {
    private static Logger logger;
    static private IDisplay display;
    static private SystemInformation systemInformation;


//TODO evtl pfad veränderung wenn nicht auas ordner gestartet


    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AlarmMonitorModule());
        systemInformation = injector.getInstance(SystemInformation.class);
        startProcedure();
        logger.info("Der Alarmmonitor startet");


        Observer obs = injector.getInstance(Observer.class);
        obs.start();

        AdminReporter reporter = injector.getInstance(AdminReporter.class);
        reporter.start();


        NewPdfCallback callback = pdf -> {
            new PrintingService(pdf).start();
            FaxProcessor processor = injector.getInstance(FaxProzessorImpl.class);
            processor.processAlarmFax(pdf);
        };
        obs.addCallback(callback);
    }

    public static IDisplay getDisplay() {
        return display;
    }

    private static void startProcedure() {
        Configurator.initialize(null, systemInformation.getConfigFolder().toURI().getPath() + "logconfig.xml");
        logger = LogManager.getLogger(FaxProzessorImpl.class);

        ConfigFactory.setProperty("mainconfig", new File(systemInformation.getConfigFolder(), "config.properties").toURI().getRawPath());
        ConfigFactory.setProperty("emailconfig", new File(systemInformation.getConfigFolder(), "email_config.properties").toURI().getRawPath());

        display = new NewLayout();
        GraphicUtil.showOnScreen(MainConfigurationLoader.getConfig().monitor(), (JFrame) display);

    }
}



