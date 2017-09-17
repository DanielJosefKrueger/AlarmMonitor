package de.alarm_monitor.main;


import de.alarm_monitor.callback.NewPdfCallback;
import de.alarm_monitor.email.EMailQueue;
import de.alarm_monitor.ocr.OCRProcessor;
import de.alarm_monitor.ocr.PNGParser;
import de.alarm_monitor.visual.AlarmMonitorGridBag;
import de.alarm_monitor.visual.IDisplay;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class Start {

    static private IDisplay display;

    public static void main(String[] args) {

        startProcedure();
        Observer obs = new Observer();
        obs.start();

        NewPdfCallback callback = new NewPdfCallback() {
            @Override
            public void onNewPdfFile(File pdf) {
            FaxProcessor processor = new FaxProzessorImpl();
            processor.prozessAlarmFax(pdf);
            }
        };


        obs.addCallback(callback);


    }


    public static IDisplay getDisplay() {

        return display;
    }


    public static void startProcedure() {
        display = new AlarmMonitorGridBag();
    }



}



