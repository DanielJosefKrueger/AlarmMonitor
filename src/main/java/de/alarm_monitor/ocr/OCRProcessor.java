package de.alarm_monitor.ocr;


import de.alarm_monitor.test.BadOrcFileException;
import de.alarm_monitor.main.MainConfiguration;
import de.alarm_monitor.main.MainConfigurationLoader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OCRProcessor {

    private final static Logger log = LoggerFactory.getLogger(OCRProcessor.class);
    private static String tPath = null;
    private MainConfiguration configuration;
    private HashMap<String, String> mapping = null;

    public OCRProcessor() {
        configuration = MainConfigurationLoader.getConfig();
    }


    public String getOCROfFile(String filename) throws IOException, TikaException, SAXException {


        if (tPath == null) {
            tPath = configuration.path_tesseract();
        }

        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        TesseractOCRConfig tesseractOCRConfig = new TesseractOCRConfig();
        tesseractOCRConfig.setTesseractPath(tPath);


        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true);
        pdfConfig.setExtractUniqueInlineImagesOnly(false); // set to false if
        // pdf contains
        // multiple images.
        ParseContext parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, tesseractOCRConfig);
        parseContext.set(PDFParserConfig.class, pdfConfig);
        // need to add this to make sure recursive parsing happens!
        parseContext.set(Parser.class, parser);

        FileInputStream stream = new FileInputStream(filename);
        Metadata metadata = new Metadata();
        parser.parse(stream, handler, metadata, parseContext);
        String content = handler.toString();

        content = correctOCR(content);

        return content;
    }

    private  String correctOCR(String text) {
        if (mapping == null) {
            initiateOcrMapping();
        }

        for (Map.Entry<String, String> e : mapping.entrySet()) {
            text = text.replaceAll(e.getKey(), e.getValue());
        }
        return text;

    }

    private void initiateOcrMapping() {

        if (mapping == null) {


            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(configuration.path_ocr()), "Cp1252"))) {


                mapping = new HashMap<>();
                String line = in.readLine();
                while (line != null) {
                    if (line.length() < 2) {
                        line = in.readLine();
                        continue;
                    }
                    String[] splitted = line.split(" ");
                    if (splitted.length != 2) {
                        throw new BadOrcFileException("Fehler in ORC-File");
                    }
                    mapping.put(splitted[0], splitted[1]);
                    line = in.readLine();
                }
            } catch (IOException | BadOrcFileException e) {
                log.error("", e);
            }

        }

    }

}
