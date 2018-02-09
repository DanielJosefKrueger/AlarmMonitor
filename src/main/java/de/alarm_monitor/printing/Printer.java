package de.alarm_monitor.printing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class Printer {

    private static final Logger logger = LogManager.getLogger(Printer.class);


    public static void print(PDDocument doc, int numberOfCopies) {

        if (doc == null) {
            throw new NullPointerException();
        }

        if (numberOfCopies < 1) {
            throw new IllegalArgumentException("Number of copies was negative: " + numberOfCopies);
        }


        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            PageFormat pf = job.defaultPage(); // standard PageFormat holen (bei
            // uns a4 - beinhaltet groe�e in
            // pixeln und r�nder
            Paper temp = pf.getPaper();
            temp.setImageableArea(0, 0, temp.getWidth(), temp.getHeight());
            pf.setPaper(temp); // Den Bedruckbaren Rand auf Seitengr��e anpassen
            job.setPrintable(new PDFPrintable(doc), pf);
            job.setCopies(numberOfCopies);
            job.print();
        } catch (PrinterException e) {
            logger.error("", e);
            e.printStackTrace();
        }
    }

    public static void print(File file, int numberOfCopies) throws PrinterException, IOException {

        if (file == null) {
            throw new NullPointerException();
        }
        if (numberOfCopies < 1) {
            throw new IllegalArgumentException("Number of copies was negative: " + numberOfCopies);
        }

        PDDocument doc = PDDocument.load(file);
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage(); // standard PageFormat holen (bei
        // uns a4 - beinhaltet groe�e in
        // pixeln und r�nder
        Paper temp = pf.getPaper();
        temp.setImageableArea(0, 0, temp.getWidth(), temp.getHeight());
        pf.setPaper(temp); // Den Bedruckbaren Rand auf Seitengr��e anpassen
        job.setPrintable(new PDFPrintable(doc), pf);
        job.setCopies(numberOfCopies);
        job.print();
    }


   /* public static void main(String[] args) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();

            PageFormat pf = job.defaultPage(); // standard PageFormat holen (bei
            // uns a4 - beinhaltet groe�e in
            // pixeln und r�nder
            Paper temp = pf.getPaper();
            temp.setImageableArea(0, 0, temp.getWidth(), temp.getHeight());
            pf.setPaper(temp); // Den Bedruckbaren Rand auf Seitengr��e anpassen
            PDDocument pdf = PDDocument.load(new File("exception.pdf"));
            job.setPrintable(new PDFPrintable(pdf), pf);

            // job.setPageable(new PDFPageable(pdf));

            job.print();
        } catch (Exception e) {
            logger.error("", e);
        }
    }*/

}
