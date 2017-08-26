package de.alarm_monitor.printing;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;

public class Printer {

    private static final Logger log = LoggerFactory.getLogger(Printer.class);


    public static void print(PDDocument doc) {

        if (doc == null) {
            return;
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

            job.print();
        } catch (PrinterException e) {
            log.error("", e);
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();

            PageFormat pf = job.defaultPage(); // standard PageFormat holen (bei
            // uns a4 - beinhaltet groe�e in
            // pixeln und r�nder
            Paper temp = pf.getPaper();
            temp.setImageableArea(0, 0, temp.getWidth(), temp.getHeight());
            pf.setPaper(temp); // Den Bedruckbaren Rand auf Seitengr��e anpassen
            PDDocument pdf = PDDocument.load(new File("test.pdf"));
            job.setPrintable(new PDFPrintable(pdf), pf);

            // job.setPageable(new PDFPageable(pdf));

            job.print();
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
