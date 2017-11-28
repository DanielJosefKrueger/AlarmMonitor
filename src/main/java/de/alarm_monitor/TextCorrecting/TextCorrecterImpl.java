package de.alarm_monitor.TextCorrecting;

import de.alarm_monitor.exception.CorrectingException;
import de.alarm_monitor.main.SystemInformationenImpl;
import de.alarm_monitor.parsing.OCRProcessorImpl1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TextCorrecterImpl implements TextCorrecter{

    private final static Logger logger = LogManager.getLogger(TextCorrecterImpl.class);
    private HashMap<String, String> mapping = null;

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

        if (mapping == null) {
            String file = SystemInformationenImpl.get().getConfigFolder().getPath() + File.separator + "ocr.txt";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
                mapping = new HashMap<>();
                String line = in.readLine();
                while (line != null) {
                    if (line.length() < 2) {
                        line = in.readLine();
                        continue;
                    }
                    String[] splitted = line.split(" ");
                    if (splitted.length != 2) {
                        logger.warn("Fehler in der OCR Datei, zu wenig oder zu viele Einträge in einer Zeile. Eintrag wird Übersprungen");
                        continue;
                    }
                    mapping.put(splitted[0], splitted[1]);
                    line = in.readLine();
                }
            }
        }
    }
}
