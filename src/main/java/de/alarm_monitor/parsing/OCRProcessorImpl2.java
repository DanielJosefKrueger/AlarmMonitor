package de.alarm_monitor.parsing;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.exception.OcrParserException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;

public class OCRProcessorImpl2 implements OCRProcessor {

    private final PngConverter pngConverter;
    private final MainConfiguration mainConfiguration;

    @Inject
    public OCRProcessorImpl2(PngConverter pngConverter, Provider<MainConfiguration> configurationProvider) {
        this.pngConverter = pngConverter;
        mainConfiguration = configurationProvider.get();
    }

    @Override
    public String pdfToString(File pdf) throws OcrParserException {
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        //instance.setDatapath(mainConfiguration.path_tesseract());
        //LoadLibs.extractTessResources("tessdata");

        //In case you don't have your own tessdata, let it also be extracted for you
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        //Set the tessdata path
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        try {
            return instance.doOCR(pngConverter.convertToBufferedImage(pdf));
        } catch (Exception e) {
            throw new OcrParserException(e);
        }
    }
}
