package de.alarm_monitor.main;


import de.alarm_monitor.callback.NewPdfCallback;
import de.alarm_monitor.visual.AlarmMonitorGridBag;
import de.alarm_monitor.visual.IDisplay;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;


import java.io.File;

public class Start {
    private static Logger logger;
    static private IDisplay display;
    static private SystemInformationen systemInformationen;

    public static void main(String[] args) {

        System.out.println(

        );


        startProcedure();
        Observer obs = new Observer();
        obs.start();
        NewPdfCallback callback = new NewPdfCallback() {
            @Override
            public void onNewPdfFile(File pdf) {
                FaxProcessor processor = new FaxProzessorImpl();
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


        Configurator.initialize(null, "config/logconfig.xml");
        logger = LogManager.getLogger(FaxProzessorImpl.class);
        logger.info("Using own configuration file for logging");

        display = new AlarmMonitorGridBag();
        systemInformationen = new SystemInformationenImpl();
    }


}



