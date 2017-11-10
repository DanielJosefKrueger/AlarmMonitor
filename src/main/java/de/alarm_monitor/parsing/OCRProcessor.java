package de.alarm_monitor.parsing;

import de.alarm_monitor.exception.OcrParserException;

import java.io.File;
import java.io.IOException;

public interface OCRProcessor {


     String pdfToString(File pdf) throws OcrParserException;
}
