package de.alarm_monitor.extracting;


import de.alarm_monitor.main.AlarmFax;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class ExtractorImpl implements Extractor {

    private static final Logger logger = LogManager.getLogger(ExtractorImpl.class);
    private final Boolean shouldFilterEinsatzMittel;
    private final MainConfiguration configuration;
    private final String filter;

    @Inject
    ExtractorImpl() {

        configuration = MainConfigurationLoader.getConfig();
        if (configuration.should_filter_einsatzmittel()) {
            if (configuration.filter_einsatzmittel().length() > 2) {
                shouldFilterEinsatzMittel = true;
            } else {
                logger.error("Filter der Einsatzmittel wurde deaktiviert, da kein Filter gesetzt wurde");
                shouldFilterEinsatzMittel = false;
            }
        } else {
            shouldFilterEinsatzMittel = false;
        }
        this.filter = configuration.filter_einsatzmittel();
    }


    @Override
    public AlarmFax extractInformation(String recognizedText) {

        String[] lines = recognizedText.split("\n");

        StringBuilder keyWord = new StringBuilder();
        StringBuilder address = new StringBuilder();
        StringBuilder operationRessources = new StringBuilder();
        StringBuilder koordinate = new StringBuilder();
        StringBuilder operatioNumber = new StringBuilder();
        StringBuilder alarmTime = new StringBuilder();
        StringBuilder reporter = new StringBuilder();
        StringBuilder comment = new StringBuilder();


        boolean bemerkungSeen = false;
        String previousLine = "";
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
            reporter.append(extractSender(line, splitted[0]));

            //Einsatzmittel
            if (splitted[0].contains("mittel") | (splitted.length > 1 && splitted[1].toLowerCase().contains("ger"))) {

                if (!shouldFilterEinsatzMittel) {
                    operationRessources.append(line).append("\n");
                } else {
                    if (line.contains(filter)) {
                        operationRessources.append(line).append("\n");
                    }

                    if (line.toLowerCase().contains("ger")) {
                        if (previousLine.contains(filter)) {
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

    private String extractSender(String line, String startLine) {
        if (startLine.contains("Name")) {
            return line;
        }else{
            return "";
        }
    }
}
