package de.alarm_monitor.main;

import de.alarm_monitor.email.EMailQueue;
import de.alarm_monitor.ocr.OCRProcessor;
import de.alarm_monitor.ocr.PNGParser;
import de.alarm_monitor.exception.*;
import de.alarm_monitor.util.AlarmResetter;
import de.alarm_monitor.visual.IDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FaxProzessorImpl implements FaxProcessor {


    private final static String EINSATZMITTEL_KEY = "Einsatzmittel";
    private final static String SCHLAGWORT_KEY = "Schlagwort";
    private final static String ADRESSE_KEY = "Adresse";

    private final AlarmResetter alarmResetter;


    public static final String ALARMZEIT_KEY = "Alarmzeit";
    public static final String EINSATZNUMMER_KEY = "Einsatznummer";
    public static final String MITTEILER_KEY = "Mitteiler";
    public static final String BEMERKUNG_KEY = "Bemerkung";
    public static final String ROUTING_LINK_KEY = "Routing";



    private static final Logger logger = LogManager.getLogger(FaxProzessorImpl.class);

    private File pdf;

    private final Boolean shouldSendEmails;
    private final Boolean shouldFilterEinsatzMittel;
    private final String filter;
    private final MainConfiguration configuration;

    @Inject
    FaxProzessorImpl(AlarmResetter alarmResetter) {
        this.alarmResetter = alarmResetter;
        configuration = MainConfigurationLoader.getConfig();
        this.shouldSendEmails = configuration.isEmailActive();

        if(configuration.should_filter_einsatzmittel()){
            if(configuration.filter_einsatzmittel().length() > 2){
                shouldFilterEinsatzMittel = true;
            }else{
                logger.error("Filter der Einsatzmittel wurde deaktiviert, da kein Filter gesetzt wurde");
                shouldFilterEinsatzMittel = false;
            }
        }else{
            shouldFilterEinsatzMittel = false;
        }
        this.filter = configuration.filter_einsatzmittel();
    }

    @Override
    public void processAlarmFax(File pdf) {
        this.pdf = pdf;
        String pathPng;
        try {
            pathPng = pdfToPng(pdf);
            String text = extractTextOfPng(pathPng);
            logger.trace("Parsed Text:\n"+text);
            Map<String, String> informationen = analyzeText(text);
            try{
                updateDisplay(informationen);
            }catch (DisplayChangeException e) {
                logger.error("Fehler beim Update der Bildschirmanzeige");
                logger.trace("Ursprüngliche Exception:", e);
            }


            try{
                addLinkToInformation(informationen);
            }catch (LinkCreationException e) {
                logger.error("Fehler beim Erstellen des Routing Links. Führe Verarbeitung fort.");
                logger.trace("Ursprüngliche Exception:", e);
            }


            if (shouldSendEmails) {
                sendEmail(informationen);
            }
        } catch (PngParserException e) {
            logger.error("Fehler beim Umwandeln der .pdf-Datei in eine .png-Datei");
            logger.trace("Ursprüngliche Exception:", e);
        } catch (OcrParserException e) {
            logger.error("Fehler beim OCR der .png-Datei");
            logger.trace("Ursprüngliche Exception:", e);
        } catch (EMailSendException e) {
            logger.error("Fehler beim Versenden der Emails");
            logger.trace("Ursprüngliche Exception:", e);
        }
    }


    private String pdfToPng(File pdf) throws PngParserException {
        try {
            return PNGParser.parsePdfToPng(pdf);
        } catch (Exception e) {
            throw new PngParserException();
        }
    }


    private String extractTextOfPng(String pathPng) throws OcrParserException {
        OCRProcessor ocrProcessor = new OCRProcessor();
        try {
            return ocrProcessor.getOCROfFile(pathPng);
        } catch (Exception e) {
            throw new OcrParserException(e);
        }
    }


    private Map<String, String> analyzeText(String fax) {

        String[] lines = fax.split("\n");

        StringBuilder schlagwort = new StringBuilder();
        StringBuilder adresse = new StringBuilder();
        StringBuilder mittel = new StringBuilder();
        StringBuilder koordinate = new StringBuilder();
        StringBuilder einsatzNummer = new StringBuilder();
        StringBuilder alarmZeit = new StringBuilder();
        StringBuilder mitteiler = new StringBuilder();
        StringBuilder bemerkung = new StringBuilder();

        Map<String, String> informationen = new HashMap<>();


        boolean bemerkungSeen = false;
        String previousLine ="";
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

                if(!shouldFilterEinsatzMittel){
                    mittel.append(line).append("\n");
                }else{
                    if(line.contains(filter)){
                        mittel.append(line).append("\n");
                    }

                    if(line.toLowerCase().contains("ger")){
                        if(previousLine.contains(filter)){
                            mittel.append(line);
                        }

                    }
                }





            }

            if (splitted[0].contains("Koordinate")) {
                koordinate.append(line).append("\n");
            }

            if (splitted[0].contains("Stra") | splitted[0].contains("Abschnitt") | splitted[0].contains("Ort") | splitted[0].contains("Objekt")) {
                adresse.append(line).append("\n");
            } else if (splitted[0].contains("Schlagw")) {
                schlagwort.append(line).append("\n");
            }

            if (bemerkungSeen) {
                bemerkung.append(line);
            }

            if (line.contains("BEMERKUNG")) {
                bemerkungSeen = true;
            }
            previousLine = line;
        }

        //for information
        logger.debug("Grund: " + schlagwort);
        logger.debug("Adresse: " + adresse);
        logger.debug("Koordidnate : " + koordinate);
        logger.debug("Bemerkung: " + bemerkung);
        logger.debug("Mittel: " + mittel);




        informationen.put(SCHLAGWORT_KEY, schlagwort.toString());
        informationen.put(ADRESSE_KEY, adresse.toString());
        informationen.put(EINSATZMITTEL_KEY, mittel.toString());
        informationen.put(ALARMZEIT_KEY, alarmZeit.toString());
        informationen.put(EINSATZNUMMER_KEY, einsatzNummer.toString());
        informationen.put(MITTEILER_KEY, mitteiler.toString());
        informationen.put(BEMERKUNG_KEY, bemerkung.toString());
        return informationen;
    }


    private void updateDisplay(Map<String, String> informationen) throws DisplayChangeException {

        alarmResetter.resetAlarm(1000*60);
        try {
            IDisplay display = Start.getDisplay();
            display.changeSchlagwort(informationen.get(SCHLAGWORT_KEY));
            display.changeAdresse(informationen.get(ADRESSE_KEY));
            display.changeEinsatzmittel(informationen.get(EINSATZMITTEL_KEY));
            display.changeAlarmzeit(informationen.get(ALARMZEIT_KEY));
            display.ChangeEinsatznummer(informationen.get(EINSATZNUMMER_KEY));
            display.changeName(informationen.get(MITTEILER_KEY));
            display.changeBemerkung(informationen.get(BEMERKUNG_KEY));

        } catch (Exception e) {
            throw new DisplayChangeException(e);
        }


    }


    private void sendEmail(Map<String, String> informationen) throws EMailSendException {
        StringBuilder email = new StringBuilder();
        email.append("Informationen zum Alarm\n");
        email.append(informationen.get(EINSATZNUMMER_KEY)).append("\n");
        email.append(informationen.get(ALARMZEIT_KEY)).append("\n");
        email.append(informationen.get(MITTEILER_KEY)).append("\n");
        email.append(informationen.get(SCHLAGWORT_KEY)).append("\n");
        email.append(informationen.get(BEMERKUNG_KEY)).append("\n");
        email.append(informationen.get(ADRESSE_KEY)).append("\n");
        email.append(informationen.get(EINSATZMITTEL_KEY)).append("\n");
        email.append("Link zum Routenplaner von Google:\n");
        email.append(informationen.get(ROUTING_LINK_KEY)).append("\n");

        try {
            EMailQueue queue = new EMailQueue();
            queue.broadcast(email.toString());
        } catch (Exception e) {
            throw new EMailSendException();
        }
    }


    void addLinkToInformation( Map<String, String> informationen) throws LinkCreationException {
        try{
           String link =  AddressFinder.createLink(informationen.get(ADRESSE_KEY));
           informationen.put(ROUTING_LINK_KEY, link);
        }catch(Exception e){
            throw new LinkCreationException(e.getMessage());
        }
    }


}
