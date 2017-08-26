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
                String filePng = PNGParser.parsePdfToPng(pdf);
                OCRProcessor ocrProcessor = new OCRProcessor();

                String text = null;
                try {
                    text = ocrProcessor.getOCROfFile(filePng);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TikaException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                System.out.println(text);
                processAlarmFDax(text);
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


    public static void processAlarmFDax(String fax) {

        String[] lines = fax.split("\n");
        StringBuilder info = new StringBuilder();
        StringBuilder schlagwort = new StringBuilder();
        StringBuilder adress = new StringBuilder();
        StringBuilder mittel = new StringBuilder();
        StringBuilder koordinate = new StringBuilder();
        StringBuilder einsatzNummer = new StringBuilder();
        StringBuilder alarmZeit = new StringBuilder();
        StringBuilder mitteiler = new StringBuilder();
        StringBuilder bemerkung = new StringBuilder();


        IDisplay display = Start.getDisplay();

        String prevLine = "";
        boolean bemerkungSeen = false;

        for (String line : lines) {
            String[] splitted = line.split(" ");

            if (splitted.length == 0) {
                continue;
            }


            //Einsatznummer und Alarmzeit
            if (splitted[0].contains("Einsatznu")) {

                int beginAlarmPart = line.indexOf("Alarm");
                einsatzNummer.append(line.subSequence(0, beginAlarmPart));
                alarmZeit.append(line.subSequence(beginAlarmPart, line.length()));
            }

            //mitteiler
            if (splitted[0].contains("Name")) {
                mitteiler.append(line);
            }

            //Einsatzmittel
            if (splitted[0].contains("mittel") | (splitted.length > 1 && splitted[1].toLowerCase().contains("ger"))) {
                mittel.append(line + "\n");
            }

            if (splitted[0].contains("Koordinate")) {
                koordinate.append(line + "\n");
            }

            if (splitted[0].contains("Stra") | splitted[0].contains("Abschnitt") | splitted[0].contains("Ort") | splitted[0].contains("Objekt")) {
                adress.append(line + "\n");
            } else if (splitted[0].contains("Schlagw")) {
                schlagwort.append(line + "\n");
            }

            if (bemerkungSeen) {
                bemerkung.append(line);
            }

            if (line.contains("BEMERKUNG")) {
                bemerkungSeen = true;
            }
            prevLine = line;
        }

        //for information
        System.out.println("Grund:");
        System.out.println(schlagwort);
        System.out.println("Adresse:");
        System.out.println(adress);
        System.out.println("Koordinate");
        System.out.println(koordinate);
        System.out.println("Informationen:");
        System.out.println(info);
        System.out.println("Mittel:");
        System.out.println(mittel);

        //changing AlarmMonitor
        display.changeSchlagwort(schlagwort.toString());
        display.changeAdresse(adress.toString());
        display.changeEinsatzmittel(mittel.toString());
        display.changeBemerkung(fax);
        display.changeAlarmzeit(alarmZeit.toString());
        display.ChangeEinsatznummer(einsatzNummer.toString());
        display.changeName(mitteiler.toString());
        display.changeBemerkung(bemerkung.toString());


        StringBuilder email = new StringBuilder();
        email.append("Informationen zum Alarm\n");

        email.append(einsatzNummer + "\n");
        email.append(alarmZeit + "\n");
        email.append(mitteiler + "\n");
        email.append(schlagwort + "\n");
        email.append(bemerkung + "\n");
        email.append(adress + "\n");
        email.append(mittel + "\n");

        EMailQueue queue = new EMailQueue();
        queue.broadcast(email.toString(), "TestPdf");


    }
}



