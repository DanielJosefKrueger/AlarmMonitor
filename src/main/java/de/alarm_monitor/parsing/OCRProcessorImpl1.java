package de.alarm_monitor.parsing;


import com.google.inject.Inject;
import de.alarm_monitor.exception.OcrParserException;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import de.alarm_monitor.main.SystemInformationen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class OCRProcessorImpl1 implements OCRProcessor {

    private final static Logger logger = LogManager.getLogger(OCRProcessorImpl1.class);
    private static String tPath = null;
    private MainConfiguration configuration;
    private HashMap<String, String> mapping = null;
    private final SystemInformationen systemInformationen;
    private final PngConverter pngConverter;

    @Inject
    public OCRProcessorImpl1(SystemInformationen systemInformationen, PngConverter pngConverter) {
        this.systemInformationen = systemInformationen;
        this.pngConverter = pngConverter;
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
        tesseractOCRConfig.setLanguage(MainConfigurationLoader.getConfig().getOcrPacket());
        logger.trace("Used language for OCR: " + tesseractOCRConfig.getLanguage());
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


        return content;
    }


    @Override
    public String pdfToString(File pdf) throws OcrParserException {
        try {
            return getOCROfFile(pngConverter.convertToPng(pdf));
        } catch (Exception e) {
            throw new OcrParserException(e);
        }
    }
}
