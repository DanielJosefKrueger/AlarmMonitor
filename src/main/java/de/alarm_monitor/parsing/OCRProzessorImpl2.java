package de.alarm_monitor.parsing;


import com.asprise.ocr.Ocr;
import de.alarm_monitor.exception.OcrParserException;

import java.io.File;
import java.io.IOException;

public class OCRProzessorImpl2 implements OCRProcessor {





    @Override
    public String pdfToString(File pdf) throws OcrParserException {
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
        return s;
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
