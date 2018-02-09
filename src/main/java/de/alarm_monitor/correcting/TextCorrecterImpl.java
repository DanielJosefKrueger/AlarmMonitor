package de.alarm_monitor.correcting;

import com.google.inject.Inject;
import de.alarm_monitor.exception.CorrectingException;
import de.alarm_monitor.main.SystemInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TextCorrecterImpl implements TextCorrecter {

    private final static Logger logger = LogManager.getLogger(TextCorrecterImpl.class);
    private final SystemInformation systemInformation;
    private HashMap<String, String> mapping = null;

    @Inject
    public TextCorrecterImpl(SystemInformation systemInformation) {
        this.systemInformation = systemInformation;
    }


    @Override
    public String correct(String text) throws CorrectingException {

        if (mapping == null) {
            try {
                initiateOcrMapping();
            } catch (IOException e) {
                throw new CorrectingException(e);
            }
        }

        for (Map.Entry<String, String> e : mapping.entrySet()) {
            text = text.replaceAll(e.getKey(), e.getValue());
        }
        return text;
    }

    private void initiateOcrMapping() throws IOException {
        logger.trace("OCR Korrektur wird initialisiert");
        if (mapping == null) {
            String file = systemInformation.getConfigFolder().getPath() + File.separator + "ocr.txt";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
                mapping = new HashMap<>();
                String line = in.readLine();
                while (line != null) {
                    if (line.length() < 2) {
                        line = in.readLine();
                        continue;
                    }
                    String[] splitted = line.split("\\$");
                    if (splitted.length != 2) {
                        logger.warn("Fehler in der OCR Datei, zu wenig oder zu viele Einträge in einer Zeile. Eintrag wird Übersprungen. Zeile war: {}", line);
                        line = in.readLine();
                        continue;
                    }
                    mapping.put(splitted[0], splitted[1]);
                    line = in.readLine();
                }
            }
        }
    }
}
