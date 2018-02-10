package de.alarm_monitor.printing;

import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.security.AlertAdminReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

public class PrintingService extends Thread {

    private static final Logger logger = LogManager.getLogger(PrintingService.class);
    private final static Logger log = LogManager.getLogger(PrintingService.class);
    private final AlertAdminReporter alertAdminReporter;
    private final File toPrint;
    private final int numberOfCopies;
    private final Boolean shouldPrint;
    private final MainConfiguration configuration;


    public PrintingService(final AlertAdminReporter alertAdminReporter,
                           final File toPrint,
                           final MainConfiguration configuration) {

        this.alertAdminReporter = alertAdminReporter;
        this.toPrint = toPrint;
        this.configuration = configuration;
        this.numberOfCopies = configuration.numerOfCopies();
        this.shouldPrint = configuration.isPrintingActive();
    }


    @Override
    public void run() {
        if (shouldPrint) {
            try {
                Printer.print(toPrint, numberOfCopies);
            } catch (IOException | PrinterException e) {
                log.error("Error while printing", e);
                alertAdminReporter.sendAlertToAdmin("Error while printing ", e);
            }
        }
    }
}
