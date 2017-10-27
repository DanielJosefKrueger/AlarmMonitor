package de.alarm_monitor.printing;

import de.alarm_monitor.main.MainConfiguration;
import de.alarm_monitor.main.MainConfigurationLoader;
import de.alarm_monitor.printing.Printer;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

public class PrintingService extends Thread {


    private final File toPrint;
    private final int numberOfCopies;
    private final Boolean shouldPrint;

    public PrintingService(File toPrint) {
        this.toPrint = toPrint;
        MainConfiguration configuration = MainConfigurationLoader.getConfig();
        this.numberOfCopies = configuration.numerOfCopies();
        this.shouldPrint = configuration.isPrintingActive();
    }


    @Override
    public void run() {
        if (!shouldPrint) {
            return;
        } else {
            try {
                Printer.print(toPrint, numberOfCopies);
            } catch (IOException e) {
                //TODO
            } catch (PrinterException e) {
                //TODO
            }
        }

    }

}
