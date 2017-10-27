package de.alarm_monitor.main;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.alarm_monitor.callback.NewPdfCallback;
import de.alarm_monitor.visual.AlarmMonitorGridBag;
import de.alarm_monitor.visual.GraphicUtil;
import de.alarm_monitor.visual.IDisplay;


import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;


import javax.inject.Inject;
import javax.swing.*;
import java.io.File;

public class Start {
    private static Logger logger;
    static private IDisplay display;
    static private SystemInformationen systemInformationen;




    public static void main(String[] args) {

        startProcedure();
        logger.info("Der Alarmmonitor startet");
        Observer obs = new Observer();
        obs.start();
        NewPdfCallback callback = new NewPdfCallback() {
            @Override
            public void onNewPdfFile(File pdf) {
                Injector injector = Guice.createInjector();
                FaxProcessor processor = injector.getInstance(FaxProzessorImpl.class);
                new PrintingService(pdf).start();
                processor.processAlarmFax(pdf);
            }
        };
        obs.addCallback(callback);
    }

    public static IDisplay getDisplay() {
        return display;
    }

    public static void startProcedure() {

        //configurating the log4j2 logging
        Configurator.initialize(null, "config/logconfig.xml");
        logger = LogManager.getLogger(FaxProzessorImpl.class);
        logger.info("Die Logger-Konfiguration aus dem config Ordner wird verwendet");


        ConfigFactory.setProperty("mainconfig",new File( SystemInformationenImpl.get().getConfigFolder() , "config.properties").toURI().getRawPath());
        ConfigFactory.setProperty("emailconfig",new File( SystemInformationenImpl.get().getConfigFolder() , "email_config.properties").toURI().getRawPath());

        display = new AlarmMonitorGridBag();
        GraphicUtil.showOnScreen(MainConfigurationLoader.getConfig().monitor(), (JFrame)display);
        systemInformationen = new SystemInformationenImpl();
    }
}



