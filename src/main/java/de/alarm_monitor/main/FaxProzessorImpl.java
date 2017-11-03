package de.alarm_monitor.main;

import de.alarm_monitor.email.EMailList;
import com.google.inject.Inject;
import de.alarm_monitor.email.EMailQueue;
import de.alarm_monitor.ocr.OCRProcessor;
import de.alarm_monitor.ocr.PngConverter;
import de.alarm_monitor.exception.*;
import de.alarm_monitor.util.AddressFinder;
import de.alarm_monitor.util.AlarmResetter;
import de.alarm_monitor.visual.IDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



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

            AlarmFax alarmFax= analyzeText(text);


            try{
                updateDisplay(alarmFax);
            }catch (DisplayChangeException e) {
                logger.error("Fehler beim Update der Bildschirmanzeige");
                logger.trace("Ursprüngliche Exception:", e);
            }


            try{
                addLinkToInformation(alarmFax);
            }catch (LinkCreationException e) {
                logger.error("Fehler beim Erstellen des Routing Links. Führe Verarbeitung fort.");
                logger.trace("Ursprüngliche Exception:", e);
            }


            if (shouldSendEmails) {
                sendEmail(alarmFax);
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
            return PngConverter.convertToPng(pdf);
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


    private AlarmFax analyzeText(String fax) {

        String[] lines = fax.split("\n");

        StringBuilder keyWord = new StringBuilder();
        StringBuilder address = new StringBuilder();
        StringBuilder operationRessources = new StringBuilder();
        StringBuilder koordinate = new StringBuilder();
        StringBuilder operatioNumber = new StringBuilder();
        StringBuilder alarmTime = new StringBuilder();
        StringBuilder reporter = new StringBuilder();
        StringBuilder comment = new StringBuilder();

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
                operatioNumber.append(line.subSequence(0, beginAlarmPart));
                alarmTime.append(line.subSequence(beginAlarmPart, line.length()));
            }

            //mitteiler
            if (splitted[0].contains("Name")) {
                reporter.append(line);
            }

            //Einsatzmittel
            if (splitted[0].contains("mittel") | (splitted.length > 1 && splitted[1].toLowerCase().contains("ger"))) {

                if(!shouldFilterEinsatzMittel){
                    operationRessources.append(line).append("\n");
                }else{
                    if(line.contains(filter)){
                        operationRessources.append(line).append("\n");
                    }

                    if(line.toLowerCase().contains("ger")){
                        if(previousLine.contains(filter)){
                            operationRessources.append(line);
                        }

                    }
                }
            }

            if (splitted[0].contains("Koordinate")) {
                koordinate.append(line).append("\n");
            }

            if (splitted[0].contains("Stra") | splitted[0].contains("Abschnitt") | splitted[0].contains("Ort") | splitted[0].contains("Objekt")) {
                address.append(line).append("\n");
            } else if (splitted[0].contains("Schlagw")) {
                keyWord.append(line).append("\n");
            }

            if (bemerkungSeen) {
                comment.append(line);
            }

            if (line.contains("BEMERKUNG")) {
                bemerkungSeen = true;
            }
            previousLine = line;
        }

        //for information
        logger.debug("Grund: " + keyWord);
        logger.debug("Adresse: " + address);
        logger.debug("Koordidnate : " + koordinate);
        logger.debug("Bemerkung: " + comment);
        logger.debug("Mittel: " + operationRessources);




        AlarmFax alarmFax = new AlarmFax();
        alarmFax.setKeyword(keyWord.toString());
        alarmFax.setAddress(address.toString());
        alarmFax.setOperationRessources(operationRessources.toString());
        alarmFax.setAlarmTime(alarmTime.toString());
        alarmFax.setOperatioNumber(operatioNumber.toString());
        alarmFax.setReporter(reporter.toString());
        alarmFax.setComment(comment.toString());


        return alarmFax;
    }


    private void updateDisplay(AlarmFax alarmFax) throws DisplayChangeException {

        alarmResetter.resetAlarm(1000*60);
        try {
            IDisplay display = Start.getDisplay();
            display.changeAlarmFax(alarmFax);


        } catch (Exception e) {
            throw new DisplayChangeException(e);
        }


    }


    private void sendEmail(AlarmFax alarmFax) throws EMailSendException {
        StringBuilder email = new StringBuilder();
        email.append("Informationen zum Alarm\n");
        email.append(alarmFax.getOperatioNumber()).append("\n");
        email.append(alarmFax.getAlarmTime()).append("\n");

        email.append(alarmFax.getReporter()).append("\n");
        email.append(alarmFax.getKeyword()).append("\n");
        email.append(alarmFax.getComment()).append("\n");
        email.append(alarmFax.getAddress()).append("\n");

        email.append(alarmFax.getOperationRessources()).append("\n");

        email.append("Link zum Routenplaner von Google:\n");
        email.append(alarmFax.getLink()).append("\n");

        try {
            EMailList queue = new EMailList();
            queue.broadcast(email.toString());
        } catch (Exception e) {
            throw new EMailSendException();
        }
    }


    void  addLinkToInformation( AlarmFax alarmFax) throws LinkCreationException {
        try{
           String link =  AddressFinder.createLink(alarmFax.getAddress());
           alarmFax.setLink(link);
        }catch(Exception e){
            throw new LinkCreationException(e.getMessage());
        }
    }


}
