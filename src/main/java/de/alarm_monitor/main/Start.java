package de.alarm_monitor.main;


import de.alarm_monitor.callback.NewPdfCallback;
import de.alarm_monitor.visual.AlarmMonitorGridBag;
import de.alarm_monitor.visual.IDisplay;

import java.io.File;

public class Start {

    static private IDisplay display;
    static private SystemInformationen systemInformationen;

    public static void main(String[] args) {

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
        display = new AlarmMonitorGridBag();
        systemInformationen = new SystemInformationenImpl();
    }


}



