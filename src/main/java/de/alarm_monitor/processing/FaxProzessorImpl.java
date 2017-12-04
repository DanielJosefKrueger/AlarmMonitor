package de.alarm_monitor.processing;

import com.google.inject.Inject;
import de.alarm_monitor.correcting.TextCorrecter;
import de.alarm_monitor.extracting.Extractor;
import de.alarm_monitor.email.EMailList;
import de.alarm_monitor.exception.*;
import de.alarm_monitor.main.AlarmFax;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import de.alarm_monitor.main.Start;
import de.alarm_monitor.parsing.OCRProcessor;
import de.alarm_monitor.util.AddressFinder;
import de.alarm_monitor.util.AlarmResetter;
import de.alarm_monitor.visual.IDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class FaxProzessorImpl implements FaxProcessor {

    private static final Logger logger = LogManager.getLogger(FaxProzessorImpl.class);
    private final AlarmResetter alarmResetter;
    private final Boolean shouldSendEmails;

    private final MainConfiguration configuration;
    private final OCRProcessor ocrProcessor;
    private final TextCorrecter correcter;
    private final Extractor extractor;
    private final EMailList queue;

    @Inject
    FaxProzessorImpl(final AlarmResetter alarmResetter,
                     final OCRProcessor ocrProcessor,
                     final TextCorrecter correcter,
                     final Extractor extractor,
                     final EMailList queue) {


        this.alarmResetter = alarmResetter;
        this.ocrProcessor = ocrProcessor;
        this.correcter = correcter;
        this.extractor = extractor;
        this.queue = queue;
        configuration = MainConfigurationLoader.getConfig();
        this.shouldSendEmails = configuration.isEmailActive();


    }

    @Override
    public void processAlarmFax(File pdf) {
        String pathPng;
        try {
            String text = ocrProcessor.pdfToString(pdf);
            logger.trace("Parsed Text:\n" + text);
            try {
                text = correcter.correct(text);
            } catch (CorrectingException e) {
                logger.error("Fehler beim Korregieren des eingelesenen Textes, fahre ohne Verbesserung fort");
                logger.trace("Ursprüngliche Exception:", e);
            }

            logger.trace("Parsed Text:\n" + text);


            AlarmFax alarmFax = extractor.extractInformation(text);


            try {
                updateDisplay(alarmFax);
            } catch (DisplayChangeException e) {
                logger.error("Fehler beim Update der Bildschirmanzeige");
                logger.trace("Ursprüngliche Exception:", e);
            }


            try {
                addLinkToInformation(alarmFax);
            } catch (LinkCreationException e) {
                logger.error("Fehler beim Erstellen des Routing Links. Führe Verarbeitung fort.");
                logger.trace("Ursprüngliche Exception:", e);
            }


            if (shouldSendEmails) {
                sendEmail(alarmFax);
            }
        } catch (OcrParserException e) {
            logger.error("Fehler beim OCR der .png-Datei");
            logger.trace("Ursprüngliche Exception:", e);
        } catch (EMailSendException e) {
            logger.error("Fehler beim Versenden der Emails");
            logger.trace("Ursprüngliche Exception:", e);
        }
    }


    private void updateDisplay(AlarmFax alarmFax) throws DisplayChangeException {

        alarmResetter.resetAlarm(MainConfigurationLoader.getConfig().getMonitorResetTime());
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

            queue.broadcast(email.toString());
        } catch (Exception e) {
            throw new EMailSendException();
        }
    }


    private void addLinkToInformation(AlarmFax alarmFax) throws LinkCreationException {
        try {
            String link = AddressFinder.createLink(alarmFax.getAddress());
            alarmFax.setLink(link);
        } catch (Exception e) {
            throw new LinkCreationException(e.getMessage());
        }
    }


}
