package de.alarm_monitor.extracting;


import com.google.inject.Provider;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.main.AlarmFax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;

public class ExtractorImpl implements Extractor {

    private static final Logger logger = LogManager.getLogger(ExtractorImpl.class);
    private final Boolean filterOperationResources;
    private final String filter;
    private final MainConfiguration mainConfiguration;

    @Inject
    public ExtractorImpl(Provider<MainConfiguration> mainConfigurationProvider) {

         this.mainConfiguration = mainConfigurationProvider.get();
        if (mainConfiguration.should_filter_einsatzmittel()) {
            if (mainConfiguration.filter_einsatzmittel().length() > 2) {
                filterOperationResources = true;
            } else {
                logger.error("Filter der Einsatzmittel wurde deaktiviert, da kein Filter gesetzt wurde");
                filterOperationResources = false;
            }
        } else {
            filterOperationResources = false;
        }
        this.filter = mainConfiguration.filter_einsatzmittel();
    }


    @Override
    public AlarmFax extractInformation(String recognizedText) {

        String[] allLines = (recognizedText.split("\n"));
        ArrayList<String> lineList = new ArrayList<>();
        for (String line : allLines) {
            if (line.length() > 5) {
                lineList.add(line);
            }
        }

        String[] lines = new String[lineList.size()];
        lineList.toArray(lines);

        String address = extractAddress(lines);
        String comment = extractComment(lines);
        String keyWord = extractKeyword(lines);
        String reporter = extractReporter(lines);
        String coordinates = extractCoordinates(lines);
        String operationResources = extractOperationRessources(lines);
        String operationNumber = extractOperationNumber(lines);
        String alarmTime = extractAlarmtime(lines);


        //for information
        logger.debug("Grund: " + keyWord);
        logger.debug("Adresse: " + address);
        logger.debug("Koordidnate : " + coordinates);
        logger.debug("Bemerkung: " + comment);
        logger.debug("Mittel: " + operationResources);


        AlarmFax alarmFax = new AlarmFax();
        alarmFax.setKeyword(keyWord);
        alarmFax.setAddress(address);
        alarmFax.setOperationRessources(operationResources);
        alarmFax.setAlarmTime(alarmTime);
        alarmFax.setOperatioNumber(operationNumber);
        alarmFax.setReporter(reporter);
        alarmFax.setComment(comment);
        return alarmFax;
    }


    private String extractReporter(String[] lines) {
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String[] splitted = line.split(" ");

            if (splitted[0].contains("Name")) {
                sb.append(line);
            }

        }
        return sb.toString();
    }


    private String extractComment(String[] lines) {
        StringBuilder sb = new StringBuilder();
        boolean aignalSeen = false;
        for (String line : lines) {
            if (aignalSeen) {
                sb.append(line).append("\n");
            }
            if (line.contains("BEMERKUNG")) {
                aignalSeen = true;
            }
        }
        return sb.toString();
    }


    private String extractKeyword(String[] lines) {
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String[] splitted = line.split(" ");


            if (splitted[0].contains("Schlagw")) {
                sb.append(line).append("\n");
            }

        }
        return sb.toString();
    }

    private String extractAddress(String[] lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String[] splitted = line.split(" ");

            if (splitted[0].contains("Stra") | splitted[0].contains("Abschnitt") | splitted[0].contains("Ort") | splitted[0].contains("Objekt")) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private String extractCoordinates(String[] lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String[] splitted = line.split(" ");
            if (splitted[0].contains("Koordinate")) {
                sb.append(line).append("\n");
            }

        }
        return sb.toString();
    }

    private String extractOperationNumber(String[] lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String[] splitted = line.split(" ");

            if (splitted[0].contains("Einsatznu")) {
                int beginAlarmPart = line.indexOf("Alarm");
                sb.append(line.subSequence(0, beginAlarmPart));
            }
        }
        return sb.toString();
    }


    private String extractAlarmtime(String[] lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String[] splitted = line.split(" ");

            if (splitted[0].contains("Einsatznu")) {
                int beginAlarmPart = line.indexOf("Alarm");
                sb.append(line.subSequence(beginAlarmPart, line.length()));
            }
        }
        return sb.toString();
    }


    private String extractOperationRessources(String[] lines) {
        StringBuilder sb = new StringBuilder();


        String previousLine = "";

        for (String line : lines) {
            String[] splitted = line.split(" ");

            if (splitted[0].contains("mittel") | (splitted.length > 1 && splitted[1].toLowerCase().contains("ger"))) {

                if (!filterOperationResources) {
                    sb.append(line).append("\n");
                } else {
                    if (line.contains(filter)) {
                        sb.append(line).append("\n");
                    }

                    if (line.toLowerCase().contains("ger")) {
                        if (previousLine.contains(filter)) {
                            sb.append(line);
                        }

                    }
                }
            }
            previousLine = line;
        }
        return sb.toString();
    }

}
