package de.alarm_monitor.main;

import de.alarm_monitor.email.EMailQueue;
import de.alarm_monitor.ocr.OCRProcessor;
import de.alarm_monitor.ocr.PNGParser;
import de.alarm_monitor.test.DisplayChangeException;
import de.alarm_monitor.test.OcrParserException;
import de.alarm_monitor.test.PngParserException;
import de.alarm_monitor.test.eMailSendException;
import de.alarm_monitor.visual.IDisplay;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FaxProzessorImpl implements FaxProcessor {

    private final static String EINSATZMITTEL_KEY = "Einsatzmittel";
    private final static String SCHLAGWORT_KEY = "Schlagwort";
    private final static String ADRESSE_KEY = "Adresse";
    public static final String ALARMZEIT_KEY = "Alarmzeit";
    public static final String EINSATZNUMMER_KEY = "Einsatznummer";
    public static final String MITTEILER_KEY = "Mitteiler";
    public static final String BEMERKUNG_KEY = "Bemerkung";


    private File pdf;
    private static final Logger LOG = LogManager.getLogger(FaxProzessorImpl.class);

    @Override
    public void prozessAlarmFax(File pdf) {
        this.pdf = pdf;
        String pathPng = null;
        try {
            pathPng = pdfToPng(pdf);
            String text = extractTextOfPng(pathPng);
            Map<String, String> informationen =  analyzeText(text);
            sendEmail(emailText);

        } catch (PngParserException e) {
            //TODO
            e.printStackTrace();
        } catch (OcrParserException e) {
            //TODO
            e.printStackTrace();
        } catch (eMailSendException e) {
            //TODO
            e.printStackTrace();
        }



    }


    private String pdfToPng(File pdf) throws PngParserException {
        try{
            return PNGParser.parsePdfToPng(pdf);
        }catch(Exception e){
            throw new PngParserException();
        }
    }



    private String extractTextOfPng(String pathPng) throws OcrParserException {
        OCRProcessor ocrProcessor = new OCRProcessor();
        String text = null;
        try{
            return ocrProcessor.getOCROfFile(pathPng);
        }catch(Exception e){
            throw new OcrParserException(e);
        }
    }



    public Map<String, String> analyzeText(String fax) {

        String[] lines = fax.split("\n");
        StringBuilder info = new StringBuilder();
        StringBuilder schlagwort = new StringBuilder();
        StringBuilder adresse = new StringBuilder();
        StringBuilder mittel = new StringBuilder();
        StringBuilder koordinate = new StringBuilder();
        StringBuilder einsatzNummer = new StringBuilder();
        StringBuilder alarmZeit = new StringBuilder();
        StringBuilder mitteiler = new StringBuilder();
        StringBuilder bemerkung = new StringBuilder();

        Map<String, String> informationen = new HashMap<>();




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
                adresse.append(line + "\n");
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
        LOG.debug("Grund: "+ schlagwort);
        LOG.debug("Adresse: "+ adresse);
        LOG.debug("Koordidnate : "+ koordinate);
        LOG.debug("Information: "+ info);
        LOG.debug("Mittel: " + mittel);

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
try{
    IDisplay display = Start.getDisplay();
    display.changeSchlagwort(informationen.get(SCHLAGWORT_KEY));
    display.changeAdresse(informationen.get(ADRESSE_KEY));
    display.changeEinsatzmittel(informationen.get(EINSATZMITTEL_KEY));
    display.changeAlarmzeit(informationen.get(ALARMZEIT_KEY));
    display.ChangeEinsatznummer(informationen.get(EINSATZNUMMER_KEY));
    display.changeName(informationen.get(MITTEILER_KEY));
    display.changeBemerkung(informationen.get(BEMERKUNG_KEY));

}catch(Exception e){
    throw new DisplayChangeException(e);
}


    }


    private void sendEmail(Map<String, String> informationen) throws eMailSendException {
        StringBuilder email = new StringBuilder();
        email.append("Informationen zum Alarm\n");

        email.append(einsatzNummer + "\n");
        email.append(alarmZeit + "\n");
        email.append(mitteiler + "\n");
        email.append(schlagwort + "\n");
        email.append(bemerkung + "\n");
        email.append(adresse + "\n");
        email.append(mittel + "\n");
        return email.toString();

        try{
            EMailQueue queue = new EMailQueue();
            queue.broadcast(email.toString(), "TestPdf");
        }catch(Exception e){
            throw new eMailSendException();
        }


    }

}
