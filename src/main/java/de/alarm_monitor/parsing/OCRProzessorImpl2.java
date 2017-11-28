package de.alarm_monitor.parsing;


import com.asprise.ocr.Ocr;
import de.alarm_monitor.exception.OcrParserException;
import de.alarm_monitor.main.SystemInformationenImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OCRProzessorImpl2 implements OCRProcessor {

    private final static Logger logger = LogManager.getLogger(OCRProzessorImpl2.class);
    private HashMap<String, String> mapping = null;



    @Override
    public String pdfToString(File pdf) throws OcrParserException  {
        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_SLOW); // English
        String s = null;
        try {
            s = ocr.recognize(new File[] {new File(PngConverter.convertToPng(pdf))},
                    Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        } catch (IOException e) {
            throw new OcrParserException(e);
        }

        try {
            return correctOCR(s);
        } catch (IOException e) {
            throw new OcrParserException(e);
        }
    }


    private String correctOCR(String text) throws IOException {
        if (mapping == null) {
            initiateOcrMapping();
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




    public static void main(String[] args){

        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_SLOW); // English
        String s = ocr.recognize(new File[] {new File("test.png")},
                Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT); // PLAINTEXT | XML | PDF | RTF
        System.out.println("Result: " + s);


        System.out.println("\n\n ALS PDF \n\n");
        String s1 = ocr.recognize("test.pdf", -1, -1, -1, -1, -1, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        System.out.println("Result: " + s1);




        ocr.stopEngine();


    }
}
