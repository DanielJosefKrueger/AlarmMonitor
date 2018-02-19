package de.alarm_monitor.parsing;


import com.google.inject.Inject;
import com.google.inject.Provider;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.exception.OcrParserException;
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
    private final PngConverter pngConverter;
    private MainConfiguration configuration;
    private HashMap<String, String> mapping = null;

    @Inject
    public OCRProcessorImpl1(PngConverter pngConverter, Provider<MainConfiguration> configurationProvider) {
        this.pngConverter = pngConverter;
        configuration = configurationProvider.get();
    }


    public String getOCROfFile(String filename) throws IOException, TikaException, SAXException {

        if (tPath == null) {
            tPath = configuration.path_tesseract();
        }
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        TesseractOCRConfig tesseractOCRConfig = new TesseractOCRConfig();
        tesseractOCRConfig.setTesseractPath(tPath);
        tesseractOCRConfig.setLanguage(configuration.getOcrPacket());
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
        return handler.toString();
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
